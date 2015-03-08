package my.remote.serverA.ejb2.api;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface RemoteObjectA extends EJBObject {

    /**
     * String used to bind Ejb1JTS bean to a IIOP naming service name.
     */
    String EJB1_BINDING_JNDI = "jts/Ejb2StatelessA";
     
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
     * @throws RemoteException
     */
    void addCastToFilm() throws RemoteException;
}
