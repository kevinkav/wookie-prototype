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
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import my.prototype.api.Ejb1Remote;
import my.prototype.api.Ejb3Remote;
import my.prototype.remote.home.api.Ejb3RemoteHome;

@RemoteHome(Ejb3RemoteHome.class)
@EJB(name = my.prototype.remote.home.api.Ejb3Remote.EJB3_REMOTE_JNDI, beanInterface = my.prototype.remote.home.api.Ejb3Remote.class)
//@Remote(Ejb3Remote.class)
@Stateless
public class Ejb3 {
//    public class Ejb3 implements Ejb3Remote{


    private Ejb1Remote ejb1;
    
    private String EJB1_JNDI = "java:global/Ear1/Ejb1/Ejb1!my.prototype.api.Ejb1Remote";
    private String EJB1_EJB_JNDI = "ejb:Ear1/Ejb1//Ejb1!my.prototype.api.Ejb1Remote";
    
    

   
    //@Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY) 
    public String runTest(long id) throws RemoteException, NamingException, CreateException {
        //log("####### (EJB3) setModifiedAttribute() for id " + id + " #########");
        //ejb1 = resolveBean(EJB1_EJB_JNDI);
        
        my.prototype.remote.home.api.Ejb1Remote ejb1 = getEjb1RemoteHome();
        log("####### (Ejb3) getting 'CountryOfOrigin' attribute from Ejb1' #########");
        return ejb1.getAttributeCountryOfOrigin(id);
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
    
    private my.prototype.remote.home.api.Ejb1Remote getEjb1RemoteHome() throws NamingException, RemoteException, CreateException {
        log("#### (EJB3) getEjb1RemoteHome....");
        InitialContext ctx = new InitialContext();
        String jndi = getEjb1RemoteHomeJndi();
        final Object iiopObject = ctx.lookup(jndi);
        
        my.prototype.remote.home.api.Ejb1RemoteHome ejb1RemoteHome = (my.prototype.remote.home.api.Ejb1RemoteHome) PortableRemoteObject
                .narrow(iiopObject, my.prototype.remote.home.api.Ejb1RemoteHome.class);
        
        return ejb1RemoteHome.create();
    }    
    
 
    private String getEjb1RemoteHomeJndi() {
        log("Building the JNDI uri for EJB1 Remote Home lookup");
        log("##### jboss.bind.address.unsecure " + System.getProperty("jboss.bind.address.unsecure"));
        log("##### jacorb.port " + System.getProperty("jacorb.port"));
        log("##### jboss.socket.binding.port-offset " + System.getProperty("jboss.socket.binding.port-offset"));

        final String ip = System.getProperty("jboss.bind.address.unsecure", "127.0.0.1");
        final String port = System.getProperty("jacorb.port", "3528");
        final String offset = System.getProperty("jboss.socket.binding.port-offset","0");
        final Integer portNumber = Integer.parseInt(port); 
        //String address = "corbaname:iiop:" + ip + ":" + portNumber.toString() + "#" + Ejb1Remote.EJB1_REMOTE_JNDI;
        //String address = "corbaname:iiop:" + ip + ":" + portNumber.toString() + "#jts/Ejb1";
        String address = "corbaname:iiop:localhost:3528#jts/Ejb1";
        log(address);
        return address;
    }


    private void log (String str){
        System.out.println(str);
    }

    
}
