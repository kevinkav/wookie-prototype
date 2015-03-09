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
package my.serverB.ejb3.impl.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import my.remote.bean.locator.Ejb3BeanLocator;
import my.remote.serverA.ejb3.api.StatefulRemoteA;
import my.serverB.ejb3.impl.Ejb3StatefulB;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Ejb3StatefulBTest {

	private static final String IRELAND = "IRELAND";
	
	@Mock
	private StatefulRemoteA mockStatefulRemoteA;
	
	@Mock
	private Ejb3BeanLocator mockEjb3BeanLocator;
	
	@InjectMocks
	private Ejb3StatefulB mockEjb3StatefulB = new Ejb3StatefulB();
	
	
	@Before
	public void setUp() throws Exception {
	}


	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetCountryOfOriginAndCreateCast() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP)).thenReturn(mockStatefulRemoteA);
		when(mockStatefulRemoteA.getCountryOfOrigin(1L)).thenReturn(IRELAND);
		String result = mockEjb3StatefulB.getCountryOfOriginAndCreateCast(1L);
		Assert.assertEquals("Expected to get value Ireland", IRELAND, result);
		verify(mockStatefulRemoteA).getCountryOfOrigin(1L);
		verify(mockStatefulRemoteA).addCastToFilm();
		verify(mockEjb3BeanLocator).locateBean(StatefulRemoteA.JNDI_LOOKUP);
	}

	@Test (expected = NamingException.class)
	public void testGetCountryOfOriginAndCreateCast_NamingException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP)).thenThrow(new NamingException("Naming error"));
		try{
			mockEjb3StatefulB.getCountryOfOriginAndCreateCast(1L);
		}catch(Exception ex){
			verify(mockStatefulRemoteA, never()).getCountryOfOrigin(1L);
			verify(mockStatefulRemoteA, never()).addCastToFilm();
			verify(mockEjb3BeanLocator).locateBean(StatefulRemoteA.JNDI_LOOKUP);
			throw ex;
		}
	}
	
	@Test (expected = RemoteException.class)
	public void testGetCountryOfOriginAndCreateCast_RemoteException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP)).thenReturn(mockStatefulRemoteA);
		when(mockStatefulRemoteA.getCountryOfOrigin(1L)).thenThrow(new RemoteException("Remote error"));
		try{
			mockEjb3StatefulB.getCountryOfOriginAndCreateCast(1L);
		}catch(Exception ex){
			verify(mockStatefulRemoteA).getCountryOfOrigin(1L);
			verify(mockStatefulRemoteA, never()).addCastToFilm();
			verify(mockEjb3BeanLocator).locateBean(StatefulRemoteA.JNDI_LOOKUP);
			throw ex;
		}
	}
	
	@Test
	public void testGetCountryOfOrigin() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP)).thenReturn(mockStatefulRemoteA);
		when(mockStatefulRemoteA.getCountryOfOrigin(1L)).thenReturn(IRELAND);
		String result = mockEjb3StatefulB.getCountryOfOrigin(1L);
		Assert.assertEquals("Expected to get value Ireland", IRELAND, result);
		verify(mockStatefulRemoteA).getCountryOfOrigin(1L);
		verify(mockEjb3BeanLocator).locateBean(StatefulRemoteA.JNDI_LOOKUP);
	}
	
	@Test (expected = NamingException.class)
	public void testGetCountryOfOrigin_NamingException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP)).thenThrow(new NamingException("Naming error"));
		try{
			mockEjb3StatefulB.getCountryOfOrigin(1L);	
		}catch (Exception ex){
			verify(mockEjb3BeanLocator).locateBean(StatefulRemoteA.JNDI_LOOKUP);
			verify(mockStatefulRemoteA, never()).getCountryOfOrigin(1L);
			throw ex;
		}
		
	}
	
	@Test (expected = RemoteException.class)
	public void testGetCountryOfOrigin_RemoteException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP)).thenReturn(mockStatefulRemoteA);
		when(mockStatefulRemoteA.getCountryOfOrigin(1L)).thenThrow(new RemoteException("Remote error"));
		try{
			mockEjb3StatefulB.getCountryOfOrigin(1L);	
		}catch (Exception ex){
			verify(mockEjb3BeanLocator).locateBean(StatefulRemoteA.JNDI_LOOKUP);
			verify(mockStatefulRemoteA).getCountryOfOrigin(1L);
			verify(mockStatefulRemoteA, never()).addCastToFilm();
			throw ex;
		}
	}
	
	@Test
	public void testCreateCast() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP)).thenReturn(mockStatefulRemoteA);
		mockEjb3StatefulB.createCast(1L);
		verify(mockStatefulRemoteA).addCastToFilm();
		verify(mockEjb3BeanLocator).locateBean(StatefulRemoteA.JNDI_LOOKUP);
	}
	
	@Test (expected = NamingException.class)
	public void testCreateCast_NamingException() throws Exception {
		when(mockEjb3BeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP)).thenThrow(new NamingException("Naming error"));
		try{
			mockEjb3StatefulB.createCast(1L);
			fail("Expected NamingException");
		}catch (Exception ex){
			verify(mockEjb3BeanLocator).locateBean(StatefulRemoteA.JNDI_LOOKUP);
			verify(mockStatefulRemoteA, never()).addCastToFilm();
			throw ex;
		}
		
	}

}
