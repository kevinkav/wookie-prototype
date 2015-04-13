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
package my.serverB.ejb3.impl;

import static my.remote.common.RemoteConstants.SERVER_A;
import static my.remote.common.RemoteConstants.SERVER_B;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.remote.bean.locator.Ejb3xBeanLocator;
import my.remote.serverA.ejb3.api.StatelessRemoteA;
import my.remote.serverB.ejb3.api.StatelessRemoteB;

/**
 * Stateless bean
 */

@Remote(StatelessRemoteB.class)
@Stateless
@EJB(name=StatelessRemoteB.JNDI_LOOKUP, beanInterface=StatelessRemoteB.class)
public class Ejb3x_StatelessB implements StatelessRemoteB{

	@Inject
    private Ejb3xBeanLocator ejb3xBeanLocator;
	    
    private static final Logger LOG = LoggerFactory.getLogger(Ejb3x_StatelessB.class);
    

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public String getCountryOfOrigin(long id) throws Exception {
		LOG.info("[{}] getCountryOfOrigin invoked with id [{}]: ", SERVER_B, id);
		String countryOfOriginEjb1 = "";
        try {
        	StatelessRemoteA ejb3StatelessRemoteA = (StatelessRemoteA) ejb3xBeanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP);
            countryOfOriginEjb1 = ejb3StatelessRemoteA.getCountryOfOrigin(id);
            LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginEjb1, SERVER_A);
            ejb3StatelessRemoteA.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("[{}] occurred so rolling back transaction - exception msg [{}]", 
        			e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
        return countryOfOriginEjb1;
	}

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public void createCast(long id) throws Exception {
    	LOG.info("[{}] createCast invoked with id [{}]", SERVER_B, id);
        try {
        	StatelessRemoteA ejb3StatelessRemoteA = (StatelessRemoteA) ejb3xBeanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP);
        	ejb3StatelessRemoteA.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("[{}] occurred so rolling back transaction - exception msg [{}]", 
        			e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
	}

}
