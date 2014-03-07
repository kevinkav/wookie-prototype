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
package my.prototype.test.api;

public interface TestCase {

    /**
     * Does any test case setup.
     * 
     * @return
     */
    String setUp();
    
    /**
     * Does any test case tear down.
     * 
     * @return
     */
    String tearDown();
    
    /**
     * Gets the result of running the test.
     * @return
     */
    String getResult();
    
    /**
     * Runs the test. 
     */
    void runTest();
}
