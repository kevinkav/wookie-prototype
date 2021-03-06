package my.serverB.ejb3.impl.test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import my.remote.bean.locator.Ejb3xBeanLocator;
import my.remote.serverA.ejb3.api.StatelessRemoteA;
import my.serverB.ejb3.impl.Ejb3x_StatelessB;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Ejb3x_StatelessBTest {

	private static final String IRELAND = "IRELAND";

	@Mock
	private StatelessRemoteA mockStatelessRemoteA;

	@Mock
	private Ejb3xBeanLocator mockEjb3BeanLocator;

	@InjectMocks
	private Ejb3x_StatelessB mockEjb3StatelessB = new Ejb3x_StatelessB();

	@Before
	public void setUp() throws Exception {
	}


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCountryOfOrigin() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP)).thenReturn(mockStatelessRemoteA);
		when(mockStatelessRemoteA.getCountryOfOrigin(1L)).thenReturn(IRELAND);
		String result = mockEjb3StatelessB.getCountryOfOrigin(1L);
		Assert.assertEquals("Expected to get value Ireland", IRELAND, result);
		verify(mockStatelessRemoteA).getCountryOfOrigin(1L);
		verify(mockEjb3BeanLocator).locateBean(StatelessRemoteA.JNDI_LOOKUP);
	}

	@Test (expected = NamingException.class)
	public void testGetCountryOfOrigin_Locator_NamingException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP)).thenThrow(new NamingException("Naming error"));
		try {
			mockEjb3StatelessB.getCountryOfOrigin(1L);
		}catch(Exception ex){
			verify(mockStatelessRemoteA, never()).getCountryOfOrigin(1L);
			verify(mockStatelessRemoteA, never()).addCastToFilm();
			verify(mockEjb3BeanLocator).locateBean(StatelessRemoteA.JNDI_LOOKUP);
			throw ex;
		}

	}

	@Test (expected = RemoteException.class)
	public void testGetCountryOfOrigin_StatelessRemoteA_RemoteException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP)).thenReturn(mockStatelessRemoteA);
		when(mockStatelessRemoteA.getCountryOfOrigin(1L)).thenThrow(new RemoteException("Remote error"));
		try{
			mockEjb3StatelessB.getCountryOfOrigin(1L);
		}catch(Exception ex){
			verify(mockStatelessRemoteA).getCountryOfOrigin(1L);
			verify(mockStatelessRemoteA, never()).addCastToFilm();
			verify(mockEjb3BeanLocator).locateBean(StatelessRemoteA.JNDI_LOOKUP);
			throw ex;
		}
	}

	@Test
	public void testCreateCast() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP)).thenReturn(mockStatelessRemoteA);
		mockEjb3StatelessB.createCast(1L);;
		verify(mockStatelessRemoteA,never()).getCountryOfOrigin(1L);
		verify(mockStatelessRemoteA).addCastToFilm();
		verify(mockEjb3BeanLocator).locateBean(StatelessRemoteA.JNDI_LOOKUP);
	}

	@Test (expected = NamingException.class)
	public void testCreateCast_Locator_NamingException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP)).thenThrow(new NamingException("Naming error"));
		try{
			mockEjb3StatelessB.createCast(1L);;
		}catch(Exception ex){
			verify(mockStatelessRemoteA,never()).getCountryOfOrigin(1L);
			verify(mockStatelessRemoteA, never()).addCastToFilm();
			verify(mockEjb3BeanLocator).locateBean(StatelessRemoteA.JNDI_LOOKUP);
			throw ex;
		}
	}

	@Test (expected = RemoteException.class)
	public void testCreateCast_StatelessRemoteA_RemoteException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP)).thenReturn(mockStatelessRemoteA);
		doThrow(new RemoteException("Remote error")).when(mockStatelessRemoteA).addCastToFilm();	// for stubbing void methods
		try{
			mockEjb3StatelessB.createCast(1L);;
		}catch(Exception ex){
			verify(mockStatelessRemoteA,never()).getCountryOfOrigin(1L);
			verify(mockStatelessRemoteA).addCastToFilm();
			verify(mockEjb3BeanLocator).locateBean(StatelessRemoteA.JNDI_LOOKUP);
			throw ex;
		}
	}

}
