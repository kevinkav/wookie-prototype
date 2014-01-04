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
package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import my.prototype.api.Ejb3Remote;


@WebServlet("/admin2")
public class WebAdmin2 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @EJB
    Ejb3Remote ejb3;
    
    protected UserTransaction utx;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String country = request.getParameter("country");

            PrintWriter writer = response.getWriter();
            writerHeader(writer);

            writer.println("<h3>Kicking EJB3...</h3>");
            writer.println("<br>");
            
            ejb3.kickEjb3("WebAdmin2");

            writeFooter(writer);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void writeFooter(PrintWriter writer) {
        writer.println("</body>");
        writer.println("</html>");
        writer.close();
    }

    private void writerHeader(PrintWriter writer) {
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