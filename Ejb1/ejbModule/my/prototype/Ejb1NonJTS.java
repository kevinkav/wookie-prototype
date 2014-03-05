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
package my.prototype;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.prototype.api.Ejb1Remote;
import my.prototype.api.Ejb3Remote;
import my.prototype.api.TestCase;
import my.prototype.entity.Film;


@Stateless
@Local(TestCase.class)
@Remote(Ejb1Remote.class)
public class Ejb1NonJTS extends TestBase implements Ejb1Remote {

    private static final Logger LOGGER = Logger.getLogger(Ejb1NonJTS.class.getCanonicalName());
    private static final String BEAN3_LOOKUP_NAME = "ejb:Ear3/Ejb3/Ejb3!my.prototype.api.Ejb3Remote";
    private Film film;
    private String localCountryOfOriginValue;
    private String remoteCountryOfOriginValue;

    @Inject
    BeanLocator beanLocator;
    

    @Override
    public String getAttributeCountryOfOrigin_RemoteCall(long id) throws RemoteException {
        LOGGER.info("### Ejb1NonJTS: getAttributeCountryOfOrigin_RemoteCall with id: " + id);
        Film film = em.find(my.prototype.entity.Film.class, id);
        return film.getCountryOfOrigin();
    }

 
    /**
     * Called from other Application Server to create Cast object
     */
    @Override
    public void createCast_RemoteCall(String leadActor) throws RemoteException {
        LOGGER.info("Locating EJB3...");
        Ejb3Remote ejb3 = (Ejb3Remote) beanLocator
                .locateBean(BEAN3_LOOKUP_NAME);
        try {
            if (ejb3 != null){
                ejb3.runTest(STAR_WARS_ID);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void runTest() {
        // print initial film data
        film = findFilm(STAR_WARS_ID);
        LOGGER.info("### Ejb1NonJTS: Initial Film: " + film.toString());

        // change attribute
        film.setCountryOfOrigin("country_ejb1");
        film = findFilm(STAR_WARS_ID);
        localCountryOfOriginValue = film.getCountryOfOrigin();
        LOGGER.info("### Ejb1NonJTS: Updated CountryOfOrigin attibute locally in Film: " + film.toString());

        // Call Ejb3
        Ejb3Remote ejb3Remote = (Ejb3Remote) beanLocator.locateBean(BEAN3_LOOKUP_NAME);
        try {
            remoteCountryOfOriginValue = ejb3Remote.runTest(STAR_WARS_ID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // print both results
        LOGGER.info("### Ejb1NonJTS: local CountryOfOrigin value: " + localCountryOfOriginValue);
        LOGGER.info("### Ejb1NonJTS: remote CountryOfOrigin value: " + remoteCountryOfOriginValue);
        LOGGER.info("### Committed transaction.");
    }
    

}
