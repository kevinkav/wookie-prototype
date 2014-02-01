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
import my.prototype.entity.Cast;
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

    private static final String USA = "USA";

    private static final String DIRECTOR = "George Lucas";

    private static final String NAME = "name";
    
    private static final String ID = "id";

    private static final String STAR_WARS = "StarWars";
    
    private static final long STAR_WARS_ID = 1l;
    
    private static final long CAST_ID = 2l;

    @PersistenceContext(unitName = "FilmDatabase")
    private EntityManager em;
    
    // Vital for solution!
    final String ejb3Address = "corbaname:iiop:localhost:3628#" + Ejb3RemoteObject.EJB3_BINDING_JNDI;

    private Film film;
        
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
        
        //initTestFilm();
        film = findFilm(STAR_WARS);
        LOGGER.info("### EJB1: Initial Film Data: " + film.toString());
        
        // change attribute
        film.setCountryOfOrigin("country_ejb1");
        localValue = getAttributeCountryOfOrigin(STAR_WARS_ID);
        
        LOGGER.info("### EJB1: Changed Data: " + film.toString());
        
        ejb3Remote = getEjb3Object();
        remoteValue = ejb3Remote.runTest(filmId);
        
        
        LOGGER.info("### EJB1: localValue = " + localValue);
        LOGGER.info("### EJB1: remoteValue = " + remoteValue);
                       
        film = findFilm(STAR_WARS);
        LOGGER.info("### EJB1: Film: " + film.toString());
        LOGGER.info("### EJB1: Cast: " + film.getCast().toString());
        
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

    
    public String getAttributeCountryOfOrigin(long id) {
        Film film = em.find(my.prototype.entity.Film.class, id);
        return film.getCountryOfOrigin();
    }
    

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setUp() throws Exception {
        Film film = createFilm(STAR_WARS, STAR_WARS_ID, DIRECTOR, 122, 1977, USA);
        em.persist(film);
        LOGGER.info("### EJB1: Created and persisted film: " + film.toString());
    }
    
    public void createCast(String leadActor){
        Cast cast = new Cast();
        cast.setId(2);
        cast.setLeadActor(leadActor);
        Film f = findFilm(STAR_WARS);
        f.setCast(cast);
        LOGGER.info("### EJB1: Created Cast instance and set in Film: " + cast.toString());
    }
    
    public void findFilms() {
        final Query query = em.createQuery("Select p FROM my.prototype.entity.Film p");
        final List<Film> films = query.getResultList();
        LOGGER.info("#########Find Films#################");
        for (final Film film : films) {
            LOGGER.info(film.toString());
        }
        if(films.isEmpty()){
            LOGGER.info("No films found.");
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
    }
    

    private void initTestFilm() {       
        film = findFilm(STAR_WARS);
        filmId = film.getId();
        film = em.find(my.prototype.entity.Film.class, filmId);
    }  

    
    private Film createFilm(String filmName, long id,  String director, int time, int year, String country) {
        Film film = new Film();
        film.setId(id);
        film.setName(filmName);
        film.setDirector(director);
        film.setRunningTimeMins(time);
        film.setYearOfRelease(year);
        film.setCountryOfOrigin(country);
        return film;
    }
    
    
    private Film findFilm(String filmName){
        Query query = em.createQuery("Select p FROM my.prototype.entity.Film p WHERE p.name LIKE :name");
        query.setParameter(NAME, filmName);
        final List<Film> films = query.getResultList();
        return films.get(0);
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
    

}


