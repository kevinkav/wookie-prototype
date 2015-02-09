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
    void runTest() throws Exception;
}
