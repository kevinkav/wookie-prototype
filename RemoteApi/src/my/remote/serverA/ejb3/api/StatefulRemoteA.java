package my.remote.serverA.ejb3.api;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface StatefulRemoteA {

    /**
     * JNDI used for lookup 
     */
    String JNDI_LOOKUP = "ejb:Ear1/A/Ejb3x_StatefulA!my.remote.serverA.ejb3.api.StatefulRemoteA?stateful";

    
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
     * @param leadActor
     * @throws RemoteException
     */
    void addCastToFilm() throws RemoteException;

}
