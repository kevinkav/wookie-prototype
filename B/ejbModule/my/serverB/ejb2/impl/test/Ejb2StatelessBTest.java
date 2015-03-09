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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import my.remote.bean.locator.Ejb2BeanLocator;
import my.remote.serverA.ejb2.api.RemoteObjectA;
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

	private static final String REMOTE_OBJECT_ADDRESS = "corbaname:iiop:localhost:3528#" + RemoteObjectA.IIOP_BINDING;
	
	private static final String IRELAND = "Ireland";
	
	@Mock
	private RemoteObjectA mockRemoteObjectA;
	
	@Mock
	private Ejb2BeanLocator mockEjb2BeanLocator;
	
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
		when(mockEjb2BeanLocator.getRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenReturn(mockRemoteObjectA);
		when(mockRemoteObjectA.getCountryOfOrigin(1L)).thenReturn(IRELAND);
		String result = ejb2StatelessB.getCountryOfOriginAndCreateCast(1L);
		Assert.assertEquals("Expected to get value Ireland", IRELAND, result);
		verify(mockRemoteObjectA).getCountryOfOrigin(1L);
		verify(mockRemoteObjectA).addCastToFilm();
		verify(mockEjb2BeanLocator).getRemoteObjectA(REMOTE_OBJECT_ADDRESS);
	}
	

	@Test
	public void testgetCountryOfOrigin() throws Exception {
		when(mockEjb2BeanLocator.getRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenReturn(mockRemoteObjectA);
		when(mockRemoteObjectA.getCountryOfOrigin(1L)).thenReturn(IRELAND);
		String result = ejb2StatelessB.getCountryOfOrigin(1L);
		Assert.assertEquals("Expected to get value Ireland", IRELAND, result);
		verify(mockRemoteObjectA).getCountryOfOrigin(1L);
		verify(mockEjb2BeanLocator).getRemoteObjectA(REMOTE_OBJECT_ADDRESS);
	}
	
	@Test
	public void testCreateCast() throws Exception {
		when(mockEjb2BeanLocator.getRemoteObjectA(REMOTE_OBJECT_ADDRESS)).thenReturn(mockRemoteObjectA);
		ejb2StatelessB.createCast(1L);
		verify(mockEjb2BeanLocator).getRemoteObjectA(REMOTE_OBJECT_ADDRESS);
		verify(mockRemoteObjectA).addCastToFilm();
		verify(mockEjb2BeanLocator).getRemoteObjectA(REMOTE_OBJECT_ADDRESS);
	}

}
