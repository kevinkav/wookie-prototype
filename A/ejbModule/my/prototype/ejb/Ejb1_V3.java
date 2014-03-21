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

import javax.ejb.EJB;
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
@EJB(name=Ejb1Remote.EJB1_JNDI_LOOKUP, beanInterface=Ejb1Remote.class)
public class Ejb1_V3 extends Ejb1Base implements Ejb1Remote {

    private static final Logger LOG = Logger.getLogger(Ejb1_V3.class.getCanonicalName());
    //private static final String EJB2_LOOKUP_NAME = "ejb:Ear2/B/Ejb2_v3!my.remote.v3.api.Ejb2Remote";

    @Inject
    BeanLocator beanLocator;
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void runTest() throws Exception {
        try {
            String local_CountryOfOriginValue = changeCountryOfOrigin();
            // Call Ejb2
            Ejb2Remote ejb2Remote = (Ejb2Remote) beanLocator.locateBean(Ejb2Remote.EJB2_JNDI_LOOKUP);
            String remote_CountryOfOriginValue = ejb2Remote.getCountryOfOriginAndCreateCast(STAR_WARS_ID);
            // print both results
            printLocalAndRemoteValues(local_CountryOfOriginValue, remote_CountryOfOriginValue);
        } catch (Exception e) {
            LOG.severe("Exception occurred rolling back transaction...");
            throw e;
        }
        LOG.info("Commiting transaction...");
    }
    
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
    
}
