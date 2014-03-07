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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import my.prototype.remote.home.api.Ejb1RemoteObject;
import my.prototype.remote.home.api.Ejb2RemoteHome;

@RemoteHome(Ejb2RemoteHome.class)
@EJB(name = my.prototype.remote.home.api.Ejb2RemoteObject.EJB2_BINDING_JNDI, beanInterface = my.prototype.remote.home.api.Ejb2RemoteObject.class)
@Stateless
public class Ejb2 {
    
    
    private String ejb1Address = "corbaname:iiop:localhost:3528#" + Ejb1RemoteObject.EJB1_BINDING_JNDI; 
    
    private final String LEAD_ACTOR = "Harrison Ford";

    private static final Logger LOGGER = Logger.getLogger(Ejb2.class.getCanonicalName());

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String runTest(long id) throws RemoteException, NamingException, CreateException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        my.prototype.remote.home.api.Ejb1RemoteObject ejb1 = getEjb1RemoteObject();
        LOGGER.info("Ejb2: getting 'CountryOfOrigin' attribute from Ejb1'");
        String attr = ejb1.getAttributeCountryOfOrigin_RemoteCall(id);
        ejb1.createCast_RemoteCall(LEAD_ACTOR);
        return attr;
    }

    
    private my.prototype.remote.home.api.Ejb1RemoteObject getEjb1RemoteObject() throws NamingException, RemoteException, CreateException {
        LOGGER.info("Ejb2: getting Ejb1RemoteHome....");
        InitialContext ctx = new InitialContext();
        final Object iiopObject = ctx.lookup(ejb1Address);
        my.prototype.remote.home.api.Ejb1RemoteHome ejb1RemoteHome = (my.prototype.remote.home.api.Ejb1RemoteHome) PortableRemoteObject
                .narrow(iiopObject, my.prototype.remote.home.api.Ejb1RemoteHome.class);
        
        return ejb1RemoteHome.create();
    }    
  
    
}
