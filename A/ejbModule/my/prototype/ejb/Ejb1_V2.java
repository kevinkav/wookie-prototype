package my.prototype.ejb;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import my.prototype.test.api.TestCase;
import my.remote.v2.home.api.Ejb1RemoteHome;
import my.remote.v2.home.api.Ejb1RemoteObject;
import my.remote.v2.home.api.Ejb2RemoteHome;
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
    private static final String CLASSNAME = Ejb1_V2.class.getSimpleName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);
    private static final String PREFIX = "[" + CLASSNAME + "] ";


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void runTest() throws Exception {
        try{
            String local_CountryOfOriginValue = changeCountryOfOrigin();
          
            // Make remote call
            Ejb2RemoteObject ejb2Remote = (Ejb2RemoteObject) getEjbRemoteObject(EJB2_ADDRESS);
            String remote_CountryOfOriginValue = ejb2Remote.getCountryOfOriginAndCreateCast(STAR_WARS_ID);

            // print both results
            printLocalAndRemoteValues(local_CountryOfOriginValue, remote_CountryOfOriginValue); 
        }catch (Exception e){
            LOG.severe(PREFIX + "Exception occurred rolling back transaction...");
            throw e;
        }
        LOG.info(PREFIX + "Commiting transaction.");
    }


    private Object getEjbRemoteObject(String ejbAddress) throws NamingException, RemoteException, CreateException {
        LOG.info(PREFIX + "Looking up address:  " + ejbAddress);
        InitialContext ctx = new InitialContext();
        final Object iiopObject = ctx.lookup(ejbAddress);
        Object remoteObj = null;
        if (ejbAddress.contains(Ejb2RemoteObject.EJB2_BINDING_JNDI)){
            Ejb2RemoteHome ejb2RemoteHome = (Ejb2RemoteHome) PortableRemoteObject
                    .narrow(iiopObject, Ejb2RemoteHome.class);
            remoteObj = ejb2RemoteHome.create(); 
        }
        return remoteObj;
    }

    @PostConstruct
    private void startup(){
        LOG.info(PREFIX + "Created " + this.getClass().getSimpleName());
    }

}


