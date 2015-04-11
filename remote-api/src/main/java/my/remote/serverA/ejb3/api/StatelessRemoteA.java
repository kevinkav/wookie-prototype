package my.remote.serverA.ejb3.api;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface StatelessRemoteA {

    /**
     * JNDI used for lookup 
     */
    String JNDI_LOOKUP = "ejb:ear-module-a-1.0-SNAPSHOT/ejb-module-a-1.0-SNAPSHOT/Ejb3x_StatelessA!my.remote.serverA.ejb3.api.StatelessRemoteA";

    
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
