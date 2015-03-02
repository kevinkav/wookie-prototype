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
package my.prototype.ejb.test;

import static my.prototype.common.Constants.CAST_ID;
import static my.prototype.common.Constants.HARRISON_FORD;
import static my.prototype.common.Constants.IRELAND;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import my.prototype.ejb.Ejb1_V2;
import my.prototype.ejb.Ejb1_V3_Stateful;
import my.prototype.entity.Cast;
import my.prototype.entity.Film;
import my.remote.v2.home.api.Ejb2RemoteObject;
import my.remote.v3.api.Ejb2Remote;
import my.remote.v3.bean.locator.BeanLocator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class Ejb1_V3_StatefulTest {

	@Mock
	private Film mockFilm;
	
	@Mock
	private Cast mockCast;
	
	@Mock
	private Query mockQuery;
	
	@Mock
	private EntityManager mockEm;
	
	@Mock
	private Ejb2Remote mockEjb2;
	
	@Mock
	BeanLocator mockBeanLocator;
	
	@InjectMocks
	private Ejb1_V3_Stateful ejb_v3 = new Ejb1_V3_Stateful();

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
		when(mockBeanLocator.locateBean(Ejb2Remote.EJB2_JNDI_LOOKUP)).thenReturn(mockEjb2);
		when(mockFilm.getCast()).thenReturn(mockCast);
		when(mockCast.getLeadActor()).thenReturn(HARRISON_FORD);
		when(mockCast.getId()).thenReturn(CAST_ID);
		
		ejb_v3.runTest();
	}

}
