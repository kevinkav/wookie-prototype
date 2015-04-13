package my.serverA.ejb2.impl;

import static my.remote.common.RemoteConstants.SERVER_A;
import static my.serverA.common.Constants.FILM_ID;
import static my.serverA.common.Constants.IRELAND;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Init;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;

import my.remote.bean.locator.Ejb2xBeanLocator;
import my.remote.common.RemoteConstants;
import my.remote.serverA.ejb2.api.StatefulRemoteHomeA;
import my.remote.serverA.ejb2.api.StatefulRemoteObjectA;
import my.remote.serverB.ejb2.api.StatefulRemoteObjectB;
import my.serverA.common.EjbBaseA;
import my.test.api.TestCaseLocal;
import my.test.api.TestCaseRemote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateful
@Local(TestCaseLocal.class)
@Remote(TestCaseRemote.class)
@RemoteHome(StatefulRemoteHomeA.class)
@EJB(name = StatefulRemoteObjectA.IIOP_BINDING, beanInterface = StatefulRemoteObjectA.class)
public class Ejb2x_StatefulA extends EjbBaseA{

    private static final Logger LOG = LoggerFactory.getLogger(Ejb2x_StatefulA.class);    
    private Ejb2xBeanLocator ejb2xBeanLocator = null;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String runTest(int portOffsetServerA, int portOffsetServerB) throws Exception {
		LOG.info("[{}] running test", SERVER_A);
    	String testResult = "Failed";
        try{
            String localValue = setCountryOfOrigin(IRELAND);
            createBeanFinder();
            String address = RemoteConstants.createCorbaEndpointAddress(portOffsetServerB, StatefulRemoteObjectB.IIOP_BINDING);
            StatefulRemoteObjectB ejb2StatelessB = ejb2xBeanLocator.getStatefulRemoteObjectB(address);
            String remoteValue = ejb2StatelessB.getCountryOfOrigin(FILM_ID, portOffsetServerA);
            ejb2StatelessB.createCast(FILM_ID, portOffsetServerA); 
            if (verifyCast() && verifyCountryOfOrigin(localValue, remoteValue)){
            	testResult = "Passed";
            }
        }catch (Exception e){
        	LOG.error("[{}] [{}],  exception msg [{}]", SERVER_A, e.getClass().getCanonicalName(), e.getMessage());
            throw e;
        }
        LOG.info("[{}] commiting transaction", SERVER_A);
        return testResult;
	}
	
  
	/*
     * Getter method to aid junit testing.
     */
    private void createBeanFinder() throws NamingException{
    	if (ejb2xBeanLocator == null){
    		ejb2xBeanLocator = new Ejb2xBeanLocator();
    	}
    }
    
    @PostConstruct
    private void startup(){
    	LOG.info("[{}] instantiated", SERVER_A);
    }
    
    @Init
    public void init(){	
    }

}
