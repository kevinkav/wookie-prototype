package my.remote.serverA.ejb3.api;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface StatelessRemoteA {

    /**
     * JNDI used for lookup 
     */
    String EJB1_JNDI_LOOKUP = "ejb:Ear1/A/Ejb1_V3!my.remote.v3.api.Ejb1Remote";

    
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
