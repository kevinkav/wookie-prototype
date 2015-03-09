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
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.remote.bean.locator.Ejb3BeanLocator;
import my.remote.serverA.ejb3.api.StatefulRemoteA;
import my.remote.serverB.ejb3.api.StatefulRemoteB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stateful bean
 */

@Remote(StatefulRemoteB.class)
@Stateful
@EJB(name=StatefulRemoteB.JNDI_LOOKUP, beanInterface=StatefulRemoteB.class)
public class Ejb3StatefulB implements StatefulRemoteB{

	@Inject
    private Ejb3BeanLocator beanLocator;
		
    private static final Logger LOG = LoggerFactory.getLogger(Ejb3StatefulB.class);


    @TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public String getCountryOfOriginAndCreateCast(long id) throws Exception {
    	LOG.info("Called with id: " + id);
        String countryOfOriginFromA = "";
        try {
        	StatefulRemoteA statefulRemoteA = (StatefulRemoteA) beanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP);
            countryOfOriginFromA = statefulRemoteA.getCountryOfOrigin(id);
            LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginFromA, SERVER_A);
            statefulRemoteA.addCastToFilm();
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
		String countryOfOriginFromA = "";
        try {
        	StatefulRemoteA statefulRemoteA = (StatefulRemoteA) beanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP);
            countryOfOriginFromA = statefulRemoteA.getCountryOfOrigin(id);
            LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginFromA, SERVER_A);
            statefulRemoteA.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("Exception occurred rolling back transaction, error message [{}]", e.getMessage());
            throw e;
        }
        return countryOfOriginFromA;
	}

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public void createCast(long id) throws Exception {
    	LOG.info("[{}] createCast invoked with id [{}]", SERVER_B, id);
        try {
        	StatefulRemoteA statefulRemoteA = (StatefulRemoteA) beanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP);
        	statefulRemoteA.addCastToFilm();
        } catch (Exception e) {
            LOG.error("Exception occurred rolling back transaction, error message [{}]", e.getMessage());
            throw e;
        }
	}

}
