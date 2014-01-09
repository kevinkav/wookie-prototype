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
package my.prototype;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import my.prototype.api.Ejb3Remote;

@Stateless
public class Ejb2 {

    //@EJB(lookup = Ejb3Remote.EJB3_REMOTE_JNDI)
    private Ejb3Remote ejb3;
    
    private my.prototype.remote.home.api.Ejb3RemoteObject ejb3Remote;
        
    private String EJB3_JNDI = "java:global/Ear2/Ejb3/Ejb3!my.prototype.api.Ejb3Remote";
    // lookup using ejb namespace
    private String EJB3_EJB_LOOKUP = "ejb:Ear2/Ejb3//Ejb3!my.prototype.api.Ejb3Remote";
    
    //@TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void runTest(long id) throws RemoteException, NamingException, CreateException{
        log("Ejb2 calling Ejb3 using resolveBean method");
        //ejb3 = ejbLookup(EJB3_JNDI);
        //ejb3 = resolveBean(EJB3_EJB_LOOKUP);
        //ejb3.setModifiedAttribute(id);
        
        ejb3Remote = getEjb3Object();
        log("###### Got EJB3RemoteObject");
        ejb3Remote.runTest(id);
    }
    

    private my.prototype.remote.home.api.Ejb3RemoteObject getEjb3Object() throws NamingException, RemoteException, CreateException {
        log("#### getEjb3Object....");
        InitialContext ctx = new InitialContext();
        String jndi = getEjb3RemoteHomeJndi();
        final Object iiopObject = ctx.lookup(jndi);
        
        my.prototype.remote.home.api.Ejb3RemoteHome ejb3RemoteHome = (my.prototype.remote.home.api.Ejb3RemoteHome) PortableRemoteObject
                .narrow(iiopObject, my.prototype.remote.home.api.Ejb3RemoteHome.class);
        
        return ejb3RemoteHome.create();
    }
    
    private String getEjb3RemoteHomeJndi(){
        log("Building the JNDI uri for EJB3 Remote Home lookup");
        log("##### jboss.bind.address.unsecure " + System.getProperty("jboss.bind.address.unsecure"));
        log("##### jacorb.port " + System.getProperty("jacorb.port"));
        log("##### jboss.socket.binding.port-offset " + System.getProperty("jboss.socket.binding.port-offset"));

        final String ip = System.getProperty("jboss.bind.address.unsecure", "127.0.0.1");
        final String port = System.getProperty("jacorb.port", "3528");
        final String offset = System.getProperty("jboss.socket.binding.port-offset","0");
        final Integer portNumber = 100 + Integer.parseInt(port); 
        String address = "corbaname:iiop:" + ip + ":" + portNumber.toString() + "#" + my.prototype.remote.home.api.Ejb3RemoteObject.EJB3_BINDING_JNDI;
        log(address);
        return address;
    }

    public void kickEjb3(){
        ejb3 = resolveBean(EJB3_EJB_LOOKUP);
        ejb3.kickEjb3("EJB2");
    }
    
    private void log (String str){
        System.out.println(str);
    }
    
    private <T> T ejbLookup(String jndi){
        T bean = null; 
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put("jboss.naming.client.ejb.context", "true");

        try {
            InitialContext ic = new InitialContext(props);
            bean = (T)ic.lookup(jndi);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return bean;
    }
    
    public static <T> T resolveBean(final String jndiName) {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        try {
            final InitialContext ctx = new InitialContext(jndiProperties);
            @SuppressWarnings("unchecked")
            final T bean = (T) ctx.lookup(jndiName);
            return bean;
        } catch (NamingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
