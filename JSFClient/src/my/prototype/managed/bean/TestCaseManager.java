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

    private boolean flush = false;
    private String tcId = "local";
    private String result;

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the flush
     */
    public boolean isFlush() {
        return flush;
    }

    /**
     * @param flush
     *            the flush to set
     */
    public void setFlush(final boolean flush) {
        this.flush = flush;
    }

    public void result(){
        ejb1.getResult();
    }
    
    public void execute() {
        System.out.println("Called execute");
        System.out.println("flush:" + this.flush);
        System.out.println("tcId:" + this.tcId);

        //if (this.tcId.equalsIgnoreCase("local")) {
            //ejb1.runLocalTest(this.flush);
        //} else {
            try {
                ejb1.runTest();
                result = ejb1.getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }

    public void setup() {
        System.out.println("Called setup");
        //result = "";
        ejb1.setUp();
    }

    public void tearDown() {
        System.out.println("Called tear down");
        //result = "";
        ejb1.tearDown();
    }

    /**
     * @return the tcId
     */
    public String getTcId() {
        return tcId;
    }

    /**
     * @param tcId
     *            the tcId to set
     */
    public void setTcId(final String tcId) {
        this.tcId = tcId;
    }

}