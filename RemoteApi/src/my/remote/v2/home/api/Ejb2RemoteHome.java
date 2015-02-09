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
