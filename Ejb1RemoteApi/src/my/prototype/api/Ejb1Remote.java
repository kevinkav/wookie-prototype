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
package my.prototype.api;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface Ejb1Remote {

    public static String EJB1_REMOTE_JNDI = "java:remote/ejb1";
        
    String getAttributeCountryOfOrigin_RemoteCall(long id) throws RemoteException;
    
    void createCast_RemoteCall(String leadActor) throws RemoteException;

}
