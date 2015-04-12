package my.serverA.ejb3.impl;

import static my.remote.common.RemoteConstants.SERVER_A;
import static my.serverA.common.Constants.FILM_ID;
import static my.serverA.common.Constants.IRELAND;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
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
import my.serverA.common.EjbBaseA;
import my.test.api.TestCaseLocal;
import my.test.api.TestCaseRemote;


@Stateless
@Local(TestCaseLocal.class)
@Remote({StatelessRemoteA.class, TestCaseRemote.class})
@EJB(name=StatelessRemoteA.JNDI_LOOKUP, beanInterface=StatelessRemoteA.class)
public class Ejb3x_StatelessA extends EjbBaseA implements StatelessRemoteA {

    private static final Logger LOG = LoggerFactory.getLogger(Ejb3x_StatelessA.class);
    
    @Inject
    Ejb3xBeanLocator ejb3xBeanLocator;
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public String runTest(int portOffsetServerA, int portOffsetServerB) throws Exception {
    	LOG.info("[{}] running test", SERVER_A);
    	String testResult = "Failed";
        try {
            String localValue = setCountryOfOrigin(IRELAND);
            StatelessRemoteB ejb3StatelessRemoteB = (StatelessRemoteB) ejb3xBeanLocator.locateBean(StatelessRemoteB.JNDI_LOOKUP);
            String remoteValue = ejb3StatelessRemoteB.getCountryOfOrigin(FILM_ID);
            ejb3StatelessRemoteB.createCast(FILM_ID);
            if (verifyCast() && verifyCountryOfOrigin(localValue, remoteValue)){
            	testResult = "Passed";
            }
        } catch (Exception e) {
        	LOG.error("Exception occurred rolling back transaction - exception msg [{}]", e.getMessage());
            throw e;
        }
        LOG.info("[{}] Commiting transaction", SERVER_A);
        return testResult;
    }
    
    @PostConstruct
    private void startup(){
        LOG.info("[{}] created", SERVER_A);
    }
}
