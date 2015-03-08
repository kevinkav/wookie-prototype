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
package my.serverB.ejb2.impl;

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

import my.remote.serverA.ejb2.api.RemoteHomeA;
import my.remote.serverA.ejb2.api.RemoteObjectA;
import my.remote.serverB.ejb2.api.RemoteHomeB;
import my.remote.serverB.ejb2.api.RemoteObjectB;

@RemoteHome(RemoteHomeB.class)
@EJB(name = RemoteObjectB.IIOP_BINDING, beanInterface = RemoteObjectB.class)
@Stateless
public class Ejb2StatelessB {
    
    private static final String EJB1_JTS_ADDRESS = "corbaname:iiop:localhost:3528#" + RemoteObjectA.IIOP_BINDING; 
    
    private static final Logger LOG = LoggerFactory.getLogger(Ejb2StatelessB.class);

    public String getCountryOfOriginAndCreateCast(long id) throws RemoteException, NamingException, CreateException {
    	LOG.info("[{}] getCountryOfOriginAndCreateCast invoked with id [{}]: ", SERVER_B, id);
    	RemoteObjectA ejb2StatelessA = getEjb1RemoteObject();
    	String countryOfOriginFromA = ejb2StatelessA.getCountryOfOrigin(id);
        LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginFromA, SERVER_A);
        ejb2StatelessA.addCastToFilm();
		return countryOfOriginFromA;
    }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String getCountryOfOrigin(long id) throws RemoteException, NamingException, CreateException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        LOG.info("[{}] getCountryOfOrigin invoked with id [{}]: ", SERVER_B, id);
        RemoteObjectA ejb2StatelessA = getEjb1RemoteObject();
        String countryOfOriginFromA = ejb2StatelessA.getCountryOfOrigin(id);
        LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginFromA, SERVER_A);
        return countryOfOriginFromA;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createCast(long id) throws RemoteException, NamingException, CreateException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        LOG.info("[{}] createCast invoked with id [{}]", SERVER_B, id);
        RemoteObjectA ejb2StatelessA = getEjb1RemoteObject();
        ejb2StatelessA.addCastToFilm();
    }

    
    private RemoteObjectA getEjb1RemoteObject() throws NamingException, RemoteException, CreateException {
        LOG.info("[{}] looking up Ejb1RemoteOject from [{}]", SERVER_A, SERVER_B);
        InitialContext ctx = new InitialContext();
        final Object iiopObject = ctx.lookup(EJB1_JTS_ADDRESS);
        RemoteHomeA ejb2StatelessRemoteHome = (RemoteHomeA) PortableRemoteObject
                .narrow(iiopObject, RemoteHomeA.class);
        RemoteObjectA ejb2StatelessA = ejb2StatelessRemoteHome.create();
        LOG.info("[{}] successfully found ejb2StatelessA from [{}]", SERVER_A, SERVER_B);
        return ejb2StatelessA;
    }    
  
    
}
