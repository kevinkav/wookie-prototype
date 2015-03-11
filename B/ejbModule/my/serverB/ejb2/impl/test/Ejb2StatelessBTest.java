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
package my.serverB.ejb2.impl.test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import my.remote.bean.locator.Ejb2xBeanLocator;
import my.remote.serverA.ejb2.api.StatelessRemoteObjectA;
import my.serverB.ejb2.impl.Ejb2StatelessB;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Ejb2StatelessBTest {

	private static final String REMOTE_OBJECT_ADDRESS = "corbaname:iiop:localhost:3528#" + StatelessRemoteObjectA.IIOP_BINDING;
	
	private static final String IRELAND = "Ireland";
	
	@Mock
	private StatelessRemoteObjectA mockRemoteObjectA;
	
	@Mock
	private Ejb2xBeanLocator mockEjb2BeanLocator;
	
	@InjectMocks
	private Ejb2StatelessB ejb2StatelessB = new Ejb2StatelessB();
	
			

	@Before
	public void setUp() throws Exception {
	}


	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testGetCountryOfOriginAndCreateCast() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenReturn(mockRemoteObjectA);
		when(mockRemoteObjectA.getCountryOfOrigin(1L)).thenReturn(IRELAND);
		String result = ejb2StatelessB.getCountryOfOriginAndCreateCast(1L);
		Assert.assertEquals("Expected to get value Ireland", IRELAND, result);
		verify(mockRemoteObjectA).getCountryOfOrigin(1L);
		verify(mockRemoteObjectA).addCastToFilm();
		verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
	}
	
	@Test (expected = NamingException.class)
	public void testGetCountryOfOriginAndCreateCast_NamingException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new NamingException("naming error"));
		try{
			ejb2StatelessB.getCountryOfOriginAndCreateCast(1L);
		}catch (Exception ex){
			verify(mockRemoteObjectA, never()).getCountryOfOrigin(1L);
			verify(mockRemoteObjectA, never()).addCastToFilm();
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			throw ex;
		}
	}
	
	@Test (expected = CreateException.class)
	public void testGetCountryOfOriginAndCreateCast_CreateException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new CreateException("create error"));
		try{
			ejb2StatelessB.getCountryOfOriginAndCreateCast(1L);
		}catch (Exception ex){
			verify(mockRemoteObjectA, never()).getCountryOfOrigin(1L);
			verify(mockRemoteObjectA, never()).addCastToFilm();
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			throw ex;
		}
	}
	
	@Test (expected = RemoteException.class)
	public void testGetCountryOfOriginAndCreateCast_RemoteException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new RemoteException("remote error"));
		try{
			ejb2StatelessB.getCountryOfOriginAndCreateCast(1L);
		}catch (Exception ex){
			verify(mockRemoteObjectA, never()).getCountryOfOrigin(1L);
			verify(mockRemoteObjectA, never()).addCastToFilm();
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			throw ex;
		}
	}
	

	@Test
	public void testgetCountryOfOrigin() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenReturn(mockRemoteObjectA);
		when(mockRemoteObjectA.getCountryOfOrigin(1L)).thenReturn(IRELAND);
		String result = ejb2StatelessB.getCountryOfOrigin(1L);
		Assert.assertEquals("Expected to get value Ireland", IRELAND, result);
		verify(mockRemoteObjectA).getCountryOfOrigin(1L);
		verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
	}
	
	@Test (expected = NamingException.class)
	public void testgetCountryOfOrigin_NamingException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new NamingException("naming error"));
		try{
			ejb2StatelessB.getCountryOfOrigin(1L);
		}catch(Exception ex){
			verify(mockRemoteObjectA, never()).getCountryOfOrigin(1L);
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			throw ex;
		}
	}
	
	@Test (expected = RemoteException.class)
	public void testgetCountryOfOrigin_RemoteObjectA_RemoteException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenReturn(mockRemoteObjectA);
		doThrow(new RemoteException("remote error")).when(mockRemoteObjectA).getCountryOfOrigin(1L);
		try{
			ejb2StatelessB.getCountryOfOrigin(1L);
		}catch(Exception ex){
			verify(mockRemoteObjectA).getCountryOfOrigin(1L);
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			throw ex;
		}
	}
	
	@Test (expected = CreateException.class)
	public void testgetCountryOfOrigin_CreateException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new CreateException("create error"));
		try{
			ejb2StatelessB.getCountryOfOrigin(1L);
		}catch(Exception ex){
			verify(mockRemoteObjectA, never()).getCountryOfOrigin(1L);
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			throw ex;
		}
	}
	
	@Test (expected = RemoteException.class)
	public void testgetCountryOfOrigin_RemoteException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new RemoteException("remote error"));
		try{
			ejb2StatelessB.getCountryOfOrigin(1L);
		}catch(Exception ex){
			verify(mockRemoteObjectA, never()).getCountryOfOrigin(1L);
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			throw ex;
		}
	}
	
	@Test
	public void testCreateCast() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenReturn(mockRemoteObjectA);
		ejb2StatelessB.createCast(1L);
		verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
		verify(mockRemoteObjectA).addCastToFilm();
	}
	
	@Test (expected = NamingException.class)
	public void testCreateCast_Locator_NamingException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new NamingException("naming error"));
		try{
			ejb2StatelessB.createCast(1L);
		}catch (Exception ex){
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			verify(mockRemoteObjectA, never()).addCastToFilm();
			throw ex;
		}
	}
	
	@Test (expected = CreateException.class)
	public void testCreateCast_Locator_CreateException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new CreateException("naming error"));
		try{
			ejb2StatelessB.createCast(1L);
		}catch (Exception ex){
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			verify(mockRemoteObjectA, never()).addCastToFilm();
			throw ex;
		}
	}
	
	@Test (expected = RemoteException.class)
	public void testCreateCast_Locator_RemoteException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenThrow(new RemoteException("naming error"));
		try{
			ejb2StatelessB.createCast(1L);
		}catch (Exception ex){
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			verify(mockRemoteObjectA, never()).addCastToFilm();
			throw ex;
		}
	}
	
	@Test (expected = RemoteException.class)
	public void testCreateCast_remoteObjectA_RemoteException() throws Exception {
		when(mockEjb2BeanLocator.getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenReturn(mockRemoteObjectA);
		doThrow(new RemoteException("remote error")).when(mockRemoteObjectA).addCastToFilm();
		try{
			ejb2StatelessB.createCast(1L);
		}catch (Exception ex){
			verify(mockEjb2BeanLocator).getStatelessRemoteObjectA(REMOTE_OBJECT_ADDRESS);
			verify(mockRemoteObjectA).addCastToFilm();
			throw ex;
		}
	}

}
