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
package my.remote.bean.locator;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import my.remote.serverA.ejb2.api.StatefulRemoteHomeA;
import my.remote.serverA.ejb2.api.StatefulRemoteObjectA;
import my.remote.serverA.ejb2.api.StatelessRemoteHomeA;
import my.remote.serverA.ejb2.api.StatelessRemoteObjectA;
import my.remote.serverB.ejb2.api.StatefulRemoteHomeB;
import my.remote.serverB.ejb2.api.StatefulRemoteObjectB;
import my.remote.serverB.ejb2.api.StatelessRemoteHomeB;
import my.remote.serverB.ejb2.api.StatelessRemoteObjectB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for dealing with the Corba handling methods.
 * 
 */
public class Ejb2xBeanLocator implements Serializable{

	
	private static final long serialVersionUID = -4891443888344085154L;

	private static final Logger LOG = LoggerFactory.getLogger(Ejb2xBeanLocator.class);
	
	private InitialContext ctx;
		
	private PortableObjectHelper portableObjectHelper;

	/**
	 * Calling the constructor creates an initial context.
	 * 
	 * @throws NamingException
	 */
	public Ejb2xBeanLocator() throws NamingException{
		this.portableObjectHelper = new PortableObjectHelper();
		createInitialContext();
	}
	
	/**
	 * Creates the StatelessRemoteObjectA business object from the specified ejb address.
	 * 
	 * @param ejbAddress
	 * @return
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	public StatelessRemoteObjectA getStatelessRemoteObjectA(String ejbAddress) throws NamingException, RemoteException, CreateException{
		LOG.info("Attempting remote lookup of [{}]", ejbAddress);
		Object iiopObject = doLookUp(ejbAddress);
		StatelessRemoteHomeA remoteHome = (StatelessRemoteHomeA) portableObjectHelper.narrow(iiopObject, StatelessRemoteHomeA.class);
		StatelessRemoteObjectA remoteObjectA = (StatelessRemoteObjectA)remoteHome.create();
		LOG.info("Successfully created StatelessRemoteObjectA.");
		return remoteObjectA;

	}
	
	/**
	 * Creates the StatefulRemoteObjectA business object from the specified ejb address.
	 * 
	 * @param ejbAddress
	 * @return
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	public StatefulRemoteObjectA getStatefulRemoteObjectA(String ejbAddress) throws NamingException, RemoteException, CreateException{
		LOG.info("Attempting remote lookup of [{}]", ejbAddress);
		Object iiopObject = doLookUp(ejbAddress);
		StatefulRemoteHomeA remoteHome = (StatefulRemoteHomeA) portableObjectHelper.narrow(iiopObject, StatefulRemoteHomeA.class);
		StatefulRemoteObjectA remoteObjectA = (StatefulRemoteObjectA)remoteHome.create();
		LOG.info("Successfully created StatefulRemoteObjectA.");
		return remoteObjectA;

	}

	/**
	 * Creates the StatelessRemoteObjectB business object from the specified ejb address.
	 * 
	 * @param ejbAddress
	 * @return the Ejb2RemoteObject business object
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	public StatelessRemoteObjectB getStatelessRemoteObjectB(String ejbAddress) throws NamingException, RemoteException, CreateException{
		LOG.info("Attempting remote lookup of [{}]", ejbAddress);
		Object iiopObject = doLookUp(ejbAddress);
		StatelessRemoteHomeB remoteHome = (StatelessRemoteHomeB) portableObjectHelper.narrow(iiopObject, StatelessRemoteHomeB.class);
		StatelessRemoteObjectB statelessRemoteObjectB = (StatelessRemoteObjectB)remoteHome.create();
		LOG.info("Successfully created StatelessRemoteObjectB.");
		return statelessRemoteObjectB;

	}
	
	/**
	 * Creates the StatefulRemoteObjectB business object from the specified ejb address.
	 * 
	 * @param ejbAddress
	 * @return 
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	public StatefulRemoteObjectB getStatefulRemoteObjectB(String ejbAddress) throws NamingException, RemoteException, CreateException{
		LOG.info("Attempting remote lookup of [{}]", ejbAddress);
		Object iiopObject = doLookUp(ejbAddress);
		StatefulRemoteHomeB remoteHome = (StatefulRemoteHomeB) portableObjectHelper.narrow(iiopObject, StatefulRemoteHomeB.class);
		StatefulRemoteObjectB statelessRemoteObjectB = (StatefulRemoteObjectB)remoteHome.create();
		LOG.info("Successfully created StatefulRemoteObjectB.");
		return statelessRemoteObjectB;

	}
	


	private Object doLookUp(String ejbAddress) throws NamingException{
		return ctx.lookup(ejbAddress);
	}
	
	
	private void createInitialContext() throws NamingException{
		if (ctx == null){
			LOG.info("Creating intial context.");
			ctx = new InitialContext();
			LOG.info("Created intial context successfully.");
		}
	}
	
}
