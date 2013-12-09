package my.prototype;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

//import org.hibernate.ejb.EntityManagerImpl;
import my.prototype.api.Ejb1Local;
import my.prototype.api.Ejb1Remote;
import my.prototype.entity.Film;


/**
 * Session Bean implementation class StatelessEjb1
 */
@Stateless
@Local(Ejb1Local.class)
@EJB(name=Ejb1Remote.EJB1_REMOTE_JNDI, beanInterface=Ejb1Remote.class)
public class Ejb1 implements Ejb1Local, Ejb1Remote {

    @PersistenceContext(unitName = "FilmDatabase")
    private EntityManager em;
    
    @EJB
    Ejb2 ejb2;
    
    Film film;
    
    long filmId = 1;
    
    @Inject
    protected UserTransaction utx;


    
    /* (non-Javadoc)
     * @see my.protoype.api.Ejb1Local#runTest()
     */
    @Override
    public void runTest() throws Exception {
        
        //setUp();
        
        //utx.begin();
        initialiseTestFilm();
        
        String localValue = null;
        String remoteValue = null;
        
        log ("Initial value: " + getAttributeCountryOfOrigin(filmId));
        
        modifyAttributeCoutryOfOrigin();
        log("Bean1: em.unwrap(org.hibernate.ejb.EntityManagerImpl.class).toString() : " + em.toString());

        
        localValue = getAttributeCountryOfOrigin(filmId);
        log("New local value: " + localValue);
        
        remoteValue = ejb2.runTest(filmId);
        log ("Remote value: " + remoteValue);
        
        if (localValue.equals(remoteValue)){
            log("Test passed!");
        }else{
            log("Test failed!");
        }
        //tearDown();
    }
    


    /* (non-Javadoc)
     * @see my.prototype.api.Ejb1Remote#kickEjb1()
     */
    @Override
    public void kickEjb1() {
        log("Ejb1 kicked by Ejb3!!");
    }
    
    /* (non-Javadoc)
     * @see my.prototype.api.Ejb1Remote#getAttributeCountryOfOrigin()
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    @Override
    public String getAttributeCountryOfOrigin(long id) {
        log("Bean1: em.unwrap(org.hibernate.ejb.EntityManagerImpl.class).toString() : " + em.toString());
        Film f = em.find(my.prototype.entity.Film.class, id);
        return f.getCountryOfOrigin();
    }
    
    
    /* (non-Javadoc)
     * @see my.prototype.api.Ejb1Remote#setAttributeCountryOfOrigin(long)
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public String setAttributeCountryOfOrigin(long id) {
        Film f = em.find(my.prototype.entity.Film.class, id);
        f.setCountryOfOrigin("XYZ");
        return "XYZ";
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
        starWars.setCountryOfOrigin("USA");
        em.persist(starWars);
    }
    
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void tearDown() throws Exception {
        final Query query = em.createQuery("Select p FROM my.prototype.entity.Film p");
        final List<Film> films = query.getResultList();
        for (final Film film : films) {
            log("Deleting film: " + film.getName());
            em.remove(film);
        }
    }
    

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void initialiseTestFilm() {       
        Query query = em.createQuery("Select p FROM my.prototype.entity.Film p WHERE p.name LIKE :name");
        query.setParameter("name", "StarWars");
        final List<Film> films = query.getResultList();
        film = films.get(0);
        filmId = film.getId();
        film = em.find(my.prototype.entity.Film.class, filmId);
    }
    
     
  
    private void modifyAttributeCoutryOfOrigin() {
        film.setCountryOfOrigin("EIRE");
        log("Changing country of origin to EIRE");
    }
    

    
    private void log (String str){
        System.out.println(str);
    }

    
    /*    private String printStarWars() {
    Film film = find(filmId);
    StringBuilder sb = new StringBuilder();
    if(film != null){
        sb.append("FilmId: " + film.getId() + "<br>");
        sb.append("Director: " + film.getDirector() + "<br>");
        sb.append("Origin: " + film.getCountryOfOrigin() + "<br>");
        sb.append("RunningTimeInMins: " + film.getRunningTimeMins() + "<br>");
        sb.append("YearOfRelease: " + film.getYearOfRelease() + "<br>");
    }
    return sb.toString();
}*/

    
}


