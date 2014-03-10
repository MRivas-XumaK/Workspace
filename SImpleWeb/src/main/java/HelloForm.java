/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author xumak-pc
 */
@WebServlet(urlPatterns = {"/HelloForm"})
public class HelloForm extends HttpServlet {
    // Method to handle GET method request.
       @Override
       public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
        {
          // Set response content type
            response.setContentType("text/html");
            PrintWriter out= response.getWriter();
            String title ="Using GET Method to Read Form Data";
            String docType;
            String path = request.getParameter("first_name");
            String name = request.getParameter("last_name");
            docType = "<!doctype html public \"-//w3c//dtd html 4.0 "+
                   "transitional//en\">\n";
            out.println(docType + "<html>\n"+
            "<head><title>"+ title +"</title></head>\n"+
            "<body bgcolor=\"#f0f0f0\">\n"+
            "<h1 align=\"center\">"+ title +"</h1>\n"+
            "<ul>\n"+
            " <li><b>First Name</b>: "
            + path+"\n"+
            " <li><b>Last Name</b>: "
            + name+"\n"+
            "</ul>\n"+"</body></html>"); 
        }
        // Method to handle POST method request.
       @Override
        public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
                doGet(request, response);
        }

}
