package my.prototype.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.prototype.entity.Cast;
import my.prototype.entity.Film;
import my.prototype.test.api.TestCase;


public abstract class Ejb1Base implements TestCase{

    public static final String LEAD_ACTOR = "Harrison Ford";
    public static final String USA = "USA";
    public static final String DIRECTOR = "George Lucas";
    public static final String STAR_WARS = "StarWars";
    public static final String COUNTRY_OF_ORIGIN_USA_CHANGED = "USA_CHANGED";
    public static final String COUNTRY_OF_ORIGIN_USA = "USA";
    public static final long STAR_WARS_ID = 1l;
    public static final long CAST_ID = 2l;  
    private Film film;
    private static final Logger LOG = Logger.getLogger(Ejb1Base.class.getCanonicalName());
    
    @PersistenceContext(unitName = "FilmDatabase")
    public EntityManager em;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String setUp() {
        Film film = createFilm(STAR_WARS, STAR_WARS_ID, DIRECTOR, 122, 1977, COUNTRY_OF_ORIGIN_USA);
        em.persist(film);
        LOG.info("### EJB1: Created and persisted film: " + film.toString());   
        return "Created film: " + film.getName() + " FilmId: " + STAR_WARS_ID;
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String tearDown() {
        StringBuilder sb = new StringBuilder();
        sb.append("Deleted CastId: ");
        Query query = em.createQuery("Select c FROM my.prototype.entity.Cast c");
        final List<Cast> casts = query.getResultList();
        for (final Cast cast : casts) {
            LOG.info("### EJB1: Deleting cast: " + cast.getId() + " " + cast.getLeadActor());
            sb.append(cast.getId());
            em.remove(cast);
        }
        sb.append(" Deleted FilmId: ");
        query = em.createQuery("Select p FROM my.prototype.entity.Film p");
        final List<Film> films = query.getResultList();
        for (final Film film : films) {
            LOG.info("### EJB1: Deleting film: " + film.getName());
            sb.append(film.getId());
            em.remove(film);
        }
        return sb.toString();
    }
    

    @Override
    public abstract void runTest() throws Exception;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public String getResult() {
        // print updated film data after remote call               
        Film film = findFilm(STAR_WARS_ID);
        String testResult = "Failed";

        if (film != null){
            if (film.getCast() != null){
                /*if (!(localCountryOfOriginValue.equals(USA) && remoteCountryOfOriginValue.equals(USA))){
                if (localCountryOfOriginValue.equals(remoteCountryOfOriginValue)){
                    testResult = "Passed";
                }
            }*/
                Cast cast = film.getCast();
                if ((cast.getLeadActor().equals(LEAD_ACTOR)) && (cast.getId() == CAST_ID)){
                    testResult = "Passed";
                }
            }
        }
        LOG.info("### TestResult: " + testResult);
        return testResult;
    }
    
    public Film findFilm(long id){
        return em.find(my.prototype.entity.Film.class, id);
    }
    
    public void printFilmContents(long filmId){
        LOG.info("### EJB1: printFilmContents... ");
        Film f = findFilm(filmId);
        if (f != null){
            f.toString();
        }
    }
    
    /**
     * Changes the CountryOfOrigin attribute and returns
     * the new value of the attribute.
     * 
     * @return the new attribute value of CountryOfOrigin
     */
    protected String changeCountryOfOrigin(){
        film = findFilm(STAR_WARS_ID);
        LOG.info("Initial Film attributes: " + film.toString());
        film.setCountryOfOrigin(COUNTRY_OF_ORIGIN_USA_CHANGED);
        film = findFilm(STAR_WARS_ID);
        String localCountryOfOriginValue = film.getCountryOfOrigin();
        LOG.info("Changed CountryOfOrigin attribute: " + film.toString());
        return localCountryOfOriginValue;
    }
    
    protected void printLocalAndRemoteValues(String localVal, String remoteVal){
        LOG.info("Local CountryOfOrigin value: " + localVal);
        LOG.info("Remote CountryOfOrigin value: " + remoteVal);
        if (!localVal.equals(remoteVal)){
            LOG.info("CountryOfOrigin local and remote values are different !!");
            LOG.info("Ejb2 did not see the changed value inside the same transaction/entitymanager started in Ejb1!!");
        }
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
    
    /**
     * Called by Ejb2 from node2 application server to get the current
     * value of the CountryOfOrigin attribute
     * 
     * @param id
     * @return the current CountryOfOrigin attribute value
     */
    public String getCountryOfOrigin(final long id) {
        LOG.info("getCountryOfOrigin with id: " + id);
        Film film = em.find(my.prototype.entity.Film.class, id);
        String attr = film.getCountryOfOrigin();
        LOG.info("Found attribute CountryOfOrigin value: " + attr);
        return attr;
    }
    
    /**
     * Called by Ejb2 from node2 application server to create a Cast object
     * in the Film object
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

}
