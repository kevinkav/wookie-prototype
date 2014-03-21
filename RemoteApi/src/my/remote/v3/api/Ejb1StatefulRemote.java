package my.remote.v3.api;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface Ejb1StatefulRemote {

    /**
     * JNDI used for lookup 
     */
    String EJB1_STATEFUL_JNDI_LOOKUP = 
            "ejb:Ear1/A/Ejb1_V3_Stateful!my.remote.v3.api.Ejb1StatefulRemote";

    
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
