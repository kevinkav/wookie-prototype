package my.prototype.ejb;

import static my.prototype.common.Constants.IRELAND;
import static my.prototype.common.Constants.FILM_ID;
import static my.remote.common.Constants.SERVER_A;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.prototype.test.api.TestCase;
import my.remote.v3.api.Ejb1StatefulRemote;
import my.remote.v3.api.Ejb2Remote;
import my.remote.v3.bean.locator.BeanLocator;

@Local(TestCase.class)
@Remote(Ejb1StatefulRemote.class)
@Stateful
@EJB(name=Ejb1StatefulRemote.EJB1_STATEFUL_JNDI_LOOKUP, beanInterface=Ejb1StatefulRemote.class)
public class Ejb1_V3_Stateful extends Ejb1Base implements Ejb1StatefulRemote {

    private static final Logger LOG = LoggerFactory.getLogger(Ejb1_V3_Stateful.class);
    
    @Inject
    BeanLocator beanLocator;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public String runTest() throws Exception {
    	LOG.info("[{}] running test", SERVER_A);
    	String testResult = "Failed";
        try {
            String localValue = setCountryOfOrigin(IRELAND);
            Ejb2Remote ejb2 = (Ejb2Remote) beanLocator.locateBean(Ejb2Remote.EJB2_JNDI_LOOKUP);
            //String remoteValue = ejb2.getCountryOfOriginAndCreateCast(FILM_ID);
            String remoteValue = ejb2.getCountryOfOrigin(FILM_ID);
            ejb2.createCast(FILM_ID);
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

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
        LOG.info("[{}] created", SERVER_A);
    }

 
}
