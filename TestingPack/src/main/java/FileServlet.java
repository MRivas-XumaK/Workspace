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
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.codec.binary.Base64;
import org.apache.jackrabbit.commons.JcrUtils;

/**
 * The FileServlet class is the class used to receive the file from the jsp/HTML
 * page and store it in the nodes of the Jackrabbit repository of the actual session;
 * It is restricted to use the doPost methods due to the MultipartConfig dependancies;
 * 
 * The MultipartConfig is a library used to manage the file header and body correctly
 * so it is stored in the Nodes of the repository correctly; It uses other methods for
 * the correct usage of this library.
 * 
 * 
 * @author Mario Rolando Rivas
 */
@WebServlet(name= "FileServlet",urlPatterns = {"/FileServlet"})
@MultipartConfig
public class FileServlet extends HttpServlet {
     private final static Logger LOGGER = 
            Logger.getLogger(FileServlet.class.getCanonicalName());
     String OptList;
     String path = "/home/xumak-pc/apache-tomcat-7.0.41/webapps/ROOT/";
     String host = "http://localhost:8084/";
    
     /**
      * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
      * methods;
      * It displays the image selected in the JSP page with the docType String
      * with HTML code in the the DrodDownServlet; Part object is used to 
      * separate the file parts with the name of the File, and the file content;
      * It uses a run method, wich saves the image in the correct node of the 
      * repository.
      * 
      * @param request      servlet request
      * @param response     servlet response
      * @throws             ServletException
      * @throws             IOException
      * @throws             RepositoryException 
      */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RepositoryException {
        if(response != null){
             PrintWriter writeOut= response.getWriter();
            
            if(request != null){
                final Part filePart = request.getPart("file");
                String filename = getFileName(filePart);

                String docType;
                String title ="Select a file to display<br>";

                InputStream filecontent = filePart.getInputStream();;
                InputStream emptyFIle = filePart.getInputStream();;
                OutputStream out = null;
                try{
                    run(filecontent,filename);
                    out = new FileOutputStream(new File(path + File.separator
                            + filename));
                    int read = 0;
                    byte[] bytes = new byte[emptyFIle.available()];    //image size

                    while ((read = emptyFIle.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    byte[] encoded = Base64.encodeBase64(bytes);
                    String encodedString = new String(encoded);
                    System.out.println("encodedBytes: "+ encodedString);



                    /*response.setContentType("image/jpeg");
                    response.setContentLength(bytes.length);
                    System.out.println(bytes);
                    response.getOutputStream().write(bytes);*/
                    docType = "<!doctype html public \"-//w3c//dtd html 4.0 "+
                           "transitional//en\">\n";
                    writeOut.println(docType + "<html>\n"+
                    "<head><title>"+ title +"</title></head>\n"+
                    "<body bgcolor=\"#f0f0f0\">\n"+
                        "<h1 align=\"center\">"+ title +"</h1>\n"+
                        "<form action=\"DropDowServlet\" method=\"POST\">" +
                            "<center><select name =\"DropList\" onchange=\"this.form.submit()\">" + OptList+
                            "</select></center>" +
                        "</form>" +
                        "<center><img src=\"data:image/jpeg;base64,"+ encodedString +"\" hieght=20% width=20%/></center>"+
                        "<form action=\"index.jsp\">\n" +
                            "<center><input type=\"submit\" value=\"Start\"></center>\n" +
                        "</form>" +
                    "</body></html>");
                } catch(FileNotFoundException fe){
                    System.err.println("ERROR: Found empty file to upload.");
                    
                    docType = "<!doctype html public \"-//w3c//dtd html 4.0 "+
                       "transitional//en\">\n";
                    
                    writeOut.println(docType + "ERROR: File Upload failed."
                    + "You did not specify a file to upload." 
                    + "<form action=\"index.jsp\">\n" +
                        "<center><input type=\"submit\" value=\"Start\"></center>\n" +
                      "</form>");
                }
                
            }else{
                writeOut.println("<br/> ERROR: ");
                writeOut.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location. +"
                    + "<form action=\"index.jsp\">\n" +
                        "<center><input type=\"submit\" value=\"Start\"></center>\n" +
                      "</form>");
                System.out.println("ERROR: Request found as null");
            }
        }else{
            System.out.println("ERROR: Response detected as null, or data is corrupted");
        }
        
        
    }
    
    
    /**
     * The run method is used to start a session in the Jackrabbit repository and stores
     * the data recieved in the request as a file; It also verifies that if the Node of the
     * determined file extension exists, then store the file in that node, or else if the extension
     * of the file is unkown, store it in an Unkown node;
     * Also, if the file is an Image, generates a string with the "<image src ="InputFile"/>" of the
     * file that it stored in the node.
     * 
     * @param file      
     * @param fileName
     * @throws RepositoryException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void run(InputStream file,String fileName) throws RepositoryException, FileNotFoundException, IOException{
        try{
            String url = "http://localhost:8080/rmi";
            Repository repository = JcrUtils.getRepository(url);
            SimpleCredentials creds = new SimpleCredentials("admin","admin".toCharArray());
            Session jcrSession = repository.login(creds, "default");

            Node root = jcrSession.getRootNode();
            Node ImagesNode = root.getNode("Images");
            Node MusicNode = root.getNode("Music");
            Node DocsNode = root.getNode("Documents");
            Node Unknown = root.getNode("Unknown");

            if(!fileName.equals("")){
                String nameOfFile = fileName(fileName);
                String extOfFIle = fileExt(fileName);
                System.out.println("Nodes created");
                if(file != null){
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
                    OptList = impression(ImagesNode);
                    System.out.println(OptList);
                    jcrSession.logout();
            }else{
                System.err.println("ERROR: InputStream found as null. File might be corrrupted at Part method call or it is not a file.");
            }
          }else{
            System.err.println("ERROR: File without a name or not in a valid format.");
          }
        }catch(RepositoryException re){
            System.err.println("ERROR: Could not acces the repository. The repository might be down.");
        }
        
        
      }
    
     /**
      * Method used to fill the string of each option of the DropDownList with the files
      * that it finds in the Images node and place it in the string used to print the HTML
      * code.
      * 
      * @param n        Node in wich are all the image files stored.
      * @return         string with the options of the files that are stored in this node
      * @throws         RepositoryException 
      */
     public String impression(Node n) throws RepositoryException{
       String output="";
       String name;
       Property aux;
       if(n != null){
        PropertyIterator auxiliar1 = n.getProperties();
        if(auxiliar1.hasNext()){
            while(auxiliar1.getPosition() < auxiliar1.getSize()){
                aux = auxiliar1.nextProperty();
                name = aux.getName();
                if(!name.equals("jcr:primaryType")){
                     output = output + "<option value=\"" + name +  "\">"+ name + "</option>" ;
                }
            }
        }
       }else{
           System.out.println("ERROR: Node not found in the repository.");
       }
       return output;

    }
    /**
     * Gets extension of the String file recived
     * 
     * @param file      String that has the complete name of the file; i.e "file.jpg".
     * @return          String with the extension of the file; i.e ".jpg"
     */ 
    public String fileExt(String file){
        String fileExt = "";
        if(file.contains(".") && !file.equals("")){
            fileExt = file.substring(file.indexOf("."), file.length());
            
        }else{
            System.out.println(file + "Is not a file");
        }
        return fileExt;  
    }
    
    /**
     * Gets the name of the String file recived
     * @param file      String that ahs the complete name of the file; i.e "file.jpg"
     * @return          String with the name of the file; i.e "file"
     */
    public String fileName(String file){
        String fileName = "";
        if(file.contains(".") && !file.equals("")){
            fileName = file.substring(0, file.indexOf("."));
        }else{
            System.out.println(file + "Is not a file");
        }
        return fileName;
    }
    
    /**
     * Gets the file of the file recived as the request from the JSP 
     * and returns the full name of the file.
     * 
     * @param part      Full file recived from the request.
     * @return          String with the full name of the file.
     */
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
}
