package my.prototype;

import java.rmi.RemoteException;
import java.util.List;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.rmi.PortableRemoteObject;

import my.prototype.api.Ejb1Local;
import my.prototype.entity.Film;
import my.prototype.remote.home.api.Ejb1RemoteHome;


/**
 * Session Bean implementation class StatelessEjb1
 */
@Stateless
@Local(Ejb1Local.class)
//@Remote(Ejb1Remote.class)
@RemoteHome(Ejb1RemoteHome.class)
@EJB(name = my.prototype.remote.home.api.Ejb1Remote.EJB1_REMOTE_JNDI, beanInterface = my.prototype.remote.home.api.Ejb1Remote.class)
public class Ejb1 implements Ejb1Local {
//public class Ejb1 implements Ejb1Local, Ejb1Remote {

    @PersistenceContext(unitName = "FilmDatabase")
    private EntityManager em;
    
    final String ejb3Address = "corbaname:iiop:localhost:3628#jts/Ejb3";

    Film film;
    
    long filmId = 1;
    
    private my.prototype.remote.home.api.Ejb3Remote ejb3Remote;
    
    private static final Logger LOGGER = Logger.getLogger(Ejb1.class.getCanonicalName());

    
    /* (non-Javadoc)
     * @see my.protoype.api.Ejb1Local#runTest()
     */
    @Override
    public void runTest() throws Exception {
        
        String localValue;
        String remoteValue;
        
        initTestFilm();
       
        LOGGER.info("Ejb1: initialvalue =  " + film.getCountryOfOrigin());
        
        changeAttribute();
        
        localValue = getAttributeCountryOfOrigin(filmId);
        
        LOGGER.info("Ejb1: em.toString() : " + em.toString());
        
                
        ejb3Remote = getEjb3Object();
        remoteValue = ejb3Remote.runTest(filmId);
        
        LOGGER.info("Ejb1: localValue = " + localValue);
        LOGGER.info("Ejb1: remoteValue = " + remoteValue);
        
        if (localValue.equals(remoteValue)){
            LOGGER.info("<<<< Test Passed >>>>>");
        }else{
            LOGGER.info("<<<< Test Failed >>>>>");
        }
    }

    
    /**
     * 
     */
    private void changeAttribute() {
        film.setCountryOfOrigin("country_ejb1");
        LOGGER.info("Ejb1: Set CountryOfOrigin attribute to: country_ejb1");
    }


    private my.prototype.remote.home.api.Ejb3Remote getEjb3Object() throws NamingException, RemoteException, CreateException {
        LOGGER.info("Ejb1: getEjb3RemoteHome....");
        InitialContext ctx = new InitialContext();
        LOGGER.info("Ejb1: Address for looking up Ejb3 =  " + ejb3Address);
        final Object iiopObject = ctx.lookup(ejb3Address);
        my.prototype.remote.home.api.Ejb3RemoteHome ejb3RemoteHome = (my.prototype.remote.home.api.Ejb3RemoteHome) PortableRemoteObject
                .narrow(iiopObject, my.prototype.remote.home.api.Ejb3RemoteHome.class);
        return ejb3RemoteHome.create();
    }
    
   
    /* (non-Javadoc)
     * @see my.prototype.api.Ejb1Remote#getAttributeCountryOfOrigin()
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public String getAttributeCountryOfOrigin(long id) {
        LOGGER.info("Ejb1: em.toString() : " + em.toString());
        Film film = em.find(my.prototype.entity.Film.class, id);
        return film.getCountryOfOrigin();
    }
    
    
    /* (non-Javadoc)
     * @see my.prototype.api.Ejb1Remote#setAttributeCountryOfOrigin(long)
     */
    //@Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setAttributeCountryOfOrigin(long id, String origin) {
        LOGGER.info("Ejb1: Setting attribute 'CountryOfOrigin' to " + origin + " #########");
        LOGGER.info("Ejb1: em.toString() : " + em.toString());

        Film f = em.find(my.prototype.entity.Film.class, id);
        f.setCountryOfOrigin(origin);
        LOGGER.info("Ejb1: Set 'CountryOfOrigin' to " + origin + " #########");
    }
    
    
    /* (non-Javadoc)
     * @see my.protoype.api.Ejb1Local#runTestSetup()
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setUp() throws Exception {
        Film starWars = new Film();
        starWars.setName("StarWars");
        starWars.setDirector("George Lucas");
        starWars.setRunningTimeMins(122);
        starWars.setYearOfRelease(1977);
        starWars.setCountryOfOrigin("?");
        em.persist(starWars);
        findFilms();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void findFilms(){
        final Query query = em.createQuery("Select p FROM my.prototype.entity.Film p");
        final List<Film> films = query.getResultList();
        LOGGER.info("##########################");
        for (final Film film : films) {
            LOGGER.info("Found film: " + film.getName());
        }
        if(films.isEmpty()){
            LOGGER.info("Found no films.");
        }
        LOGGER.info("##########################");
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void tearDown() throws Exception {
        final Query query = em.createQuery("Select p FROM my.prototype.entity.Film p");
        final List<Film> films = query.getResultList();
        for (final Film film : films) {
            LOGGER.info("Deleting film: " + film.getName());
            em.remove(film);
        }
        findFilms();
    }
    

    private void initTestFilm() {       
        Query query = em.createQuery("Select p FROM my.prototype.entity.Film p WHERE p.name LIKE :name");
        query.setParameter("name", "StarWars");
        final List<Film> films = query.getResultList();
        film = films.get(0);
        filmId = film.getId();
        film = em.find(my.prototype.entity.Film.class, filmId);
    }    
}


