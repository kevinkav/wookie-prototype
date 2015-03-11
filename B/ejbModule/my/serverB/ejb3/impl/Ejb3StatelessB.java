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

import static my.remote.common.Constants.SERVER_A;
import static my.remote.common.Constants.SERVER_B;

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
public class Ejb3StatelessB implements StatelessRemoteB{

	@Inject
    private Ejb3xBeanLocator beanLocator;
	    
    private static final Logger LOG = LoggerFactory.getLogger(Ejb3StatelessB.class);
    

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public String getCountryOfOriginAndCreateCast(long id) throws Exception {
        LOG.info("Called with id: " + id);
        String countryOfOriginFromA = "";
        try {
        	StatelessRemoteA ejb3StatelessRemoteA = (StatelessRemoteA) beanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP);
            countryOfOriginFromA = ejb3StatelessRemoteA.getCountryOfOrigin(id);
            LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginFromA, SERVER_A);
            ejb3StatelessRemoteA.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("Exception occurred rolling back transaction, error message [{}]", e.getMessage());
            throw e;
        }
        return countryOfOriginFromA;
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public String getCountryOfOrigin(long id) throws Exception {
		LOG.info("[{}] getCountryOfOrigin invoked with id [{}]: ", SERVER_B, id);
		String countryOfOriginEjb1 = "";
        try {
        	StatelessRemoteA ejb3StatelessRemoteA = (StatelessRemoteA) beanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP);
            countryOfOriginEjb1 = ejb3StatelessRemoteA.getCountryOfOrigin(id);
            LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginEjb1, SERVER_A);
            ejb3StatelessRemoteA.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("Exception occurred rolling back transaction, error message [{}]", e.getMessage());
            throw e;
        }
        return countryOfOriginEjb1;
	}

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public void createCast(long id) throws Exception {
    	LOG.info("[{}] createCast invoked with id [{}]", SERVER_B, id);
        try {
        	StatelessRemoteA ejb3StatelessRemoteA = (StatelessRemoteA) beanLocator.locateBean(StatelessRemoteA.JNDI_LOOKUP);
        	ejb3StatelessRemoteA.addCastToFilm();
        } catch (Exception e) {
            LOG.error("Exception occurred rolling back transaction, error message [{}]", e.getMessage());
            throw e;
        }
	}

}
