package my.remote.serverB.ejb2.api;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface RemoteHomeB extends EJBHome{

    /**
     * Method that creates a RemoteObjectB.
     * 
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    RemoteObjectB create() throws RemoteException, CreateException;
}
