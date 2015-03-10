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
package my.remote.bean.locator.test;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import my.remote.bean.locator.Ejb2BeanLocator;
import my.remote.bean.locator.PortableObjectHelper;
import my.remote.serverA.ejb2.api.RemoteHomeA;
import my.remote.serverA.ejb2.api.RemoteObjectA;
import my.remote.serverB.ejb2.api.RemoteHomeB;
import my.remote.serverB.ejb2.api.RemoteObjectB;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Ejb2BeanLocatorTest {

	@Mock
	private InitialContext mockInitialContext;
	
	@Mock
	private Object mockObject;
	
	@Mock
	private RemoteHomeA mockRemoteHomeA;
	
	@Mock
	private RemoteHomeB mockRemoteHomeB;
	
	@Mock
	private RemoteObjectA mockRemoteObjectA;
	
	@Mock
	private RemoteObjectB mockRemoteObjectB;
	
	@Mock
	private PortableObjectHelper mockPortableObjectHelper;

	private Ejb2BeanLocator ejb2BeanLocator;
	
	private static final String EJB_ADDRESS_A = "ejb_Address_A";
	
	private static final String EJB_ADDRESS_B = "ejb_Address_B";

	@Before
	public void setUp() throws Exception {
		ejb2BeanLocator = new Ejb2BeanLocator();
		Whitebox.setInternalState(ejb2BeanLocator, "ctx", mockInitialContext);
		Whitebox.setInternalState(ejb2BeanLocator, "portableObjectHelper", mockPortableObjectHelper);
	}

	
	@After
	public void tearDown() throws Exception {
		ejb2BeanLocator = null;
	}

	@Test
	public void testConstructor() throws NamingException {
		new Ejb2BeanLocator();
	}
	
	@Test 
	public void testGetRemoteObjectA() throws RemoteException, NamingException, CreateException{
		when(mockInitialContext.lookup(EJB_ADDRESS_A)).thenReturn(mockObject);
		when(mockPortableObjectHelper.narrow(mockObject, RemoteHomeA.class)).thenReturn(mockRemoteHomeA);
		when(mockRemoteHomeA.create()).thenReturn(mockRemoteObjectA);
		RemoteObjectA result = ejb2BeanLocator.getRemoteObjectA(EJB_ADDRESS_A);
		Assert.assertNotNull(result);
		Assert.assertTrue("Should be RemoteObjectA object", result instanceof RemoteObjectA);
		verify(mockInitialContext).lookup(EJB_ADDRESS_A);
		verify(mockPortableObjectHelper).narrow(mockObject, RemoteHomeA.class);
		verify(mockRemoteHomeA).create();
	}
	
	@Test 
	public void testGetRemoteObjectB() throws RemoteException, NamingException, CreateException{
		when(mockInitialContext.lookup(EJB_ADDRESS_B)).thenReturn(mockObject);
		when(mockPortableObjectHelper.narrow(mockObject, RemoteHomeB.class)).thenReturn(mockRemoteHomeB);
		when(mockRemoteHomeB.create()).thenReturn(mockRemoteObjectB);
		RemoteObjectB result = ejb2BeanLocator.getRemoteObjectB(EJB_ADDRESS_B);
		Assert.assertNotNull(result);
		Assert.assertTrue("Should be RemoteObjectA object", result instanceof RemoteObjectB);
		verify(mockInitialContext).lookup(EJB_ADDRESS_B);
		verify(mockPortableObjectHelper).narrow(mockObject, RemoteHomeB.class);
		verify(mockRemoteHomeB).create();
	}

}
