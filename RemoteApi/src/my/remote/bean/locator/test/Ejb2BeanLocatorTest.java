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

import my.remote.bean.locator.Ejb2xBeanLocator;
import my.remote.bean.locator.PortableObjectHelper;
import my.remote.serverA.ejb2.api.StatelessRemoteHomeA;
import my.remote.serverA.ejb2.api.StatelessRemoteObjectA;
import my.remote.serverB.ejb2.api.StatelessRemoteHomeB;
import my.remote.serverB.ejb2.api.StatelessRemoteObjectB;

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
	private StatelessRemoteHomeA mockRemoteHomeA;
	
	@Mock
	private StatelessRemoteHomeB mockRemoteHomeB;
	
	@Mock
	private StatelessRemoteObjectA mockRemoteObjectA;
	
	@Mock
	private StatelessRemoteObjectB mockRemoteObjectB;
	
	@Mock
	private PortableObjectHelper mockPortableObjectHelper;

	private Ejb2xBeanLocator ejb2BeanLocator;
	
	private static final String EJB_ADDRESS_A = "ejb_Address_A";
	
	private static final String EJB_ADDRESS_B = "ejb_Address_B";

	@Before
	public void setUp() throws Exception {
		ejb2BeanLocator = new Ejb2xBeanLocator();
		Whitebox.setInternalState(ejb2BeanLocator, "ctx", mockInitialContext);
		Whitebox.setInternalState(ejb2BeanLocator, "portableObjectHelper", mockPortableObjectHelper);
	}

	
	@After
	public void tearDown() throws Exception {
		ejb2BeanLocator = null;
	}

	@Test
	public void testConstructor() throws NamingException {
		new Ejb2xBeanLocator();
	}
	
	@Test 
	public void testGetRemoteObjectA() throws RemoteException, NamingException, CreateException{
		when(mockInitialContext.lookup(EJB_ADDRESS_A)).thenReturn(mockObject);
		when(mockPortableObjectHelper.narrow(mockObject, StatelessRemoteHomeA.class)).thenReturn(mockRemoteHomeA);
		when(mockRemoteHomeA.create()).thenReturn(mockRemoteObjectA);
		StatelessRemoteObjectA result = ejb2BeanLocator.getStatelessRemoteObjectA(EJB_ADDRESS_A);
		Assert.assertNotNull(result);
		Assert.assertTrue("Should be RemoteObjectA object", result instanceof StatelessRemoteObjectA);
		verify(mockInitialContext).lookup(EJB_ADDRESS_A);
		verify(mockPortableObjectHelper).narrow(mockObject, StatelessRemoteHomeA.class);
		verify(mockRemoteHomeA).create();
	}
	
	@Test 
	public void testGetRemoteObjectB() throws RemoteException, NamingException, CreateException{
		when(mockInitialContext.lookup(EJB_ADDRESS_B)).thenReturn(mockObject);
		when(mockPortableObjectHelper.narrow(mockObject, StatelessRemoteHomeB.class)).thenReturn(mockRemoteHomeB);
		when(mockRemoteHomeB.create()).thenReturn(mockRemoteObjectB);
		StatelessRemoteObjectB result = ejb2BeanLocator.getStatelessRemoteObjectB(EJB_ADDRESS_B);
		Assert.assertNotNull(result);
		Assert.assertTrue("Should be RemoteObjectA object", result instanceof StatelessRemoteObjectB);
		verify(mockInitialContext).lookup(EJB_ADDRESS_B);
		verify(mockPortableObjectHelper).narrow(mockObject, StatelessRemoteHomeB.class);
		verify(mockRemoteHomeB).create();
	}

}
