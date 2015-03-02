package my.prototype.ejb;

import static my.prototype.common.Constants.IRELAND;
import static my.prototype.common.Constants.FILM_ID;
import static my.remote.common.Constants.SERVER_A;

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

import my.prototype.test.api.TestCase;
import my.remote.v3.api.Ejb1Remote;
import my.remote.v3.api.Ejb2Remote;
import my.remote.v3.bean.locator.BeanLocator;


@Stateless
@Local(TestCase.class)
@Remote(Ejb1Remote.class)
@EJB(name=Ejb1Remote.EJB1_JNDI_LOOKUP, beanInterface=Ejb1Remote.class)
public class Ejb1_V3 extends Ejb1Base implements Ejb1Remote {

    private static final Logger LOG = LoggerFactory.getLogger(Ejb1_V3.class);
    
    @Inject
    BeanLocator beanLocator;
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public String runTest() throws Exception {
    	LOG.info("[{}] [{}] running test", SERVER_A);
    	String testResult = "Failed";
        try {
            String localValue = setCountryOfOrigin(IRELAND);
            Ejb2Remote ejb2 = (Ejb2Remote) beanLocator.locateBean(Ejb2Remote.EJB2_JNDI_LOOKUP);
            String remoteValue = ejb2.getCountryOfOriginAndCreateCast(FILM_ID);
            if (verifyCast() && verifyCountryOfOrigin(localValue, remoteValue)){
            	testResult = "Passed";
            }
        } catch (Exception e) {
            LOG.error("[{}] Exception occurred rolling back transaction, exception message - ", SERVER_A, e.getMessage());
            throw e;
        }
        LOG.info("[{}] commiting transaction", SERVER_A);
        return testResult;
    }
    
    @PostConstruct
    private void startup(){
        LOG.info("[{}] created", SERVER_A);
    }
}
