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
package my.serverA.ejb3.impl.test;

import static my.serverA.common.Constants.CAST_ID;
import static my.serverA.common.Constants.FILM_ID;
import static my.serverA.common.Constants.HARRISON_FORD;
import static my.serverA.common.Constants.IRELAND;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import my.database.entity.Cast;
import my.database.entity.Film;
import my.remote.bean.locator.Ejb3xBeanLocator;
import my.remote.serverB.ejb3.api.StatelessRemoteB;
import my.serverA.ejb3.impl.Ejb3StatelessA;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Ejb3StatelessATest {

	@Mock
	private Film mockFilm;
	
	@Mock
	private Cast mockCast;
	
	@Mock
	private Query mockQuery;
	
	@Mock
	private EntityManager mockEm;
	
	@Mock
	private StatelessRemoteB mockStatelessRemoteB;
	
	@Mock
	Ejb3xBeanLocator mockBeanLocator;
	
	@InjectMocks
	private Ejb3StatelessA ejb3StatelessA = new Ejb3StatelessA();

	@Before
	public void setUp() throws Exception {
	}


	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test_runTest() throws Exception {
		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockBeanLocator.locateBean(StatelessRemoteB.JNDI_LOOKUP)).thenReturn(mockStatelessRemoteB);
		when(mockFilm.getCast()).thenReturn(mockCast);
		when(mockCast.getLeadActor()).thenReturn(HARRISON_FORD);
		when(mockCast.getId()).thenReturn(CAST_ID);
		when(mockStatelessRemoteB.getCountryOfOriginAndCreateCast(FILM_ID)).thenReturn(IRELAND);	
		String testResult = ejb3StatelessA.runTest();	// execute test
		Assert.assertEquals("Expected a pass message!", "Passed", testResult);
		verify(mockStatelessRemoteB, times(1)).getCountryOfOriginAndCreateCast(FILM_ID);
		verify(mockBeanLocator).locateBean(StatelessRemoteB.JNDI_LOOKUP);
		verify(mockCast).getId();
		verify(mockCast).getLeadActor();
	}
	
	@Test (expected = NamingException.class)
	public void test_runTest_NamingException() throws Exception{
		when(mockEm.find(any(Class.class), any(Object.class))).thenReturn(mockFilm);
		when(mockFilm.getCountryOfOrigin()).thenReturn(IRELAND);
		when(mockBeanLocator.locateBean(StatelessRemoteB.JNDI_LOOKUP)).thenThrow(new NamingException());
		when(mockStatelessRemoteB.getCountryOfOrigin(1l)).thenReturn(IRELAND);
		ejb3StatelessA.runTest();
	}
	

}
