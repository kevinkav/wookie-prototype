package my.remote.serverB.ejb2.api;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface StatelessRemoteObjectB extends EJBObject{

    /**
     * String used to bind Ejb2StatelessB bean to a IIOP naming service name.
     */
    public static String IIOP_BINDING = "jts/Ejb2x_StatelessB";
    
    /**
     * Gets the CountryOfOrigin attribute and creates Cast object.
     * 
     * @param id
     * @return
     * @throws RemoteException
     */
    String getCountryOfOriginAndCreateCast(long id) throws RemoteException;
    
    /**
     * Gets the CountryOfOrigin attribute.
     * 
     * @param id
     * @return
     * @throws RemoteException
     */
    String getCountryOfOrigin(long id) throws RemoteException;
    
    /**
     * Creates a Cast object associated with a film.
     * 
     * @param id
     * @throws RemoteException
     */
    void createCast(long id) throws RemoteException;
}