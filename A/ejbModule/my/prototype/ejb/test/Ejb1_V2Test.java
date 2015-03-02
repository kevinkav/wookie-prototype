package my.prototype.ejb.test;

import static my.prototype.common.Constants.IRELAND;
import static my.prototype.common.Constants.USA;
import static my.prototype.common.Constants.HARRISON_FORD;
import static my.prototype.common.Constants.CAST_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

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
		Assert.assertTrue("Expected film created message", actualResult.contains("created"));
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
		
		Assert.assertEquals("Should get passed message!", "Passed", testResult);
		verify(mockEjb2).getCountryOfOrigin(1l);
		verify(mockEjb2, times(1)).createCast(1l);;
		
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
