package my.serverB.ejb3.impl;

import static my.remote.common.RemoteConstants.SERVER_A;
import static my.remote.common.RemoteConstants.SERVER_B;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.remote.bean.locator.Ejb3xBeanLocator;
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
public class Ejb3x_StatefulB implements StatefulRemoteB{

	@Inject
    private Ejb3xBeanLocator ejb3xBeanLocator;
		
    private static final Logger LOG = LoggerFactory.getLogger(Ejb3x_StatefulB.class);


    @TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public String getCountryOfOrigin(long id) throws Exception {
    	LOG.info("[{}] getCountryOfOrigin invoked with id [{}]: ", SERVER_B, id);
		String countryOfOriginFromA = "";
        try {
        	StatefulRemoteA statefulRemoteA = (StatefulRemoteA) ejb3xBeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP);
            countryOfOriginFromA = statefulRemoteA.getCountryOfOrigin(id);
            LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginFromA, SERVER_A);
            statefulRemoteA.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("[{}] [{}],  exception msg [{}]", SERVER_B, e.getClass().getCanonicalName(), e.getMessage());
            throw e;
        }
        return countryOfOriginFromA;
	}

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public void createCast(long id) throws Exception {
    	LOG.info("[{}] createCast invoked with id [{}]", SERVER_B, id);
        try {
        	StatefulRemoteA statefulRemoteA = (StatefulRemoteA) ejb3xBeanLocator.locateBean(StatefulRemoteA.JNDI_LOOKUP);
        	statefulRemoteA.addCastToFilm();
        } catch (Exception e) {
        	LOG.error("[{}] [{}],  exception msg [{}]", SERVER_B, e.getClass().getCanonicalName(), e.getMessage());
            throw e;
        }
	}

}
