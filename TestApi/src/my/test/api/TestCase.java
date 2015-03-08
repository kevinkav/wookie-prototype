package my.test.api;


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
     * Runs the test. 
     * 
     * @return
     * @throws Exception
     */
    String runTest() throws Exception;
    
    
}
