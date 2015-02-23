package my.prototype.ejb;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.prototype.test.api.TestCase;
import my.remote.v3.api.Ejb1Remote;
import my.remote.v3.api.Ejb2Remote;
import my.remote.v3.bean.locator.BeanLocator;


@Stateless
@Local(TestCase.class)
@Remote(Ejb1Remote.class)
@EJB(name=Ejb1Remote.EJB1_JNDI_LOOKUP, beanInterface=Ejb1Remote.class)
public class Ejb1_V3 extends Ejb1Base implements Ejb1Remote {

    private static final String CLASSNAME = Ejb1_V3.class.getSimpleName();
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
    
    @PostConstruct
    private void startup(){
        LOG.info(PREFIX + "Created " + this.getClass().getSimpleName());
    }
}