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

import javax.ejb.EJBObject;

public interface Ejb1RemoteObject extends EJBObject {

    /**
     * String used to bind Ejb1JTS bean to a IIOP naming service name.
     */
    String EJB1_BINDING_JNDI = "jts/Ejb1";
     
    /**
     * Gets the CountryOfOrigin attribute from the Film object.
     * 
     * @param id
     * @return
     * @throws RemoteException
     */
    String getCountryOfOrigin(long id) throws RemoteException;
    
    /**
     * Creates a Cast object and adds it to Film object.
     * 
     * @throws RemoteException
     */
    void addCastToFilm() throws RemoteException;
}
