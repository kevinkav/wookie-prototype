package my.serverA.ejb3.impl;

import static my.remote.common.RemoteConstants.SERVER_A;
import static my.serverA.common.Constants.FILM_ID;
import static my.serverA.common.Constants.IRELAND;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.remote.bean.locator.Ejb3xBeanLocator;
import my.remote.serverA.ejb3.api.StatefulRemoteA;
import my.remote.serverB.ejb3.api.StatefulRemoteB;
import my.serverA.common.EjbBaseA;
import my.test.api.TestCaseLocal;
import my.test.api.TestCaseRemote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Local(TestCaseLocal.class)
@Remote({StatefulRemoteA.class, TestCaseRemote.class})
@Stateful
@EJB(name=StatefulRemoteA.JNDI_LOOKUP, beanInterface=StatefulRemoteA.class)
public class Ejb3x_StatefulA extends EjbBaseA implements StatefulRemoteA {

    private static final Logger LOG = LoggerFactory.getLogger(Ejb3x_StatefulA.class);
    
    @Inject
    Ejb3xBeanLocator ejb3xBeanLocator;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public String runTest(int portOffsetServerA, int portOffsetServerB) throws Exception {
    	LOG.info("[{}] running test", SERVER_A);
    	String testResult = "Failed";
        try {
            String localValue = setCountryOfOrigin(IRELAND);
            StatefulRemoteB ejb3StatefulRemoteB = (StatefulRemoteB) ejb3xBeanLocator.locateBean(StatefulRemoteB.JNDI_LOOKUP);
            String remoteValue = ejb3StatefulRemoteB.getCountryOfOrigin(FILM_ID);
            ejb3StatefulRemoteB.createCast(FILM_ID);
            if (verifyCast() && verifyCountryOfOrigin(localValue, remoteValue)){
            	testResult = "Passed";
            }
        } catch (Exception e) {
        	LOG.error("[{}] occurred so rolling back transaction - exception msg [{}]", 
        			e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
        LOG.info("[{}] Commiting transaction", SERVER_A);
        return testResult;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String tearDown() {
        String str = super.tearDown();
        destroyBean();
        return str;
    }
    
    @Remove
    private void destroyBean(){
        LOG.info("[{}] destroying stateful bean", SERVER_A);
    }

    @PostConstruct
    private void startup(){
        LOG.info("[{}] instantiated", SERVER_A);
    }

 
}
