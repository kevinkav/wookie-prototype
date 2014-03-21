package my.remote.v3.bean.locator;

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BeanLocator {

    private Hashtable<Object, Object> props;
    private Context context;
    private static final Logger LOG = Logger.getLogger(BeanLocator.class.getCanonicalName());

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
        initProperties();
        context = new InitialContext(props);
    }
    
    private void initProperties() {
        props = new Hashtable<Object, Object>();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put("jboss.naming.client.ejb.context", "true");
    }

}
