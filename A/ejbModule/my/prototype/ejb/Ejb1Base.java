package my.prototype.ejb;

import static my.prototype.common.Constants.CAST_ID;
import static my.prototype.common.Constants.HARRISON_FORD;
import static my.prototype.common.Constants.IRELAND;
import static my.prototype.common.Constants.USA;
import static my.prototype.common.Constants.GEORGE_LUCAS;
import static my.prototype.common.Constants.FILM_ID;
import static my.prototype.common.Constants.STAR_WARS;
import static my.prototype.common.Constants.RUNNING_TIME;
import static my.prototype.common.Constants.YEAR;
import static my.remote.common.Constants.SERVER_A;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.prototype.entity.Cast;
import my.prototype.entity.Film;
import my.prototype.test.api.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class Ejb1Base implements TestCase{

    private static final Logger LOG = LoggerFactory.getLogger(Ejb1Base.class);  
    
    @PersistenceContext(unitName = "FilmDatabase")
    private EntityManager em;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String setUp() {
        Film film = createFilm(STAR_WARS, 
        						FILM_ID, 
        						GEORGE_LUCAS, 
        						RUNNING_TIME, 
        						YEAR, 
        						USA);
        em.persist(film);
        return SERVER_A + " created " + film.toString();
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String tearDown() {
        StringBuilder sb = new StringBuilder();
        sb.append("Deleted CastId: ");
        Query query = em.createQuery("Select c FROM my.prototype.entity.Cast c");
        final List<Cast> casts = query.getResultList();
        for (final Cast cast : casts) {
            sb.append(cast.getId());
            em.remove(cast);
        }
        sb.append(" Deleted FilmId: ");
        query = em.createQuery("Select p FROM my.prototype.entity.Film p");
        final List<Film> films = query.getResultList();
        for (final Film film : films) {
            sb.append(film.getId());
            em.remove(film);
        }
        LOG.info("[{}] [{}]", SERVER_A, sb.toString());
        return sb.toString();
    }
    

    @Override
    public abstract String runTest() throws Exception;
    
    /**
     * Method thats called from server-B to get the current
     * value of the CountryOfOrigin attribute in the active transaction
     * 
     * @param id
     * @return the current CountryOfOrigin attribute value
     */
    public String getCountryOfOrigin(final long id) {
        LOG.info("[{}] getCountryOfOrigin with id [{}] ", SERVER_A, id);
        Film film = em.find(my.prototype.entity.Film.class, id);
        String attr = film.getCountryOfOrigin();
        LOG.info("[{}] found CountryOfOrigin value [{}] ", SERVER_A, attr);
        return attr;
    }
    
    /**
     * Called by Ejb2 from node2 application server to create a Cast object
     * in the Film object
     */
    public void addCastToFilm(){
        LOG.info("[{}] adding Cast object to Film", SERVER_A);
        Cast cast = new Cast();
        cast.setId(CAST_ID);
        cast.setLeadActor(HARRISON_FORD);
        Film film = find(FILM_ID, my.prototype.entity.Film.class);
        film.setCast(cast);
        //em.persist(cast);
        //em.persist(f);
        persist(cast);
        persist(film);
        LOG.info("[{}] created Cast object[{}] ", SERVER_A, cast.toString());
    }
        
    
    /**
     * Sets the CountryOfOrigin attribute and returns
     * the new value of the attribute.
     * 
     * @return the new attribute value of CountryOfOrigin
     */
    protected String setCountryOfOrigin(String newCountry){
        Film film = find(FILM_ID, my.prototype.entity.Film.class);
        LOG.info("[{}] before setting CountryOfOrigin attribute value is [{}] ", SERVER_A, film.getCountryOfOrigin());
        film.setCountryOfOrigin(newCountry);
        film = find(FILM_ID, my.prototype.entity.Film.class);
        String updatedCountryOfOrigin = film.getCountryOfOrigin();
        LOG.info("[{}] set new CountryOfOrigin value to [{}] ",  SERVER_A, updatedCountryOfOrigin);
        return updatedCountryOfOrigin;
    }
     
    protected boolean verifyCast() {
    	LOG.info("[{}] verifying Cast", SERVER_A);
		boolean result = false;
		Film film = find(FILM_ID, my.prototype.entity.Film.class);
		if (film != null){
            if (film.getCast() != null){
                Cast cast = film.getCast();
                if ((cast.getLeadActor().equals(HARRISON_FORD)) && (cast.getId() == CAST_ID)){
                	result = true;    	
                }
            }
        }
		LOG.info("[{}] verify cast result passed? [{}]", SERVER_A, result);
		return result;
	}

	protected boolean verifyCountryOfOrigin(String localVal, String remoteVal) {
		LOG.info("[{}] verifying CountryOfOriginAttribute, localValue [{}]"
				+ " remoteValue [{}]", SERVER_A, localVal, remoteVal);
		boolean result = (localVal.equals(remoteVal) && 
				remoteVal.equals(IRELAND));
		LOG.info("[{}] verify CountryOfOrigin passed? [{}]", SERVER_A, result);
		return result;
	}
	
	private Film find(long id, Class clazz){
		LOG.info("[{}] find object with id [{}]", SERVER_A, id);
        return em.find(clazz, id);
    }
	
	private void persist(Object obj){
		em.persist(obj);
	}
    
    private Film createFilm(String filmName, long id,  String director, int time, int year, String country) {
    	LOG.info("[{}] creating new film...", SERVER_A);
        Film film = new Film();
        film.setId(id);
        film.setName(filmName);
        film.setDirector(director);
        film.setRunningTimeMins(time);
        film.setYearOfRelease(year);
        film.setCountryOfOrigin(country);
        LOG.info("[{}] created film [{}}", film.toString());
        return film;
    }

}
