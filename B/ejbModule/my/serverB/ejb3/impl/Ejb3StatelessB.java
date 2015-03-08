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

import my.remote.ejb3.bean.locator.BeanLocator;
import my.remote.serverA.ejb3.api.StatelessRemoteA;
import my.remote.serverB.ejb3.api.StatelessRemoteB;


@Remote(StatelessRemoteB.class)
@Stateless
@EJB(name=StatelessRemoteB.JNDI_LOOKUP, beanInterface=StatelessRemoteB.class)
public class Ejb3StatelessB implements StatelessRemoteB{

	@Inject
    private BeanLocator beanLocator;
	
    private StatelessRemoteA ejb1;  
    
    private static final Logger LOG = LoggerFactory.getLogger(Ejb3StatelessB.class);
    //private static final String BEAN1_LOOKUP_NAME = "ejb:Ear1/A/Ejb1_V3!my.remote.v3.api.Ejb1Remote";
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String getCountryOfOriginAndCreateCast(long id) throws Exception {
        LOG.info("Called with id: " + id);
        String countryOfOriginEjb1 = "";
        try {
            ejb1 = (StatelessRemoteA) beanLocator.locateBean(StatelessRemoteA.EJB1_JNDI_LOOKUP);
            countryOfOriginEjb1 = ejb1.getCountryOfOrigin(id);
            LOG.info("received CoutryOfOrigin value from ejb1: " + countryOfOriginEjb1);
            ejb1.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("Exception occurred rolling back transaction, error message [{}]", e.getMessage());
            throw e;
        }
        return countryOfOriginEjb1;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public String getCountryOfOrigin(long id) throws Exception {
		LOG.info("[{}] getCountryOfOrigin invoked with id [{}]: ", SERVER_B, id);
		String countryOfOriginEjb1 = "";
        try {
            ejb1 = (StatelessRemoteA) beanLocator.locateBean(StatelessRemoteA.EJB1_JNDI_LOOKUP);
            countryOfOriginEjb1 = ejb1.getCountryOfOrigin(id);
            LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginEjb1, SERVER_A);
            ejb1.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("Exception occurred rolling back transaction, error message [{}]", e.getMessage());
            throw e;
        }
        return countryOfOriginEjb1;
	}

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void createCast(long id) throws Exception {
    	LOG.info("[{}] createCast invoked with id [{}]", SERVER_B, id);
        try {
            ejb1 = (StatelessRemoteA) beanLocator.locateBean(StatelessRemoteA.EJB1_JNDI_LOOKUP);
            ejb1.addCastToFilm();
        } catch (Exception e) {
            LOG.error("Exception occurred rolling back transaction, error message [{}]", e.getMessage());
            throw e;
        }
	}

}
