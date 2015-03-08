package my.serverA.ejb2.impl;

import static my.remote.common.Constants.SERVER_A;
import static my.serverA.common.Constants.FILM_ID;
import static my.serverA.common.Constants.IRELAND;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;

import my.remote.serverA.ejb2.api.RemoteHomeA;
import my.remote.serverA.ejb2.api.RemoteObjectA;
import my.remote.serverB.ejb2.api.RemoteObjectB;
import my.serverA.common.CorbaUtil;
import my.serverA.common.EjbBaseA;
import my.test.api.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Session Bean implementation class Ejb1
 */
@Stateless
@Local(TestCase.class)
@RemoteHome(RemoteHomeA.class)
@EJB(name = RemoteObjectA.EJB1_BINDING_JNDI, beanInterface = RemoteObjectA.class)
public class Ejb2StatelessA extends EjbBaseA {

    private static final String EJB2_ADDRESS = "corbaname:iiop:localhost:3628#" + RemoteObjectB.EJB2_BINDING_JNDI;
    private static final Logger LOG = LoggerFactory.getLogger(Ejb2StatelessA.class);    
    private CorbaUtil corbaUtil = null;
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String runTest() throws Exception {
    	LOG.info("[{}] running test", SERVER_A);
    	String testResult = "failed";
        try{
            String localValue = setCountryOfOrigin(IRELAND);
            createCorbaUtil();
            RemoteObjectB ejb2 = corbaUtil.getEjb2RemoteObject(EJB2_ADDRESS);
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


