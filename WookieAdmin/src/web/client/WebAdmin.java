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
package web.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import prototype.ejb.api.StatelessEjb1Remote;

@WebServlet("/admin")
public class WebAdmin extends HttpServlet{

    private static final long serialVersionUID = 1L;
    
    @EJB(lookup=StatelessEjb1Remote.REMOTE_JNDI, beanInterface=StatelessEjb1Remote.class)
    StatelessEjb1Remote ejb1;
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //String name = request.getParameter("name");
        // String localGreeting;
        
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head></head>");
        writer.println("<body>");
        writer.println("<h1>" + "Starting..." + "</h1>");
        
        writer.println("<h2>creating film...</h2>");
        ejb1.createStartWars();
       
        writer.println("<h2>persisting film...</h2>");
        ejb1.createStartWars();
        
        writer.println("<h2>Fetching film details</h2><br><br>");
        writer.println(ejb1.getStarWars());
        writer.println("</body>");
        writer.println("</html>");
        writer.close();
        
        ejb1.createStartWars();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }
}
