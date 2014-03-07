/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package my.prototype.ejb;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.prototype.test.api.TestCase;
import my.prototype.entity.Cast;
import my.prototype.entity.Film;
import my.remote.v3.api.Ejb1Remote;
import my.remote.v3.api.Ejb2Remote;
import my.remote.v3.bean.locator.BeanLocator;


@Stateless
@Local(TestCase.class)
@Remote(Ejb1Remote.class)
public class Ejb1_v3 extends TestBase implements Ejb1Remote {

    private static final Logger LOG = Logger.getLogger(Ejb1_v3.class.getCanonicalName());
    private static final String BEAN3_LOOKUP_NAME = "ejb:Ear2/B/Ejb2_v3!my.remote.v3.api.Ejb2Remote";
    private Film film;
    private String localCountryOfOriginValue;
    private String remoteCountryOfOriginValue;

    @Inject
    BeanLocator beanLocator;
    

    @Override
    public String getCountryOfOrigin(long id) throws RemoteException {
        LOG.info("getAttributeCountryOfOrigin_RemoteCall with id: " + id);
        Film film = em.find(my.prototype.entity.Film.class, id);
        String attr = film.getCountryOfOrigin();
        LOG.info("Found attribute CountryOfOrigin value: " + attr);
        return attr;
    }

 
    /**
     * Called from other Application Server to add Cast object to Film object
     */
    @Override
    public void addCastToFilm() throws RemoteException {
        LOG.info("addCastToFilm");
        Cast cast = new Cast();
        cast.setId(CAST_ID);
        cast.setLeadActor(LEAD_ACTOR);
        Film f = findFilm(STAR_WARS_ID);
        f.setCast(cast);
        em.persist(cast);
        em.persist(f);
        LOG.info("Created Cast in Film: " + cast.toString());
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void runTest() {
        // print initial film data
        film = findFilm(STAR_WARS_ID);
        LOG.info("Initial Film: " + film.toString());

        // change attribute
        film.setCountryOfOrigin(COUNTRY_OF_ORIGIN_CHANGED);
        film = findFilm(STAR_WARS_ID);
        localCountryOfOriginValue = film.getCountryOfOrigin();
        LOG.info("Updated CountryOfOrigin attibute locally in Film: " + film.toString());

        // Call Ejb3
        Ejb2Remote ejb3Remote = (Ejb2Remote) beanLocator.locateBean(BEAN3_LOOKUP_NAME);
        try {
            remoteCountryOfOriginValue = ejb3Remote.runTest(STAR_WARS_ID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // print both results
        LOG.info("local CountryOfOrigin value: " + localCountryOfOriginValue);
        LOG.info("remote CountryOfOrigin value: " + remoteCountryOfOriginValue);
        if (!remoteCountryOfOriginValue.equals("USA")){
            LOG.info("Ejb2 doesn't see changed CountryOfOrigin value: " + COUNTRY_OF_ORIGIN_CHANGED);
        }
        LOG.info("Commiting transaction...");
    }
    

}
