package my.prototype.ejb;

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

public class BeanLocator {

    private Hashtable<Object, Object> props;
    private Context context;
    private static final Logger LOGGER = Logger.getLogger(BeanLocator.class.getCanonicalName());

    
    public Object locateBean(String address) {
        initContext();
        Object bean = null;
        try {
            bean = context.lookup(address);
            LOGGER.info("Located bean");
        } catch (final Exception e) {
            LOGGER.severe("Bean lookup failed");
            LOGGER.severe(e.toString());
        }
        return bean;
    }


    private void initContext() {
        initProperties();
        try {
            context = new InitialContext(props);
            LOGGER.info("Context initialised");
        } catch (final Exception e) {
            LOGGER.severe("Context initialisation failed");
            final StackTraceElement[] st = e.getStackTrace();
            for (final StackTraceElement ste : st) {
                LOGGER.severe(ste.toString());
            }
        }
    }
    
    private void initProperties() {
        props = new Hashtable<Object, Object>();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put("jboss.naming.client.ejb.context", "true");
    }

}
