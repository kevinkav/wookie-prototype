/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package my.remote.serverB.ejb3.api;

import javax.ejb.Remote;

@Remote
public interface StatefulRemoteB {

    String JNDI_LOOKUP = "ejb:ear-module-b-1.0-SNAPSHOT/ejb-module-b-1.0-SNAPSHOT/Ejb3x_StatefulB!my.remote.serverB.ejb3.api.StatefulRemoteB?stateful";

    String getCountryOfOriginAndCreateCast(long id) throws Exception;
    
    String getCountryOfOrigin(long id) throws Exception;
    
    void createCast(long id) throws Exception;
}
