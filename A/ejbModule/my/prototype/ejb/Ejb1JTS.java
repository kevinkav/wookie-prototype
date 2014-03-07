package my.prototype.ejb;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import my.prototype.test.api.TestCase;
import my.prototype.entity.Cast;
import my.prototype.entity.Film;
import my.prototype.remote.home.api.Ejb1RemoteHome;
import my.prototype.remote.home.api.Ejb2RemoteHome;
import my.prototype.remote.home.api.Ejb2RemoteObject;


/**
 * Session Bean implementation class Ejb1
 */
@Stateless
@Local(TestCase.class)
@RemoteHome(Ejb1RemoteHome.class)
@EJB(name = my.prototype.remote.home.api.Ejb1RemoteObject.EJB1_BINDING_JNDI, beanInterface = my.prototype.remote.home.api.Ejb1RemoteObject.class)
public class Ejb1JTS extends TestBase {


    private String localCountryOfOriginValue;
    private String remoteCountryOfOriginValue;
    final String ejb2Address = "corbaname:iiop:localhost:3628#" + Ejb2RemoteObject.EJB2_BINDING_JNDI;
    private Film film;
    private static final Logger LOGGER = Logger.getLogger(Ejb1JTS.class.getCanonicalName());    


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void runTest() {
        try{
            // print initial film data
            film = findFilm(STAR_WARS_ID);
            LOGGER.info("### EJB1: Initial Film: " + film.toString());

            // change attribute
            film.setCountryOfOrigin("country_ejb1");
            film = findFilm(STAR_WARS_ID);
            localCountryOfOriginValue = film.getCountryOfOrigin();
            LOGGER.info("### EJB1: Updated CountryOfOrigin attibute locally in Film: " + film.toString());

            // Make remote call
            Ejb2RemoteObject ejb2Remote = (Ejb2RemoteObject) getEjbRemoteObject(ejb2Address);
            remoteCountryOfOriginValue = ejb2Remote.runTest(STAR_WARS_ID);

            // print both results
            LOGGER.info("### EJB1: local CountryOfOrigin value: " + localCountryOfOriginValue);
            LOGGER.info("### EJB1: remote CountryOfOrigin value: " + remoteCountryOfOriginValue);
            LOGGER.info("### Commiting transaction...");
        }catch (Exception e){
            LOGGER.severe("+++++++++++++++++ Exception !!!! ++++++++++++++++");
            e.printStackTrace();
        }
    }



    public String getAttributeCountryOfOrigin_RemoteCall(final long id) {
        LOGGER.info("### EJB1: getAttributeCountryOfOrigin_RemoteCall with id: " + id);
        Film film = em.find(my.prototype.entity.Film.class, id);
        return film.getCountryOfOrigin();
    }


    /**
     * Called from other Application Server to create Cast object
     */
    public void createCast_RemoteCall(final String leadActor){
        LOGGER.info("### EJB1: createCast_RemoteCall with Lead actor: " + leadActor);
        Cast cast = new Cast();
        cast.setId(CAST_ID);
        cast.setLeadActor(leadActor);
        Film f = findFilm(STAR_WARS_ID);
        f.setCast(cast);
        em.persist(cast);
        em.persist(f);
        LOGGER.info("### EJB1: Created Cast instance and set in Film: " + cast.toString());
    }


    private Object getEjbRemoteObject(String ejbAddress) throws NamingException, RemoteException, CreateException {
        LOGGER.info("Looking up address -->  " + ejbAddress);
        InitialContext ctx = new InitialContext();
        final Object iiopObject = ctx.lookup(ejbAddress);
        Object remoteObj = null;
        if (ejbAddress.contains(Ejb2RemoteObject.EJB2_BINDING_JNDI)){
            Ejb2RemoteHome ejb2RemoteHome = (my.prototype.remote.home.api.Ejb2RemoteHome) PortableRemoteObject
                    .narrow(iiopObject, my.prototype.remote.home.api.Ejb2RemoteHome.class);
            remoteObj = ejb2RemoteHome.create(); 
        }
        return remoteObj;
    }


}


