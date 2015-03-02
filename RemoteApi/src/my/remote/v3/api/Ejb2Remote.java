package my.remote.v3.api;

import javax.ejb.Remote;

@Remote
public interface Ejb2Remote {

    String EJB2_JNDI_LOOKUP = "ejb:Ear2/B/Ejb2_V3!my.remote.v3.api.Ejb2Remote";

    String getCountryOfOriginAndCreateCast(long id) throws Exception;
    
    String getCountryOfOrigin(long id) throws Exception;
    
    void createCast(long id) throws Exception;
}
