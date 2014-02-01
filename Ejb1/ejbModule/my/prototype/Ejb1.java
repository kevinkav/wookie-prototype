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
import my.prototype.remote.home.api.Ejb3RemoteObject;


/**
 * Session Bean implementation class StatelessEjb1
 */
@Stateless
@Local(Ejb1Local.class)
@RemoteHome(Ejb1RemoteHome.class)
@EJB(name = my.prototype.remote.home.api.Ejb1RemoteObject.EJB1_BINDING_JNDI, beanInterface = my.prototype.remote.home.api.Ejb1RemoteObject.class)
public class Ejb1 implements Ejb1Local {

    /**
     * 
     */
    private static final String USA = "USA";

    /**
     * 
     */
    private static final String DIRECTOR = "George Lucas";

    /**
     * 
     */
    private static final String NAME = "name";

    /**
     * 
     */
    private static final String STAR_WARS = "StarWars";
    
    private static final long STAR_WARS_ID = 1l;

    @PersistenceContext(unitName = "FilmDatabase")
    private EntityManager em;
    
    // Vital for solution!
    final String ejb3Address = "corbaname:iiop:localhost:3628#" + Ejb3RemoteObject.EJB3_BINDING_JNDI;

    Film film;
    
    Film anotherFilm;
    
    long filmId = 1;
    
    private my.prototype.remote.home.api.Ejb3RemoteObject ejb3Remote;
    
    private static final Logger LOGGER = Logger.getLogger(Ejb1.class.getCanonicalName());

    
    /* (non-Javadoc)
     * @see my.protoype.api.Ejb1Local#runTest()
     */
    @Override
    //@TransactionAttribute(TransactionAttributeType.NEVER)
    //@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String runTest1() throws Exception {
        
        String localValue;
        String remoteValue;
        String result = "";
        
        initTestFilm();
        LOGGER.info("### EJB1 Initial Data: " + film.toString());
        
        changeAttribute();
        localValue = getAttributeCountryOfOrigin(filmId);
        
        LOGGER.info("### EJB1 Changed Data: " + film.toString());
        
        ejb3Remote = getEjb3Object();
        remoteValue = ejb3Remote.runTest(filmId);
        
        
        LOGGER.info("### EJB1: localValue = " + localValue);
        LOGGER.info("### EJB1: remoteValue = " + remoteValue);
        
        persistFilm(anotherFilm);
        
        if (localValue.equals("?") && remoteValue.equals("?")){
            LOGGER.info("##### Test Failed ######");
            result = "Failed";
        }else if (localValue.equals(remoteValue)){
            LOGGER.info("##### Test Passed ######");
            result = "Passed";
        }else{
            LOGGER.info("##### Test Failed ######");
            result = "Failed";
        }
        return result;
    }

    
    private void changeAttribute() {
        film.setCountryOfOrigin("country_ejb1");
        LOGGER.info("### EJB1: Setting CountryOfOrigin new value: country_ejb1");
    }


    private my.prototype.remote.home.api.Ejb3RemoteObject getEjb3Object() throws NamingException, RemoteException, CreateException {
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
    //@TransactionAttribute(TransactionAttributeType.MANDATORY)
    public String getAttributeCountryOfOrigin(long id) {
        LOGGER.info("Ejb1: em.toString() : " + em.toString());
        Film film = em.find(my.prototype.entity.Film.class, id);
        return film.getCountryOfOrigin();
    }
    
    
    /* (non-Javadoc)
     * @see my.protoype.api.Ejb1Local#runTestSetup()
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setUp() throws Exception {
        Film f = createFilm(STAR_WARS, STAR_WARS_ID, DIRECTOR, 122, 1977, USA);
        persistFilm(f);
    }

    public void persistFilm(Film f){
        em.persist(f);
        LOGGER.info("### EJB1: Persisted film: " + film.toString());
    }
   
    
    private Film createFilm(String filmName, long id,  String director, int time, int year, String country) {
        Film film = new Film();
        film.setId(id);
        film.setName(filmName);
        film.setDirector(director);
        film.setRunningTimeMins(time);
        film.setYearOfRelease(year);
        film.setCountryOfOrigin(country);
        LOGGER.info("### EJB1: Created film instance: " + film.toString());
        return film;
    }
    
    public void createAnotherFilm(String filmName, long id,  String director, int time, int year, String country){
        Film anotherFilm = createFilm(filmName, id, director, time, year, country);
    }
    
    //@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void findFilms() {
        final Query query = em.createQuery("Select p FROM my.prototype.entity.Film p");
        final List<Film> films = query.getResultList();
        LOGGER.info("##########################");
        for (final Film film : films) {
            LOGGER.info(film.toString());
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
            LOGGER.info("### EJB1: Deleting film: " + film.getName());
            em.remove(film);
        }
        findFilms();
    }
    

    private void initTestFilm() {       
        Query query = em.createQuery("Select p FROM my.prototype.entity.Film p WHERE p.name LIKE :name");
        query.setParameter(NAME, STAR_WARS);
        final List<Film> films = query.getResultList();
        film = films.get(0);
        filmId = film.getId();
        film = em.find(my.prototype.entity.Film.class, filmId);
    }    
}


