package my.remote.serverB.ejb3.api;

import javax.ejb.Remote;

@Remote
public interface StatelessRemoteB {

    String JNDI_LOOKUP = "ejb:ear-module-b-1.0-SNAPSHOT/ejb-module-b-1.0-SNAPSHOT/Ejb3x_StatelessB!my.remote.serverB.ejb3.api.StatelessRemoteB";
    
    String getCountryOfOrigin(long id) throws Exception;
    
    void createCast(long id) throws Exception;
}
