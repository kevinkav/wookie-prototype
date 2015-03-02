package my.prototype.ejb.test;

import static my.prototype.common.Constants.CAST_ID;
import static my.prototype.common.Constants.FILM_ID;
import static my.prototype.common.Constants.HARRISON_FORD;
import static my.prototype.common.Constants.IRELAND;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import my.prototype.ejb.CorbaUtil;
import my.prototype.ejb.Ejb1_V2;
import my.prototype.entity.Cast;
import my.prototype.entity.Film;
import my.remote.v2.home.api.Ejb2RemoteObject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Ejb1_V2Test {

	private static final String EJB2_ADDRESS = 
			"corbaname:iiop:localhost:3628#" + Ejb2RemoteObject.EJB2_BINDING_JNDI;

	@Mock
	private Film mockFilm;
	@Mock
	private Cast mockCast;
	@Mock
	private Query mockQuery;
	@Mock
	private EntityManager mockEm;
	@Mock
	private Ejb2RemoteObject mockEjb2;
	@Mock
	private CorbaUtil mockCorbaUtil;
	@InjectMocks
	private Ejb1_V2 ejb_v2 = new Ejb1_V2();

	private Film expectedFilm;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//Whitebox.setInternalState(ejb_v2, "em", mockEm);
		//Whitebox.setInternalState(ejb_v2, "corbaUtil", mockCorbaUtil);
		//compareFilm = createFilm();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void test_setUp() throws Exception {
		String actualResult = ejb_v2.setUp();
		verify(mockEm).persist(any(Film.class));
		Assert.assertTrue("Expected a film created message!", actualResult.contains("created"));
	}

	@Test
	public void test_runTest() throws Exception{
		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockCorbaUtil.getEjb2RemoteObject(EJB2_ADDRESS)).thenReturn(mockEjb2);
		when(mockEjb2.getCountryOfOrigin(1l)).thenReturn(IRELAND);
		when(mockFilm.getCast()).thenReturn(mockCast);
		when(mockCast.getLeadActor()).thenReturn(HARRISON_FORD);
		when(mockCast.getId()).thenReturn(CAST_ID);

		String testResult = ejb_v2.runTest();

		Assert.assertEquals("Expected a pass message!", "Passed", testResult);
		verify(mockEjb2).getCountryOfOrigin(FILM_ID);
		verify(mockEjb2, times(1)).createCast(FILM_ID);
		verify(mockFilm, times(2)).getCountryOfOrigin();
		verify(mockCorbaUtil).getEjb2RemoteObject(EJB2_ADDRESS);
		verify(mockCast).getId();
		verify(mockCast).getLeadActor();

	}

	@Test (expected = NamingException.class)
	public void test_runTest_NamingException() throws Exception{

		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockCorbaUtil.getEjb2RemoteObject(EJB2_ADDRESS)).thenThrow(new NamingException());
		when(mockEjb2.getCountryOfOrigin(1l)).thenReturn(IRELAND);

		ejb_v2.runTest();
	}
	
	@Test (expected = CreateException.class)
	public void test_runTest_CreateException() throws Exception{

		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockCorbaUtil.getEjb2RemoteObject(EJB2_ADDRESS)).thenThrow(new CreateException());
		when(mockEjb2.getCountryOfOrigin(1l)).thenReturn(IRELAND);

		ejb_v2.runTest();
	}
	
	@Test (expected = RemoteException.class)
	public void test_runTest_RemoteException() throws Exception{

		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockCorbaUtil.getEjb2RemoteObject(EJB2_ADDRESS)).thenThrow(new RemoteException());
		when(mockEjb2.getCountryOfOrigin(1l)).thenReturn(IRELAND);

		ejb_v2.runTest();
	}

	@Test
	public void test_getCountryOfOrigin(){
		
		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		
		String countryOfOrigin = ejb_v2.getCountryOfOrigin(FILM_ID);

		Assert.assertEquals("Expected to get " + IRELAND,  IRELAND, countryOfOrigin);
		verify(mockEm).find(my.prototype.entity.Film.class, 1l);
		verify(mockFilm).getCountryOfOrigin();
	}
	
	@Test
	public void test_AddCastToFilm(){
		
		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		
		ejb_v2.addCastToFilm();
		
		verify(mockEm).find(my.prototype.entity.Film.class, 1l);
		verify(mockFilm).setCast(any(Cast.class));
		verify(mockEm, times(2)).persist(any(Object.class));
	}
	
	
	@Test
	public void test_tearDown(){
		when(mockEm.createQuery(any(String.class))).thenReturn(mockQuery);
		List<Cast> castList = new ArrayList<>();
		castList.add(mockCast);
		List<Film> filmList = new ArrayList<>();
		filmList.add(mockFilm);
		when(mockQuery.getResultList()).thenReturn(castList).thenReturn(filmList); // stubbing consecutive calls
		when(mockFilm.getId()).thenReturn(FILM_ID);
		when(mockCast.getId()).thenReturn(CAST_ID);
		String message = ejb_v2.tearDown();

		verify(mockEm, times(2)).remove(any(Long.class));
		Assert.assertTrue("Expected deleted message!", message.contains("Deleted"));
	}
	
	
	private Film createFilm() {
		Film film = new Film();
		film.setName("StarWars");
		film.setId(1L);
		film.setCountryOfOrigin("USA");
		film.setDirector("George Lucas");
		film.setRunningTimeMins(122);
		film.setYearOfRelease(1977);
		return film;
	}


}
