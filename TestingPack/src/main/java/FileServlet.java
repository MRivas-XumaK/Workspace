/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.jackrabbit.commons.JcrUtils;

/**
 *
 * @author xumak-pc
 */
@WebServlet(name= "FileServlet",urlPatterns = {"/FileServlet"})
@MultipartConfig
public class FileServlet extends HttpServlet {
     private final static Logger LOGGER = 
            Logger.getLogger(FileServlet.class.getCanonicalName());
     String ImFiles, MusicFiles, DcsFIles, UnknwnFiles;
     String path = "/home/xumak-pc/apache-tomcat-7.0.41/webapps/ROOT/";
     String host = "http://localhost:8084/";
     
     /*public final Repository repository =
            new RMIRemoteRepository("//localhost/jackrabbit.repository");*/
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RepositoryException {
        //response.setContentType("text/html;charset=UTF-8");
            // Create path components to save the file
         //Directorio a donde lo voy a poner
        final Part filePart = request.getPart("file");
        String filename = getFileName(filePart);
          
        //PrintWriter writeOut= response.getWriter();
        String docType;
        String title ="Displaying images<br>";
        
        InputStream filecontent = null;
        InputStream emptyFIle = null;
        OutputStream out = null;
        
        filecontent = filePart.getInputStream();
        emptyFIle = filePart.getInputStream();
        
        run(filecontent,filename);
        out = new FileOutputStream(new File(path + File.separator
                    + filename));
        int read = 0;
            byte[] bytes = new byte[emptyFIle.available()];    //image size

            while ((read = emptyFIle.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            response.setContentType("image/jpeg");
            response.setContentLength(bytes.length);
            System.out.println(bytes);
            response.getOutputStream().write(bytes);
        /*docType = "<!doctype html public \"-//w3c//dtd html 4.0 "+
                   "transitional//en\">\n";
            writeOut.println(docType + "<html>\n"+
            "<head><title>"+ title +"</title></head>\n"+
            "<body bgcolor=\"#f0f0f0\">\n"+
            "<h1 align=\"center\">"+ title +"</h1>\n"+
            "<ul>\n"+ ImFiles+"\n"+
            "</ul>\n"+"</body></html>"); */
        
    }
    
    public void run(InputStream file,String fileName) throws RepositoryException, FileNotFoundException, IOException{
        String url = "http://localhost:8080/rmi";
        Repository repository = JcrUtils.getRepository(url);
        SimpleCredentials creds = new SimpleCredentials("admin","admin".toCharArray());
        Session jcrSession = repository.login(creds, "default");
        
        Node root = jcrSession.getRootNode();
        Node ImagesNode = root.getNode("Images");
        Node MusicNode = root.getNode("Music");
        Node DocsNode = root.getNode("Documents");
        Node Unknown = root.getNode("Unknown");
        
        String nameOfFile = fileName(fileName);
        String extOfFIle = fileExt(fileName);
        System.out.println("Nodes created");
        if(extOfFIle.equals(".png") || extOfFIle.equals(".jpg")){
           System.out.println("Pictures");
           ImagesNode.setProperty(fileName, file);
           jcrSession.save();
        }else if(extOfFIle.equals(".mp3") || extOfFIle.equals(".wma") || extOfFIle.equals(".wav")){
            System.out.println("Music");
            MusicNode.setProperty(fileName, file);
            jcrSession.save();
        }else if(extOfFIle.equals(".txt") || extOfFIle.equals(".doc") || extOfFIle.equals(".docx") || extOfFIle.equals(".pdf") ){
             System.out.println("Documents");
            DocsNode.setProperty(fileName, file);
            jcrSession.save();
        }else{
             System.out.println("Unknown");
            Unknown.setProperty(fileName, file);
            jcrSession.save();
        }
        // preorder(ImagesNode, fileName);
        ImFiles = impression(ImagesNode);
        MusicFiles = impression(MusicNode);
        DcsFIles = impression(DocsNode);
        UnknwnFiles=impression(Unknown);
        jcrSession.logout();
        
      }
     public String impression(Node n) throws RepositoryException{
       String auxiliar = "";
       String output = n.getName() + ": ";
       Property aux;
       PropertyIterator auxiliar1 = n.getProperties();
       if(auxiliar1.hasNext()){
           while(auxiliar1.getPosition() < auxiliar1.getSize()){
               aux = auxiliar1.nextProperty();
               //aux1.next();
               auxiliar = aux.getName();
               //System.out.println(auxiliar);
               if(!auxiliar.equals("jcr:primaryType")){
                    output = output + "<br><img src=\"" + host +auxiliar +  "\" width = 50 height= 50><br>";
               }
           }
       }
        return output;

    }
     
    public String fileExt(String file){
        String fileExt = null;
        if(file.contains(".")){
            fileExt = file.substring(file.indexOf("."), file.length());
            
        }else{
            System.out.println(file + "Is not a file");
        }
        return fileExt;  
    }
    
    public String fileName(String file){
        String fileName = null;
        if(file.contains(".")){
            fileName = file.substring(0, file.indexOf("."));
        }else{
            System.out.println(file + "Is not a file");
        }
        return fileName;
    }
    
    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
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
         try {
             processRequest(request, response);
         } catch (RepositoryException ex) {
             Logger.getLogger(FileServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
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
         try {
             processRequest(request, response);
         } catch (RepositoryException ex) {
             Logger.getLogger(FileServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
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
