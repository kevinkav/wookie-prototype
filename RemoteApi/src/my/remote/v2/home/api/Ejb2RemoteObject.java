package my.remote.v2.home.api;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Ejb2RemoteObject extends EJBObject{

    /**
     * String used to bind Ejb2JTS bean to a IIOP naming service name.
     */
    public static String EJB2_BINDING_JNDI = "jts/Ejb2";
    
    /**
     * This method first tries to fetch the CountryOfOrigin attribute that was changed
     * by Ejb1 at the start of test and secondly this method tries to create and add 
     * a new Cast object to the Film object.
     * 
     * @param id
     * @return
     * @throws RemoteException
     */
    String getCountryOfOriginAndCreateCast(long id) throws RemoteException;
}
