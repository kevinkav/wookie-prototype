package my.remote.serverB.ejb2.api;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface StatefulRemoteObjectB extends EJBObject {

    /**
     * String used to bind Ejb2StatelessB bean to a IIOP naming service name.
     */
    public static String IIOP_BINDING = "jts/Ejb2x_StatefulB";
    
    /**
     * Gets the countryOfOrigin attribute.
     * 
     * @param id
     * @param portOffsetServerA
     * @return
     * @throws RemoteException
     */
    String getCountryOfOrigin(long id, int portOffsetServerA) throws RemoteException;
    
    /**
     * Creates a Cast object associated with a film.
     * 
     * @param id
     * @param portOffsetServerA
     * @throws RemoteException
     */
    void createCast(long id, int portOffsetServerA) throws RemoteException;
}
