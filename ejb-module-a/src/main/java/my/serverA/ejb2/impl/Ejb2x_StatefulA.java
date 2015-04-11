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
package my.serverA.ejb2.impl;

import static my.remote.common.Constants.SERVER_A;
import static my.serverA.common.Constants.FILM_ID;
import static my.serverA.common.Constants.IRELAND;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Init;
import javax.ejb.Local;
import javax.ejb.RemoteHome;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;

import my.remote.bean.locator.Ejb2xBeanLocator;
import my.remote.serverA.ejb2.api.StatefulRemoteHomeA;
import my.remote.serverA.ejb2.api.StatefulRemoteObjectA;
import my.remote.serverB.ejb2.api.StatefulRemoteObjectB;
import my.serverA.common.EjbBaseA;
import my.test.api.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateful
@Local(TestCase.class)
@RemoteHome(StatefulRemoteHomeA.class)
@EJB(name = StatefulRemoteObjectA.IIOP_BINDING, beanInterface = StatefulRemoteObjectA.class)
public class Ejb2x_StatefulA extends EjbBaseA{

    private static final String EJB2X_STATEFUL_ADDRESS = "corbaname:iiop:localhost:3628#" + StatefulRemoteObjectB.IIOP_BINDING;
    private static final Logger LOG = LoggerFactory.getLogger(Ejb2x_StatefulA.class);    
    private Ejb2xBeanLocator ejb2xBeanLocator = null;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String runTest() throws Exception {
		LOG.info("[{}] running test", SERVER_A);
    	String testResult = "failed";
        try{
            String localValue = setCountryOfOrigin(IRELAND);
            createBeanFinder();
            StatefulRemoteObjectB ejb2StatelessB = ejb2xBeanLocator.getStatefulRemoteObjectB(EJB2X_STATEFUL_ADDRESS);
            //String remoteValue = ejb2StatelessB.getCountryOfOriginAndCreateCast(FILM_ID);
            String remoteValue = ejb2StatelessB.getCountryOfOrigin(FILM_ID);
            ejb2StatelessB.createCast(FILM_ID); 
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
    private void createBeanFinder() throws NamingException{
    	if (ejb2xBeanLocator == null){
    		ejb2xBeanLocator = new Ejb2xBeanLocator();
    	}
    }
    
    @PostConstruct
    private void startup(){
    	LOG.info("[{}] created", SERVER_A);
    }
    
    @Init
    public void init(){	
    }

}
