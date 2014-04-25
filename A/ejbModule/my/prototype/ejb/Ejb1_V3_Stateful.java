package my.prototype.ejb;


import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.prototype.test.api.TestCase;
import my.remote.v3.api.Ejb1StatefulRemote;
import my.remote.v3.api.Ejb2Remote;
import my.remote.v3.bean.locator.BeanLocator;

@Local(TestCase.class)
@Remote(Ejb1StatefulRemote.class)
@Stateful
@EJB(name=Ejb1StatefulRemote.EJB1_STATEFUL_JNDI_LOOKUP, beanInterface=Ejb1StatefulRemote.class)
public class Ejb1_V3_Stateful extends Ejb1Base implements Ejb1StatefulRemote {

    private static final String CLASSNAME = Ejb1_V3_Stateful.class.getSimpleName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);
    private static final String PREFIX = "[" + CLASSNAME + "] ";
    
    
    @Inject
    BeanLocator beanLocator;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void runTest() throws Exception {
        try {
            String local_CountryOfOriginValue = changeCountryOfOrigin();
            // Call Ejb2
            Ejb2Remote ejb2Remote = (Ejb2Remote) beanLocator.locateBean(Ejb2Remote.EJB2_JNDI_LOOKUP);
            String remote_CountryOfOriginValue = ejb2Remote.getCountryOfOriginAndCreateCast(STAR_WARS_ID);
            // print both results
            printLocalAndRemoteValues(local_CountryOfOriginValue, remote_CountryOfOriginValue);
        } catch (Exception e) {
            LOG.severe(PREFIX + "Exception occurred rolling back transaction...");
            throw e;
        }
        LOG.info(PREFIX + "Commiting transaction...");
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
        LOG.info(PREFIX + "Finished with stateful bean " + this.getClass().getSimpleName());
    }

    @PostConstruct
    private void startup(){
        LOG.info(PREFIX + "Created " + this.getClass().getSimpleName());
    }

 
}
