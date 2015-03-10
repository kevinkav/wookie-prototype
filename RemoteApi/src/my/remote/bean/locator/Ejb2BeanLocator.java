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

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import my.remote.serverA.ejb2.api.RemoteHomeA;
import my.remote.serverA.ejb2.api.RemoteObjectA;
import my.remote.serverB.ejb2.api.RemoteHomeB;
import my.remote.serverB.ejb2.api.RemoteObjectB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for dealing with the Corba handling methods.
 * 
 */
public class Ejb2BeanLocator {

	private static final Logger LOG = LoggerFactory.getLogger(Ejb2BeanLocator.class);
	
	private InitialContext ctx;
		
	private PortableObjectHelper portableObjectHelper;

	/**
	 * Calling the constructor creates an initial context.
	 * 
	 * @throws NamingException
	 */
	public Ejb2BeanLocator() throws NamingException{
		this.portableObjectHelper = new PortableObjectHelper();
		createInitialContext();
	}

	/**
	 * Creates the RemoteObjectB business object from the specified ejb address.
	 * 
	 * @param ejbAddress
	 * @return the Ejb2RemoteObject business object
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	public RemoteObjectB getRemoteObjectB(String ejbAddress) throws NamingException, RemoteException, CreateException{
		LOG.info("Attempting remote lookup of [{}]", ejbAddress);
		Object iiopObject = doLookUp(ejbAddress);
		RemoteHomeB ejb2RemoteHome = (RemoteHomeB) portableObjectHelper.narrow(iiopObject, RemoteHomeB.class);
		RemoteObjectB remoteObjectB = (RemoteObjectB)ejb2RemoteHome.create();
		LOG.info("Successfully created remoteObjectB.");
		return remoteObjectB;

	}
	
	/**
	 * Creates the RemoteObjectA business object from the specified ejb address.
	 * 
	 * @param ejbAddress
	 * @return
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	public RemoteObjectA getRemoteObjectA(String ejbAddress) throws NamingException, RemoteException, CreateException{
		LOG.info("Attempting remote lookup of [{}]", ejbAddress);
		Object iiopObject = doLookUp(ejbAddress);
		//RemoteHomeA ejb2RemoteHome = (RemoteHomeA) narrow(iiopObject, RemoteHomeA.class);
		RemoteHomeA ejb2RemoteHome = (RemoteHomeA) portableObjectHelper.narrow(iiopObject, RemoteHomeA.class);
		RemoteObjectA remoteObjectA = (RemoteObjectA)ejb2RemoteHome.create();
		LOG.info("Successfully created remoteObjectA.");
		return remoteObjectA;

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
