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
    TestCase ejb_v2;
    
    @EJB(beanName="Ejb1_v3")
    TestCase ejb_v3;

    private String result1;
    private String info1 = "Info...";
    private String setupResult1;
    private String teardownResult1;
    
    private String result2;
    private String info2 = "Info...";
    private String setupResult2;
    private String teardownResult2;

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
        return result1;
    }

    public void setResultTC1(String result) {
        this.result1 = result;
    }
    
    public String getInfoTC1() {
        return info1;
    }

    public void setInfoTC1(String info) {
        this.info1 = info;
    }
    
    public void setupTC1() {
        setupResult1 = ejb_v2.setUp();
        info1 = "Setup done";
    }

    public void tearDownTC1() {
        teardownResult1 = ejb_v2.tearDown();
        info1 = teardownResult1;
        info1 = "Teardown done";
    }


    public void executeTC1() {
        try {
            ejb_v2.runTest();
            result1 = ejb_v2.getResult();
            info1 = "Test executed";
        } catch (Exception e) {
            //e.printStackTrace();
            result1 = "Failed";
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
        return result2;
    }

    public void setResultTC2(String result) {
        this.result2 = result;
    }
    
    public String getInfoTC2() {
        return info2;
    }

    public void setInfoTC2(String info) {
        this.info2 = info;
    }
    
    public void setupTC2() {
        setupResult2 = ejb_v3.setUp();
        info2 = "Setup done";
    }

    public void tearDownTC2() {
        teardownResult2 = ejb_v3.tearDown();
        info2 = teardownResult1;
        info2 = "Teardown done";
    }


    public void executeTC2() {
        try {
            ejb_v3.runTest();
            result2 = ejb_v3.getResult();
            info2 = "Test executed";
        } catch (Exception e) {
            //e.printStackTrace();
            result2 = "Failed";
        }
    }



}