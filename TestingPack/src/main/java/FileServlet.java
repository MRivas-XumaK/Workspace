/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
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
     /*public final Repository repository =
            new RMIRemoteRepository("//localhost/jackrabbit.repository");*/
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RepositoryException {
        response.setContentType("text/html;charset=UTF-8");
            // Create path components to save the file
        final String path = request.getParameter("destination"); //Directorio a donde lo voy a poner
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);
        
        LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", 
                    new Object[]{fileName, path});
        
        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();
        
        try {
            /*out = new FileOutputStream(new File(path + File.separator
                    + fileName));*/
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];    //image size
            run(filecontent, fileName);
            writer.println("New file " + fileName + " created at " + path);
            System.out.println("Filename is: " + fileName);
            /*while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }*/
            writer.println(filecontent.toString());
            
            LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", 
                    new Object[]{fileName, path});
        }catch (FileNotFoundException fne) {
            writer.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.");
            writer.println("<br/> ERROR: " + fne.getMessage());

            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", 
                    new Object[]{fne.getMessage()});
        }finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
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
        
        if(extOfFIle.equals(".png") || extOfFIle.equals(".jpg")){
           System.out.println("Pictures");
           ImagesNode.setProperty(nameOfFile, file);
           jcrSession.save();
        }else if(extOfFIle.equals(".mp3") || extOfFIle.equals(".wma") || extOfFIle.equals(".wav")){
            System.out.println("Music");
            MusicNode.setProperty(nameOfFile, file);
            jcrSession.save();
        }else if(extOfFIle.equals(".txt") || extOfFIle.equals(".doc") || extOfFIle.equals(".docx") || extOfFIle.equals(".pdf") ){
             System.out.println("Documents");
            DocsNode.setProperty(nameOfFile, file);
            jcrSession.save();
        }else{
             System.out.println("Unknown");
            Unknown.setProperty(nameOfFile, file);
            jcrSession.save();
        }
        //System.out.println("Login successful, workspace: " + jcrSession.getWorkspace());

        jcrSession.logout();
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
    
    public static String preorder(Node n) throws RepositoryException{
       NodeIterator avaNodes;
       avaNodes = n.getNodes(); 
       String nodePath = "";
       if(!n.hasNodes()){
            return nodePath;
        }else{
            while(avaNodes.getPosition() != avaNodes.getSize()){
                n = avaNodes.nextNode();
                nodePath = nodePath + n.getPath();
                System.out.println(nodePath);
                preorder(n);
            }
            return nodePath;
        }
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
