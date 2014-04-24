/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author xumak-pc
 */

@WebServlet(urlPatterns = {"/VideoTestServlet"})
@MultipartConfig
public class VideoTestServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        final Part filePart = request.getPart("file");
        String filename = getFileName(filePart);
        InputStream filecontent = filePart.getInputStream();
        byte[] bytes = new byte[filecontent.available()];
        filecontent.read(bytes);
        
        /*OutputStream PDFprint = response.getOutputStream();
        PDFprint.write(bytes);
        if(PDFprint != null){
            PDFprint.close();
        }*/
        
        
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);
        System.out.println(encodedString);
        String title = "Video Servlet";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "+
                           "transitional//en\">\n";
        
        out.println(docType + "<html>\n"+
                    "<head><title>"+ title +"</title></head>\n"+
                    "<body bgcolor=\"#f0f0f0\">\n"+
                        "<h1 align=\"center\">"+ title +"</h1>\n"+
                        "<video width=\"320\" hieght=\"240\" controls>" +
                        "<source type=\"video/webm\" src=\"data:video/webm;base64," + encodedString+ "\">" +
                        "Your browser does not support the video element" +
                        "</video>" + 
                    "</body></html>");
        
        
        
        
      
    }
    
    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
