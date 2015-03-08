package my.remote.serverB.ejb3.api;

import javax.ejb.Remote;

@Remote
public interface StatelessRemoteB {

    String JNDI_LOOKUP = "ejb:Ear2/B/Ejb3StatelessB!my.remote.serverB.ejb3.api.StatelessRemoteB";

    String getCountryOfOriginAndCreateCast(long id) throws Exception;
    
    String getCountryOfOrigin(long id) throws Exception;
    
    void createCast(long id) throws Exception;
}
