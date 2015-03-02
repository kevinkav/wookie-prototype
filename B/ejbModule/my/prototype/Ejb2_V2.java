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

import static my.remote.common.Constants.SERVER_A;
import static my.remote.common.Constants.SERVER_B;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.remote.v2.home.api.Ejb1RemoteHome;
import my.remote.v2.home.api.Ejb1RemoteObject;
import my.remote.v2.home.api.Ejb2RemoteHome;
import my.remote.v2.home.api.Ejb2RemoteObject;

@RemoteHome(Ejb2RemoteHome.class)
@EJB(name = Ejb2RemoteObject.EJB2_BINDING_JNDI, beanInterface = Ejb2RemoteObject.class)
@Stateless
public class Ejb2_V2 {
    
    private static final String EJB1_JTS_ADDRESS = 
            "corbaname:iiop:localhost:3528#" + Ejb1RemoteObject.EJB1_BINDING_JNDI; 
    
    private static final Logger LOG = LoggerFactory.getLogger(Ejb2_V2.class);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String getCountryOfOrigin(long id) throws RemoteException, NamingException, CreateException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        LOG.info("[{}] getCountryOfOrigin invoked with id [{}]: ", SERVER_B, id);
        Ejb1RemoteObject ejb1 = getEjb1RemoteObject();
        String attr = ejb1.getCountryOfOrigin(id);
        LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, attr, SERVER_A);
        //ejb1.addCastToFilm();
        return attr;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createCast(long id) throws RemoteException, NamingException, CreateException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        LOG.info("[{}] createCast invoked with id [{}]", SERVER_B, id);
        Ejb1RemoteObject ejb1 = getEjb1RemoteObject();
        ejb1.addCastToFilm();
    }

    
    private Ejb1RemoteObject getEjb1RemoteObject() throws NamingException, RemoteException, CreateException {
        LOG.info("[{}] looking up Ejb1RemoteOject from [{}]", SERVER_A, SERVER_B);
        InitialContext ctx = new InitialContext();
        final Object iiopObject = ctx.lookup(EJB1_JTS_ADDRESS);
        Ejb1RemoteHome ejb1RemoteHome = (Ejb1RemoteHome) PortableRemoteObject
                .narrow(iiopObject, Ejb1RemoteHome.class);
        Ejb1RemoteObject ejb1RemoteObject = ejb1RemoteHome.create();
        LOG.info("[{}] successfully found Ejb1RemoteOject from [{}]", SERVER_A, SERVER_B);
        return ejb1RemoteObject;
    }    
  
    
}
