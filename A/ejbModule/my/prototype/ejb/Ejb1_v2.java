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
import my.remote.v2.home.api.Ejb1RemoteHome;
import my.remote.v2.home.api.Ejb1RemoteObject;
import my.remote.v2.home.api.Ejb2RemoteHome;
import my.remote.v2.home.api.Ejb2RemoteObject;


/**
 * Session Bean implementation class Ejb1
 */
@Stateless
@Local(TestCase.class)
@RemoteHome(Ejb1RemoteHome.class)
@EJB(name = Ejb1RemoteObject.EJB1_BINDING_JNDI, beanInterface = Ejb1RemoteObject.class)
public class Ejb1_v2 extends TestBase {


    private String localCountryOfOriginValue;
    private String remoteCountryOfOriginValue;
    final String ejb2Address = "corbaname:iiop:localhost:3628#" + Ejb2RemoteObject.EJB2_BINDING_JNDI;
    private Film film;
    private static final Logger LOG = Logger.getLogger(Ejb1_v2.class.getCanonicalName());    


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void runTest() {
        try{
            // print initial film data
            film = findFilm(STAR_WARS_ID);
            LOG.info("Initial Film: " + film.toString());

            // change attribute
            film.setCountryOfOrigin(COUNTRY_OF_ORIGIN_CHANGED);
            film = findFilm(STAR_WARS_ID);
            localCountryOfOriginValue = film.getCountryOfOrigin();
            LOG.info("Updated CountryOfOrigin attibute locally in Film: " + film.toString());

            // Make remote call
            Ejb2RemoteObject ejb2Remote = (Ejb2RemoteObject) getEjbRemoteObject(ejb2Address);
            remoteCountryOfOriginValue = ejb2Remote.runTest(STAR_WARS_ID);

            // print both results
            LOG.info("local CountryOfOrigin value: " + localCountryOfOriginValue);
            LOG.info("remote CountryOfOrigin value: " + remoteCountryOfOriginValue);
            LOG.info("Commiting transaction...");
        }catch (Exception e){
            LOG.severe("+++++++++++++++++ Exception !!!! ++++++++++++++++");
            e.printStackTrace();
        }
    }



    public String getCountryOfOrigin(final long id) {
        LOG.info("getCountryOfOrigin with id: " + id);
        Film film = em.find(my.prototype.entity.Film.class, id);
        return film.getCountryOfOrigin();
    }


    /**
     * Called from other Application Server to create Cast object
     */
    public void addCastToFilm(){
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


    private Object getEjbRemoteObject(String ejbAddress) throws NamingException, RemoteException, CreateException {
        LOG.info("Looking up address:  " + ejbAddress);
        InitialContext ctx = new InitialContext();
        final Object iiopObject = ctx.lookup(ejbAddress);
        Object remoteObj = null;
        if (ejbAddress.contains(Ejb2RemoteObject.EJB2_BINDING_JNDI)){
            Ejb2RemoteHome ejb2RemoteHome = (Ejb2RemoteHome) PortableRemoteObject
                    .narrow(iiopObject, Ejb2RemoteHome.class);
            remoteObj = ejb2RemoteHome.create(); 
        }
        return remoteObj;
    }


}


