package my.remote.v3.api;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface Ejb2Remote {

    String EJB2_REMOTE_JNDI = "java:remote/ejb2";
    
    String runTest(long id) throws RemoteException;
}
