package my.serverA.ejb2.impl.test;

import static my.serverA.common.Constants.CAST_ID;
import static my.serverA.common.Constants.FILM_ID;
import static my.serverA.common.Constants.HARRISON_FORD;
import static my.serverA.common.Constants.IRELAND;
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

import my.database.entity.Cast;
import my.database.entity.Film;
import my.remote.bean.locator.Ejb2xBeanLocator;
import my.remote.serverB.ejb2.api.StatelessRemoteObjectB;
import my.serverA.ejb2.impl.Ejb2x_StatelessA;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Ejb2x_StatelessATest {

	private static final String EJB2_ADDRESS = 
			"corbaname:iiop:localhost:3628#" + StatelessRemoteObjectB.IIOP_BINDING;

	@Mock
	private Film mockFilm;
	
	@Mock
	private Cast mockCast;
	
	@Mock
	private Query mockQuery;
	
	@Mock
	private EntityManager mockEm;
	
	@Mock
	private StatelessRemoteObjectB mockmockRemoteObjectB;
	
	@Mock
	private Ejb2xBeanLocator mockEjb2BeanLocator;
	
	@InjectMocks
	private Ejb2x_StatelessA ejb_2x = new Ejb2x_StatelessA();


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void test_setUp() throws Exception {
		String actualResult = ejb_2x.setUp();
		verify(mockEm).persist(any(Film.class));
		Assert.assertTrue("Expected a film created message!", actualResult.contains("created"));
	}

	@Test
	public void test_runTest() throws Exception{
		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockEjb2BeanLocator.getStatelessRemoteObjectB(EJB2_ADDRESS)).thenReturn(mockmockRemoteObjectB);
		when(mockmockRemoteObjectB.getCountryOfOrigin(1l, 0)).thenReturn(IRELAND);
		when(mockmockRemoteObjectB.getCountryOfOrigin(1l, 0)).thenReturn(IRELAND);
		when(mockFilm.getCast()).thenReturn(mockCast);
		when(mockCast.getLeadActor()).thenReturn(HARRISON_FORD);
		when(mockCast.getId()).thenReturn(CAST_ID);

		String testResult = ejb_2x.runTest(0, 100);

		Assert.assertEquals("Expected a pass message!", "Passed", testResult);
		//verify(mockmockRemoteObjectB, times(1)).getCountryOfOriginAndCreateCast(FILM_ID);
		verify(mockmockRemoteObjectB, times(1)).getCountryOfOrigin(FILM_ID, 0);
		verify(mockmockRemoteObjectB, times(1)).createCast(FILM_ID, 0);
		verify(mockEjb2BeanLocator).getStatelessRemoteObjectB(EJB2_ADDRESS);
		verify(mockCast).getId();
		verify(mockCast).getLeadActor();

	}

	@Test (expected = NamingException.class)
	public void test_runTest_NamingException() throws Exception{

		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockEjb2BeanLocator.getStatelessRemoteObjectB(EJB2_ADDRESS)).thenThrow(new NamingException());
		when(mockmockRemoteObjectB.getCountryOfOrigin(1l, 100)).thenReturn(IRELAND);

		ejb_2x.runTest(0, 100);
	}
	
	@Test (expected = CreateException.class)
	public void test_runTest_CreateException() throws Exception{

		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockEjb2BeanLocator.getStatelessRemoteObjectB(EJB2_ADDRESS)).thenThrow(new CreateException());
		when(mockmockRemoteObjectB.getCountryOfOrigin(1l, 100)).thenReturn(IRELAND);

		ejb_2x.runTest(0, 100);
	}
	
	@Test (expected = RemoteException.class)
	public void test_runTest_RemoteException() throws Exception{

		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockEjb2BeanLocator.getStatelessRemoteObjectB(EJB2_ADDRESS)).thenThrow(new RemoteException());
		when(mockmockRemoteObjectB.getCountryOfOrigin(1l, 100)).thenReturn(IRELAND);

		ejb_2x.runTest(0, 100);
	}

	@Test
	public void test_getCountryOfOrigin(){
		
		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		
		String countryOfOrigin = ejb_2x.getCountryOfOrigin(FILM_ID);

		Assert.assertEquals("Expected to get " + IRELAND,  IRELAND, countryOfOrigin);
		verify(mockEm).find(my.database.entity.Film.class, 1l);
		verify(mockFilm).getCountryOfOrigin();
	}
	
	@Test
	public void test_AddCastToFilm(){
		
		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		
		ejb_2x.addCastToFilm();
		
		verify(mockEm).find(my.database.entity.Film.class, 1l);
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
		String message = ejb_2x.tearDown();

		verify(mockEm, times(2)).remove(any(Long.class));
		Assert.assertTrue("Expected deleted message!", message.contains("Deleted"));
	}


}
