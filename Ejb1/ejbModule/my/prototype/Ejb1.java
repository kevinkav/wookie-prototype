package my.prototype;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.prototype.api.Ejb1Local;
import my.prototype.entity.StarWars;


/**
 * Session Bean implementation class StatelessEjb1
 */
@Stateless
@Local(Ejb1Local.class)
//@EJB(name=Ejb1Local.REMOTE_JNDI, beanInterface=Ejb1Local.class)
public class Ejb1 implements Ejb1Local {

    @PersistenceContext(unitName = "FilmDatabase")
    private EntityManager em;
    
    private String getStarWars() {
        StarWars film = findStarWars();
        StringBuilder sb = new StringBuilder();
        if(film != null){
            sb.append("FilmId: " + film.getFilmId() + "<br>");
            sb.append("Director: " + film.getDirector() + "<br>");
            sb.append("Origin: " + film.getCountryOfOrigin() + "<br>");
            sb.append("RunningTimeInMins: " + film.getRunningTimeMins() + "<br>");
            sb.append("YearOfRelease: " + film.getYearOfRelease() + "<br>");
        }
        return sb.toString();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private StarWars find() {
        StarWars film = em.find(my.prototype.entity.StarWars.class, 1);
        return film;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private StarWars findStarWars(){
        Query q1 = em.createQuery("Select p FROM my.prototype.entity.StarWars p");
        List<StarWars> list = q1.getResultList();
        StarWars starWars = null;
        if (list.size() > 0){
            assert list.size() == 1;
            starWars = list.get(0);
        }
        return starWars;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void persist(StarWars film){
        em.persist(film);
        em.flush();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void deleteStarWars() {
        StarWars film = findStarWars();
        if(film != null){
            log("Removing old filmId=: " + film.getFilmId() + " ");
            em.remove(film);
        }
    }
    
    private StarWars createStarWars() {
        StarWars starWars = new StarWars();
        starWars.setDirector("George Lucas");
        starWars.setRunningTimeMins(122);
        starWars.setYearOfRelease(1977);
        return starWars;
    }


    /* (non-Javadoc)
     * @see my.protoype.api.Ejb1Local#runTest()
     */
    @Override
    public String runTest() {
        modifyStarWarsCoutryOfOrigin();
        return getStarWars();
    }

    private void modifyStarWarsCoutryOfOrigin() {
        StarWars starWars = findStarWars();
        if(starWars != null){
            starWars.setCountryOfOrigin("modified!");
            persist(starWars);
        }
    }
    

    /* (non-Javadoc)
     * @see my.protoype.api.Ejb1Local#runTestSetup()
     */
    @Override
    public String runTestSetup(String country) {
        deleteStarWars();
        StarWars starWars = createStarWars();
        starWars.setCountryOfOrigin(country);
        persist(starWars);
        return getStarWars();
    }

    private void log (String str){
        System.out.println(str);
    }

    
}


