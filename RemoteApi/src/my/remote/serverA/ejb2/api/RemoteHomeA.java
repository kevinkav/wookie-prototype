package my.remote.serverA.ejb2.api;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface RemoteHomeA extends EJBHome{
    
    /**
     * Method that creates a RemoteObjectA.
     * 
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    RemoteObjectA create() throws RemoteException, CreateException;
}
