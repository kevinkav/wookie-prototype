package my.remote.serverB.ejb2.api;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface StatefulRemoteHomeB extends EJBHome {

    /**
     * Method that creates a StatefulRemoteObjectB.
     * 
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    StatefulRemoteObjectB create() throws RemoteException, CreateException;
    
}
