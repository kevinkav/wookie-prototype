/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package my.client.managed.bean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import my.test.api.TestCase;



@ManagedBean(name = "tc")
public class TestCaseManager {

    @EJB(beanName="Ejb2StatelessA")
    TestCase ejb2xStatelessA;
    
/*    @EJB(beanName="Ejb2StatefulA")
    TestCase ejb2xStatefulA;*/
    
    @EJB(beanName="Ejb3StatelessA")
    TestCase ejb3xStatelessA;
    
    @EJB(beanName="Ejb3StatefulA")
    TestCase ejb3xStatefulA;

    private static final String EXECUTED_MSG = "[Executed Test]";
    private static final String DONE = "Done";
    private static final String FAILED = "Failed";
    
    private String tc1_test_result;
    private String tc1_setup_result;
    private String tc1_teardown_result;
    private String tc1_info = "";
    
    private String tc2_test_result;
    private String tc2_setup_result;
    private String tc2_teardown_result;
    private String tc2_info = "";
    
    private String tc3_test_result;
    private String tc3_setup_setUp_result;
    private String tc3_teardown_result;
    private String tc3_info = "";
    
    private String tc4_test_result;
    private String tc4_setup_result;
    private String tc4_teardown_result;
    private String tc4_info = "";
    
    
    /* ********************************************
     * TC1 EJB3.1 Stateless
     ******************************************** */
    public String getTeardownResultTC1() {
        return tc1_teardown_result;
    }

    public void setTeardownResultTC1(String teardownResult) {
        this.tc1_teardown_result = teardownResult;
    }

    public String getSetupResultTC1() {
        return tc1_setup_result;
    }

    public void setSetupResultTC1(String setupResult) {
        this.tc1_setup_result = setupResult;
    }

    public String getResultTC1() {
        return tc1_test_result;
    }

    public void setResultTC1(String result) {
        this.tc1_test_result = result;
    }
    
    public String getInfoTC1() {
        return tc1_info;
    }

    public void setInfoTC1(String info) {
        this.tc1_info = info;
    }
    
    public void setupTC1() { 
        tc1_info = ejb3xStatelessA.setUp();
        tc1_setup_result = DONE;
    }

    public void tearDownTC1() {
        tc1_info = ejb3xStatelessA.tearDown();
        tc1_teardown_result = DONE;
    }


    public void executeTC1() {
        try {
        	tc1_test_result = ejb3xStatelessA.runTest();
            tc1_info = EXECUTED_MSG;
        } catch (Exception e) {
            tc1_info =  "[" + e.getMessage() + "]";
            tc1_test_result = FAILED;
        }
    }
    
    /* ********************************************
     * TC2 EJB 3.1 Statefull
     ******************************************** */
    public String getTeardownResultTC2() {
        return tc2_teardown_result;
    }

    public void setTeardownResultTC2(String teardownResult) {
        this.tc2_teardown_result = teardownResult;
    }

    public String getSetupResultTC2() {
        return tc2_setup_result;
    }

    public void setSetupResultTC2(String setupResult) {
        this.tc2_setup_result = setupResult;
    }

    public String getResultTC2() {
        return tc2_test_result;
    }

    public void setResultTC2(String result) {
        this.tc2_test_result = result;
    }
    
    public String getInfoTC2() {
        return tc2_info;
    }

    public void setInfoTC2(String info) {
        this.tc2_info = info;
    }
    
    public void setupTC2() { 
        tc2_info = ejb3xStatefulA.setUp();
        tc2_setup_result = DONE;
    }

    public void tearDownTC2() {
        tc2_info = ejb3xStatefulA.tearDown();
        tc2_teardown_result = DONE;
    }


    public void executeTC2() {
        try {
        	tc2_test_result = ejb3xStatefulA.runTest();
            tc2_info = EXECUTED_MSG;
        } catch (Exception e) {
            tc2_info = "[" + e.getMessage() + "]";
            tc2_test_result = FAILED;
        }
    }

    /* ********************************************
     * TC3 EJB2.1 Stateless
     ******************************************** */
    
    public String getTeardownResultTC3() {
        return tc3_teardown_result;
    }

    public void setTeardownResultTC3(String teardownResult) {
        this.tc3_teardown_result = teardownResult;
    }

    public String getSetupResultTC3() {
        return tc3_setup_setUp_result;
    }

    public void setSetupResultTC3(String setupResult) {
        this.tc3_setup_setUp_result = setupResult;
    }

    public String getResultTC3() {
        return tc3_test_result;
    }

    public void setResultTC3(String result) {
        this.tc3_test_result = result;
    }
    
    public String getInfoTC3() {
        return tc3_info;
    }

    public void setInfoTC3(String info) {
        this.tc3_info = info;
    }
    
    public void setupTC3() {
        tc3_info = ejb2xStatelessA.setUp();
        tc3_setup_setUp_result = DONE;
    }

    public void tearDownTC3() {
        tc3_info = ejb2xStatelessA.tearDown();
        tc3_teardown_result = DONE;
    }


    public void executeTC3() {
        try {
        	tc3_test_result = ejb2xStatelessA.runTest();
            tc3_info = EXECUTED_MSG;
        } catch (Exception e) {
            tc3_info = "[" + e.getMessage() + "]";
            tc3_test_result = FAILED;
        }
    }
    
    /* ********************************************
     * TC4 EJB2.1 Stateful 
     ******************************************** */
    
   /* public String getTeardownResultTC4() {
        return tc4_teardown_result;
    }

    public void setTeardownResultTC4(String teardownResult) {
        this.tc4_teardown_result = teardownResult;
    }

    public String getSetupResultTC4() {
        return tc4_setup_result;
    }

    public void setSetupResultTC4(String setupResult) {
        this.tc4_setup_result = setupResult;
    }

    public String getResultTC4() {
        return tc4_test_result;
    }

    public void setResultTC4(String result) {
        this.tc4_test_result = result;
    }
    
    public String getInfoTC4() {
        return tc4_info;
    }

    public void setInfoTC4(String info) {
        this.tc4_info = info;
    }
    
    public void setupTC4() {
        tc4_info = ejb2xStatefulA.setUp();
        tc4_setup_result = DONE;
    }

    public void tearDownTC4() {
        tc4_info = ejb2xStatefulA.tearDown();
        tc4_teardown_result = DONE;
    }


    public void executeTC4() {
        try {
        	tc4_test_result = ejb2xStatefulA.runTest();
            tc4_info = EXECUTED_MSG;
        } catch (Exception e) {
            tc4_info = "[" + e.getMessage() + "]";
            tc4_test_result = FAILED;
        }
    }*/
}