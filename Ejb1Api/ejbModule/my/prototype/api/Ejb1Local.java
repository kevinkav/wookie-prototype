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
package my.prototype.api;

import javax.ejb.Local;

@Local
public interface Ejb1Local {
        
    void setUp() throws Exception;
    
    void tearDown() throws Exception ;
    
    String runTest1() throws Exception;
    
}