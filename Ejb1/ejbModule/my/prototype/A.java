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
package my.prototype;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.rmi.PortableRemoteObject;

import my.prototype.api.ALocal;
import my.prototype.api.Ejb1Remote;
import my.prototype.api.Ejb3Remote;
import my.prototype.entity.Film;
import my.prototype.remote.home.api.Ejb3RemoteObject;

@Remote(Ejb1Remote.class)
@Local(ALocal.class)
@Stateless
public class A implements ALocal, Ejb1Remote{

    
    @PersistenceContext(unitName = "FilmDatabase")
    private EntityManager em;
    
    Film film;
    
    long filmId = 1;
    
    private my.prototype.api.Ejb3Remote ejb3Remote;
    
    private static final Logger LOGGER = Logger.getLogger(Ejb1.class.getCanonicalName());

    

    @Override
    public String runTest() throws Exception {
        
        String localValue;
        String remoteValue;
        String result = "";
        
        initTestFilm();
       
        LOGGER.info("Ejb1: initialvalue =  " + film.getCountryOfOrigin());
        
        changeAttribute();
        
        localValue = getAttributeCountryOfOrigin(filmId);
        
        LOGGER.info("Ejb1: em.toString() : " + em.toString());
        
                
        ejb3Remote = resolveBean(Ejb3Remote.EJB3_REMOTE_JNDI);
        
        remoteValue = ejb3Remote.runTest(filmId);
        
        LOGGER.info("Ejb1: localValue = " + localValue);
        LOGGER.info("Ejb1: remoteValue = " + remoteValue);
        
        if (localValue.equals(remoteValue)){
            LOGGER.info("<<<< Test Passed >>>>>");
            result = "Passed";
        }else{
            LOGGER.info("<<<< Test Failed >>>>>");
            result = "Failed";
        }
        return result;
    }

    
  
    private void changeAttribute() {
        film.setCountryOfOrigin("country_ejb1");
        LOGGER.info("Ejb1: Set CountryOfOrigin attribute to: country_ejb1");
    }


    private static <T> T resolveBean(final String jndiName) {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        try {
            final InitialContext ctx = new InitialContext(populateProperties());
            @SuppressWarnings("unchecked")
            final T bean = (T) ctx.lookup(jndiName);
            return bean;
        } catch (NamingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
    private static Properties populateProperties() {
        final Properties jndiProps = new Properties();
   
        jndiProps.put(Context.PROVIDER_URL, "remote://127.0.0.1:4547");
 
        jndiProps.put("jboss.naming.client.ejb.context", true);
        return jndiProps;
}
    

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public String getAttributeCountryOfOrigin(long id) {
        LOGGER.info("Ejb1: em.toString() : " + em.toString());
        Film film = em.find(my.prototype.entity.Film.class, id);
        return film.getCountryOfOrigin();
    }
    
 
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
