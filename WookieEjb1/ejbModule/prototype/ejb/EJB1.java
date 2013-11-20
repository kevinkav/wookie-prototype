package prototype.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import prototype.ejb.api.StatelessEjb1Remote;
import prototype.entity.StarWars;

/**
 * Session Bean implementation class StatelessEjb1
 */
@Stateless
@EJB(name=StatelessEjb1Remote.REMOTE_JNDI, beanInterface=StatelessEjb1Remote.class)
public class EJB1 implements StatelessEjb1Remote {

    @PersistenceContext(unitName = "FilmDatabase")
    private EntityManager em;
    
    @Override
    public String getStarWars() {
        StarWars film = em.find(prototype.entity.StarWars.class, 1);
        StringBuilder sb = new StringBuilder();
        sb.append("FilmId: " + film.getFilmId() + "<br>");
        sb.append("Director: " + film.getDirector() + "<br>");
        sb.append("Origin: " + film.getCountryOfOrigin() + "<br>");
        sb.append("RunningTimeInMins: " + film.getRunningTimeMins() + "<br>");
        sb.append("YearOfRelease: " + film.getYearOfRelease() + "<br>");
        return sb.toString();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void persist(StarWars film){
        em.persist(film);
    }
    
    @Override
    public StarWars createStartWars() {
        StarWars starWars = new StarWars();
        starWars.setCountryOfOrigin("USA");
        starWars.setDirector("George Lucas");
        starWars.setFilmId(1);
        starWars.setRunningTimeMins(122);
        starWars.setYearOfRelease(1977);
        return starWars;        
    }
    
}


