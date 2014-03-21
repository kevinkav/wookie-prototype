package my.remote.v3.api;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface Ejb1Remote {

    /**
     * JNDI used to lookup Ejb1NonJTS
     */
    //String EJB1_REMOTE_JNDI = "java:remote/ejb1";
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
