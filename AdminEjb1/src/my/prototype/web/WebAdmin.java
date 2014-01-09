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
package my.prototype.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import my.prototype.api.Ejb1Local;


@WebServlet("/admin")
public class WebAdmin extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @EJB
    Ejb1Local ejb1;
    
    protected UserTransaction utx;
    
    PrintWriter writer;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String country = request.getParameter("country");

            writer = response.getWriter();
            writerHeader();

            writer.println("<h3>Test setup...creating film.</h3>");
            writer.println("<br>");
            ejb1.setUp();

            
            writer.println("<h3>Running Test1...</h3>");
            String result1 = ejb1.runTest1();
            printResult(1, result1);
            
            writeFooter();
            
            ejb1.tearDown();
            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    private void printResult(int testNumber, String result){
        writer.println("<h3>Test " + testNumber + ": " + result + " </h3><br>");
    }

    private void writeFooter() {
        writer.println("</body>");
        writer.println("</html>");
        writer.close();
    }

    private void writerHeader() {
        writer.println("<html>");
        writer.println("<head></head>");
        writer.println("<body>");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }
}
