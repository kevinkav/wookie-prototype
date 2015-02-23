package my.prototype.ejb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void runTest() throws Exception {
        try{
            String local_CountryOfOriginValue = changeCountryOfOrigin();
          
            // Make remote call
            createCorbaUtil();
            Ejb2RemoteObject ejb2Remote = corbaUtil.getEjb2RemoteObject(EJB2_ADDRESS);
            String remote_CountryOfOriginValue = ejb2Remote.getCountryOfOriginAndCreateCast(STAR_WARS_ID);

            // print both results
            printLocalAndRemoteValues(local_CountryOfOriginValue, remote_CountryOfOriginValue); 
        }catch (Exception e){
            LOG.error("Exception occurred rolling back transaction...");
            throw e;
        }
        LOG.info("Commiting transaction.");
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
        LOG.info("Created " + this.getClass().getSimpleName());
    }

}


