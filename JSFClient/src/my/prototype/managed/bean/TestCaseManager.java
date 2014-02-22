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

import my.prototype.api.Ejb1Local;


@ManagedBean(name = "tc")
public class TestCaseManager {

    @EJB
    Ejb1Local ejb1;

    private String result;
    private String info = "Info...";
    private String setupResult;
    private String teardownResult;


    public String getTeardownResult() {
        return teardownResult;
    }

    public void setTeardownResult(String teardownResult) {
        this.teardownResult = teardownResult;
    }

    public String getSetupResult() {
        return setupResult;
    }

    public void setSetupResult(String setupResult) {
        this.setupResult = setupResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    public void setup() {
        setupResult = ejb1.setUp();
        info = "Setup done";
    }

    public void tearDown() {
        teardownResult = ejb1.tearDown();
        info = teardownResult;
        info = "Teardown done";
    }


    public void execute() {
        try {
            result = ejb1.runTest();
            info = "Test executed";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}