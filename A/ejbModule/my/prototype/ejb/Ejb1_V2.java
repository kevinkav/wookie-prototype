package my.prototype.ejb;

import static my.prototype.common.Constants.IRELAND;
import static my.prototype.common.Constants.FILM_ID;
import static my.remote.common.Constants.SERVER_A;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;

import my.prototype.test.api.TestCase;
import my.remote.v2.home.api.Ejb1RemoteHome;
import my.remote.v2.home.api.Ejb1RemoteObject;
import my.remote.v2.home.api.Ejb2RemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Session Bean implementation class Ejb1
 */
@Stateless
@Local(TestCase.class)
@RemoteHome(Ejb1RemoteHome.class)
@EJB(name = Ejb1RemoteObject.EJB1_BINDING_JNDI, beanInterface = Ejb1RemoteObject.class)
public class Ejb1_V2 extends Ejb1Base {

    private static final String EJB2_ADDRESS = "corbaname:iiop:localhost:3628#" + Ejb2RemoteObject.EJB2_BINDING_JNDI;
    private static final Logger LOG = LoggerFactory.getLogger(Ejb1_V2.class);    
    private CorbaUtil corbaUtil = null;
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String runTest() throws Exception {
    	LOG.info("[{}] running test", SERVER_A);
    	String testResult = "failed";
        try{
            String localValue = setCountryOfOrigin(IRELAND);
            createCorbaUtil();
            Ejb2RemoteObject ejb2 = corbaUtil.getEjb2RemoteObject(EJB2_ADDRESS);
            String remoteValue = ejb2.getCountryOfOrigin(FILM_ID);
            ejb2.createCast(FILM_ID); 
            if (verifyCast() && verifyCountryOfOrigin(localValue, remoteValue)){
            	testResult = "Passed";
            }
        }catch (Exception e){
            LOG.error("Exception occurred rolling back transaction - exception msg [{}]", e.getMessage());
            throw e;
        }
        LOG.info("[{}] commiting transaction", SERVER_A);
        return testResult;
    }


    /*
     * Getter method to aid junit testing.
     */
    private void createCorbaUtil() throws NamingException{
    	if (corbaUtil == null){
    		corbaUtil = new CorbaUtil();
    	}
    }


    @PostConstruct
    private void startup(){
    	LOG.info("[{}] created", SERVER_A);
    }

}


