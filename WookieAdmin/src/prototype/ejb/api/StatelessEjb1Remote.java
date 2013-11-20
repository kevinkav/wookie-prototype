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
package prototype.ejb.api;

import javax.ejb.Remote;

import prototype.entity.StarWars;

@Remote
public interface StatelessEjb1Remote {

    String REMOTE_JNDI = "java:/ejb1/StatelessEjb1Remote";
    
    void persist(StarWars film);

    String getStarWars();

    StarWars createStartWars();
    
}
