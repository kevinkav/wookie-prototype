package my.prototype.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.prototype.test.api.TestCase;
import my.prototype.entity.Cast;
import my.prototype.entity.Film;

public abstract class TestBase implements TestCase{

    public static final String LEAD_ACTOR = "Harrison Ford";
    public static final String USA = "USA";
    public static final String DIRECTOR = "George Lucas";
    public static final String STAR_WARS = "StarWars";
    public static final long STAR_WARS_ID = 1l;
    public static final long CAST_ID = 2l;    
    private static final Logger LOGGER = Logger.getLogger(TestBase.class.getCanonicalName());

    @Inject
    public BeanLocator beanLocator;
    
    @PersistenceContext(unitName = "FilmDatabase")
    public EntityManager em;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String setUp() {
        Film film = createFilm(STAR_WARS, STAR_WARS_ID, DIRECTOR, 122, 1977, USA);
        em.persist(film);
        LOGGER.info("### EJB1: Created and persisted film: " + film.toString());   
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
            LOGGER.info("### EJB1: Deleting cast: " + cast.getId() + " " + cast.getLeadActor());
            sb.append(cast.getId());
            em.remove(cast);
        }
        sb.append(" Deleted FilmId: ");
        query = em.createQuery("Select p FROM my.prototype.entity.Film p");
        final List<Film> films = query.getResultList();
        for (final Film film : films) {
            LOGGER.info("### EJB1: Deleting film: " + film.getName());
            sb.append(film.getId());
            em.remove(film);
        }
        return sb.toString();
    }
    

    @Override
    public abstract void runTest();
    
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
        LOGGER.info("### TestResult: " + testResult);
        return testResult;
    }
    
    public Film findFilm(long id){
        return em.find(my.prototype.entity.Film.class, id);
    }
    
    public void printFilmContents(long filmId){
        LOGGER.info("### EJB1: printFilmContents... ");
        Film f = findFilm(filmId);
        if (f != null){
            f.toString();
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

}
