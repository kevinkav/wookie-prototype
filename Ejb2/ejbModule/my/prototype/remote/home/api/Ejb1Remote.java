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
import javax.ejb.Remote;

@Remote
public interface Ejb1Remote extends EJBObject {

    public static String EJB1_REMOTE_JNDI = "java:/wookie/ejb1";
       
    String getAttributeCountryOfOrigin(long id) throws RemoteException;
    
    void setAttributeCountryOfOrigin(long id, String string) throws RemoteException;

}
