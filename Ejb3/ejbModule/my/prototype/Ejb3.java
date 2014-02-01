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
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import my.prototype.api.Ejb1Remote;
import my.prototype.remote.home.api.Ejb1RemoteObject;
import my.prototype.remote.home.api.Ejb3RemoteHome;

@RemoteHome(Ejb3RemoteHome.class)
@EJB(name = my.prototype.remote.home.api.Ejb3RemoteObject.EJB3_BINDING_JNDI, beanInterface = my.prototype.remote.home.api.Ejb3RemoteObject.class)
@Stateless
public class Ejb3 {

    Ejb1Remote ejb1;
    
    String EJB1_JNDI = "java:global/Ear1/Ejb1/Ejb1!my.prototype.api.Ejb1Remote";
    
    String EJB1_EJB_JNDI = "ejb:Ear1/Ejb1//Ejb1!my.prototype.api.Ejb1Remote";
    
    final String ejb1Address = "corbaname:iiop:localhost:3528#" + Ejb1RemoteObject.EJB1_BINDING_JNDI; 

    private static final Logger LOGGER = Logger.getLogger(Ejb3.class.getCanonicalName());

   
    //@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)   // tx will not be propagated from EJB1
    //@TransactionAttribute(TransactionAttributeType.SUPPORTS)       // successfull only if EJB1 starts TX
    //@TransactionAttribute(TransactionAttributeType.MANDATORY)        // successfull only if EJB1 starts TX
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String runTest(long id) throws RemoteException, NamingException, CreateException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        my.prototype.remote.home.api.Ejb1RemoteObject ejb1 = getEjb1RemoteObject();
        LOGGER.info("Ejb3: getting 'CountryOfOrigin' attribute from Ejb1'");
        String attr = ejb1.getAttributeCountryOfOrigin(id);
        ejb1.createCast("Harrison Ford");
        return attr;
    }

    
    private static <T> T resolveBean(final String jndiName) {
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
    
    private my.prototype.remote.home.api.Ejb1RemoteObject getEjb1RemoteObject() throws NamingException, RemoteException, CreateException {
        LOGGER.info("Ejb3: getting Ejb1RemoteHome....");
        InitialContext ctx = new InitialContext();
        final Object iiopObject = ctx.lookup(ejb1Address);
        my.prototype.remote.home.api.Ejb1RemoteHome ejb1RemoteHome = (my.prototype.remote.home.api.Ejb1RemoteHome) PortableRemoteObject
                .narrow(iiopObject, my.prototype.remote.home.api.Ejb1RemoteHome.class);
        
        return ejb1RemoteHome.create();
    }    
  
    
}
