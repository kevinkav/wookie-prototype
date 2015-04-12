package my.test.api;


public interface TestCaseLocal {

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
     * 
     * @param portOffsetServerA 
     * Used for JTS IIOP endpoint calculation at runtime only. This value is the port
     * offset that server A is running on.
     * 
     * @param portOffsetServerB 
     * Used for JTS IIOP endpoint calculation at runtime only. This value is the port
     * offset that server B is running on.
     * 
     * @return
     * @throws Exception
     */
    String runTest(int portOffsetServerA, int portOffsetServerB) throws Exception;
    
    
}
