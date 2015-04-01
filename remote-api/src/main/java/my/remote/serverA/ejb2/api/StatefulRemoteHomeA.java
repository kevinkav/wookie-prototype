package my.remote.serverA.ejb2.api;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface StatefulRemoteHomeA extends EJBHome {

    /**
     * Method that creates a StatefulRemoteObjectA.
     * 
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    StatefulRemoteObjectA create() throws RemoteException, CreateException;

}
