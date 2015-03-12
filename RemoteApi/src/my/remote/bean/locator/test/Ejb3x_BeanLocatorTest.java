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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.naming.Context;
import javax.naming.NamingException;

import my.remote.bean.locator.Ejb3xBeanLocator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Ejb3x_BeanLocatorTest {

	@Mock
	private Context mockContext;
	
	@InjectMocks
	private Ejb3xBeanLocator ejb3BeanLocator = new Ejb3xBeanLocator();
	
	private static final String EJB3_ADDRESS = "ejb3_address";


	@Test
	public void testLocateBean() throws NamingException {
		when(mockContext.lookup(EJB3_ADDRESS)).thenReturn(new Object());
		Object result = ejb3BeanLocator.locateBean(EJB3_ADDRESS);
		Assert.assertNotNull(result);
		verify(mockContext).lookup(EJB3_ADDRESS);
	}
	
	@Test (expected = NamingException.class)
	public void testLocateBean_NamingException() throws Exception {
		when(mockContext.lookup(EJB3_ADDRESS)).thenThrow(new NamingException("naming error"));
		try{
			ejb3BeanLocator.locateBean(EJB3_ADDRESS);
		}catch(Exception ex){
			verify(mockContext).lookup(EJB3_ADDRESS);
			throw ex;
		}
	}

}
