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

import static my.remote.common.RemoteConstants.SERVER_A;
import static my.remote.common.RemoteConstants.SERVER_B;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.Init;
import javax.ejb.RemoteHome;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import my.remote.bean.locator.Ejb2xBeanLocator;
import my.remote.common.RemoteConstants;
import my.remote.serverA.ejb2.api.StatefulRemoteObjectA;
import my.remote.serverB.ejb2.api.StatefulRemoteHomeB;
import my.remote.serverB.ejb2.api.StatefulRemoteObjectB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateful
@RemoteHome(StatefulRemoteHomeB.class)
@EJB(name = StatefulRemoteObjectB.IIOP_BINDING, beanInterface = StatefulRemoteObjectB.class)
public class Ejb2x_StatefulB {

	
    //private static final String REMOTE_OBJECT_ADDRESS = "corbaname:iiop:localhost:3528#" + StatefulRemoteObjectA.IIOP_BINDING;     
    
    private static final Logger LOG = LoggerFactory.getLogger(Ejb2x_StatefulB.class);
    
    @Inject
    private Ejb2xBeanLocator ejb2xBeanLocator;
    
/*    public String getCountryOfOriginAndCreateCast(long id, int portOffsetServerA) throws RemoteException, NamingException, CreateException {
    	LOG.info("[{}] getCountryOfOriginAndCreateCast invoked with id [{}]: ", SERVER_B, id);
    	StatefulRemoteObjectA ejb2StatefulA = null;
		ejb2StatefulA = (StatefulRemoteObjectA) ejb2xBeanLocator.getStatefulRemoteObjectA((REMOTE_OBJECT_ADDRESS));
    	String countryOfOriginFromA = ejb2StatefulA.getCountryOfOrigin(id);
        LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginFromA, SERVER_A);
        ejb2StatefulA.addCastToFilm();
		return countryOfOriginFromA;
    }*/
    
    @Init
    public void init(){	
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String getCountryOfOrigin(long id, int portOffsetServerA) throws RemoteException, NamingException, CreateException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        LOG.info("[{}] getCountryOfOrigin invoked with id [{}]: ", SERVER_B, id);
        String address = RemoteConstants.createCorbaEndpointAddress(portOffsetServerA, StatefulRemoteObjectA.IIOP_BINDING);
        StatefulRemoteObjectA ejb2StatefulA = (StatefulRemoteObjectA) ejb2xBeanLocator.getStatefulRemoteObjectA(address);
        String countryOfOriginFromA = ejb2StatefulA.getCountryOfOrigin(id);
        LOG.info("[{}] received CountryOfOrigin value [{}] from [{}]", SERVER_B, countryOfOriginFromA, SERVER_A);
        return countryOfOriginFromA;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createCast(long id, int portOffsetServerA) throws RemoteException, NamingException, CreateException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        LOG.info("[{}] createCast invoked with id [{}]", SERVER_B, id);
        String address = RemoteConstants.createCorbaEndpointAddress(portOffsetServerA, StatefulRemoteObjectA.IIOP_BINDING);
        StatefulRemoteObjectA ejb2StatefulA = (StatefulRemoteObjectA) ejb2xBeanLocator.getStatefulRemoteObjectA(address);
        ejb2StatefulA.addCastToFilm();
    }
	
}
