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
package my.prototype.remote.home.api;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Ejb2RemoteObject extends EJBObject{

    public static String EJB2_BINDING_JNDI = "jts/Ejb2";
           
    String runTest(long id) throws RemoteException;
}
