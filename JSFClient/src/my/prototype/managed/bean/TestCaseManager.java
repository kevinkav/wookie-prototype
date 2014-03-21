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
package my.prototype.managed.bean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import my.prototype.test.api.TestCase;



@ManagedBean(name = "tc")
public class TestCaseManager {

    @EJB(beanName="Ejb1_v2")
    TestCase ejb1_v2;
    
    @EJB(beanName="Ejb1_v3")
    TestCase ejb1_v3;

    private static final String SETUP_MSG = "[Setup Test]";
    private static final String EXECUTED_MSG = "[Executed Test]";
    private static final String TEARDOWN_MSG = "[Teardown Test]";
    private static final String DONE = "Done";
    
    private String testResult1;
    private String setupResult1;
    private String teardownResult1;
    private String info1 = "";
    
    private String testResult2;
    private String setupResult2;
    private String teardownResult2;
    private String info2 = "";

    /* ********************************************
     * TC1 - Using JTS 
     ******************************************** */
    public String getTeardownResultTC1() {
        return teardownResult1;
    }

    public void setTeardownResultTC1(String teardownResult) {
        this.teardownResult1 = teardownResult;
    }

    public String getSetupResultTC1() {
        return setupResult1;
    }

    public void setSetupResultTC1(String setupResult) {
        this.setupResult1 = setupResult;
    }

    public String getResultTC1() {
        return testResult1;
    }

    public void setResultTC1(String result) {
        this.testResult1 = result;
    }
    
    public String getInfoTC1() {
        return info1;
    }

    public void setInfoTC1(String info) {
        this.info1 = info;
    }
    
    public void setupTC1() {
        info1 = ejb1_v2.setUp();
        setupResult1 = DONE;
    }

    public void tearDownTC1() {
        info1 = ejb1_v2.tearDown();
        teardownResult1 = DONE;
    }


    public void executeTC1() {
        try {
            ejb1_v2.runTest();
            testResult1 = ejb1_v2.getResult();
            info1 = EXECUTED_MSG;
        } catch (Exception e) {
            String ERROR_MSG = "[" + e.getMessage() + "]";
            testResult1 = ERROR_MSG;
            info1 = ERROR_MSG;
        }
    }

    
    /* ********************************************
     * TC2 - Using NON JTS 
     ******************************************** */
    public String getTeardownResultTC2() {
        return teardownResult2;
    }

    public void setTeardownResultTC2(String teardownResult) {
        this.teardownResult2 = teardownResult;
    }

    public String getSetupResultTC2() {
        return setupResult2;
    }

    public void setSetupResultTC2(String setupResult) {
        this.setupResult2 = setupResult;
    }

    public String getResultTC2() {
        return testResult2;
    }

    public void setResultTC2(String result) {
        this.testResult2 = result;
    }
    
    public String getInfoTC2() {
        return info2;
    }

    public void setInfoTC2(String info) {
        this.info2 = info;
    }
    
    public void setupTC2() { 
        info2 = ejb1_v3.setUp();
        setupResult2 = DONE;
    }

    public void tearDownTC2() {
        info2 = ejb1_v3.tearDown();
        teardownResult2 = DONE;
    }


    public void executeTC2() {
        try {
            ejb1_v3.runTest();
            testResult2 = ejb1_v3.getResult();
            info2 = EXECUTED_MSG;
        } catch (Exception e) {
            String ERROR_MSG = "[" + e.getMessage() + "]";
            testResult2 = ERROR_MSG;
            info2 = ERROR_MSG;
        }
    }



}