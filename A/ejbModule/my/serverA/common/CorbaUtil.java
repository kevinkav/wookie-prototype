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
package my.serverA.common;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import my.remote.serverB.ejb2.api.RemoteHomeB;
import my.remote.serverB.ejb2.api.RemoteObjectB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for dealing with the Corba handling methods.
 * 
 */
public class CorbaUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CorbaUtil.class);
	private InitialContext ctx;

	/**
	 * Calling the constructor creates an initial context.
	 * 
	 * @throws NamingException
	 */
	public CorbaUtil() throws NamingException{
		createInitialContext();
	}

	/**
	 * Creates the Ejb2Remote object from the specified ejb address.
	 * 
	 * @param ejbAddress
	 * @return the Ejb2RemoteObject business object
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	public RemoteObjectB getEjb2RemoteObject(String ejbAddress) throws NamingException, RemoteException, CreateException{
		LOG.info("Attempting remote lookup of [{}]", ejbAddress);
		Object iiopObject = ctx.lookup(ejbAddress);
		 RemoteHomeB ejb2RemoteHome = (RemoteHomeB) PortableRemoteObject
                 .narrow(iiopObject, RemoteHomeB.class);
		 RemoteObjectB ejb2RemoteObject = (RemoteObjectB)ejb2RemoteHome.create();
		 LOG.info("Successfully created EJb2RemoteObject.");
		 return ejb2RemoteObject;
		 
	}

	private void createInitialContext() throws NamingException{
		LOG.info("Creating intial context.");
		ctx = new InitialContext();
		LOG.info("Created intial context successfully.");
	}
}
