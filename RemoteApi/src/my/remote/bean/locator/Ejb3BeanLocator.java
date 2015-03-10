package my.remote.bean.locator;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Beanlocator thats used to locate EJBs remotely.
 * Note it implements serializable because the stateful ejb cannot
 * be passivated unless this class is serializable.
 */
public class Ejb3BeanLocator implements Serializable {

    private static final long serialVersionUID = -520152991172756468L;
    private Hashtable<Object, Object> props;
    private Context context;
    private static final Logger LOG = Logger.getLogger(Ejb3BeanLocator.class.getCanonicalName());

    /**
     * Method used to locate a remote EJB bean.
     * 
     * @param address
     * @return
     * @throws NamingException 
     */
    public Object locateBean(String address) throws NamingException {
        initContext();
        Object bean = context.lookup(address);
        LOG.info("Located bean successfully.");
        return bean;
    }


    private void initContext() throws NamingException {
        if (context == null){
        	initProperties();
        	context = new InitialContext(props);
        }
    }
    
    private void initProperties() {
        props = new Hashtable<Object, Object>();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put("jboss.naming.client.ejb.context", "true");
    }

}
