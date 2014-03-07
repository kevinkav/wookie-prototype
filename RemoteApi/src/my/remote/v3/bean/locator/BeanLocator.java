package my.remote.v3.bean.locator;

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

public class BeanLocator {

    private Hashtable<Object, Object> props;
    private Context context;
    private static final Logger LOG = Logger.getLogger(BeanLocator.class.getCanonicalName());

    /**
     * Method used to locate a remote EJB bean.
     * 
     * @param address
     * @return
     */
    public Object locateBean(String address) {
        initContext();
        Object bean = null;
        try {
            bean = context.lookup(address);
            LOG.info("Located bean successfully.");
        } catch (final Exception e) {
            LOG.severe("Bean lookup failed");
        }
        return bean;
    }


    private void initContext() {
        initProperties();
        try {
            context = new InitialContext(props);
        } catch (final Exception e) {
            LOG.severe("Context initialisation failed");
        }
    }
    
    private void initProperties() {
        props = new Hashtable<Object, Object>();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put("jboss.naming.client.ejb.context", "true");
    }

}
