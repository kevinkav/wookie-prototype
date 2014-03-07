package my.remote.v2.home.api;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface Ejb1RemoteHome extends EJBHome{
    
    /**
     * Method that creates a Ejb1RemoteObject.
     * 
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    Ejb1RemoteObject create() throws RemoteException, CreateException;
}
