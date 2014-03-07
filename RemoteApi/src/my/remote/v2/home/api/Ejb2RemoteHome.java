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
package my.remote.v2.home.api;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface Ejb2RemoteHome extends EJBHome{

    /**
     * Method that creates a Ejb2RemoteObject.
     * 
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    Ejb2RemoteObject create() throws RemoteException, CreateException;
}
