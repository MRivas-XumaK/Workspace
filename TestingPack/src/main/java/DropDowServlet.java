import java.io.IOException;
import java.io.InputStream;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.jackrabbit.commons.JcrUtils;

/**
 * DropdownServlet is the class used for the response of the FileServlet to view the images
 * already stored in the Jackrabbit repository; It uses Base64 Encoding to display the image in
 * HTML, this means that is not necesary to have the path of the image stored in the localhost 
 * machine of the Jackrabbit repository;
 * In this case, it is not necesary to use the MultipartConfig and by this its not restricted 
 * to use the doPost method.
 * 
 * @author Mario Rolando Rivas 
 * 
 */
@WebServlet(urlPatterns = {"/DropDowServlet"})
public class DropDowServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * 
     * In this method also prints the html for the web browser as the response parameter in the
     * docType String. It is also setting session in the jackrabbit repository http://localhost:8080/rmi
     * and starts using data from the repository with the session stablished. This data is then printed
     * in the html docType as part of the application.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws javax.jcr.RepositoryException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RepositoryException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String title ="Select a file to display<br>";
        
        
        String url = "http://localhost:8080/rmi";
        Repository repository = JcrUtils.getRepository(url);
        SimpleCredentials creds = new SimpleCredentials("admin","admin".toCharArray());
        Session jcrSession = repository.login(creds, "default");
        
        Node root = jcrSession.getRootNode();
        Node ImagesNode = root.getNode("Images");
        String OptList = listing(ImagesNode);
        
        
        
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "+
               "transitional//en\">\n";
        out.println(docType + "<html>\n"+
        "<head><title>"+ title +"</title></head>\n"+
        "<body bgcolor=\"#f0f0f0\">\n"+
            "<h1 align=\"center\">"+ title +"</h1>\n"+
            "<form action=\"DropDowServlet\" method=\"POST\">" +
                "<center><select name =\"DropList\" onchange=\"this.form.submit()\">" + OptList+
                "</select></center>" +
            "</form>");
        
        /**
         * Preparing data from the repository to be encoded  in Base64 Encoding of java 
         * so then it can be used in the <img src=data:image/jpeg;base64 /> tag and concat it in
         * the HTML code already generated.
         */
        
        String file = request.getParameter("DropList");
        int read = 0;
        InputStream TheFile = impression(file,ImagesNode);
        byte[] bytes = new byte[TheFile.available()];
        TheFile.read(bytes);
        
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);
        System.out.println("encodedBytes: "+ encodedString);
        PrintWriter writeOut= response.getWriter();
        
        out.println(docType + 
                "<center><img src=\"data:image/jpeg;base64,"+ encodedString +"\" hieght=20% width=20%/></center>"+
                "<form action=\"index.jsp\">\n" +
                    "<center><input type=\"submit\" value=\"Start\"></center>\n" +
                "</form>" +
                "</body></html>");
    }
    
    /**
     * Enlists all the image file names located in the Images node of the Jackrabbit repository on the DropDown List
     * element of the HTML code and returns them as a single String.
     * 
     * @param n     The node that i want to locate and enlist the files.
     * @return
     * @throws      RepositoryException 
     */
    public String listing(Node n) throws RepositoryException{
       String output="<option selected=\"selected\" disabled=\"disabled\">Select a picture</option>";
       String name;
       Property aux;
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
        return output;

    }
    
    /**
     * Prepares the image in a InputStream and verifies that, if there is no image in the request, then place 
     * the first image available as an InputStream; Else if the NodeName is not null, search the file and place 
     * it in the InputStream and return it.
     * 
     * @param NodeName      Name of the file recived in the DropDownList image checked.
     * @param n             The node in which i want to obtain the file.
     * @return
     * @throws RepositoryException 
     */
    
    public InputStream impression(String NodeName, Node n) throws RepositoryException{
       String name;
       Property aux;
       InputStream file= null;
       PropertyIterator auxiliar1 = n.getProperties();
       if(auxiliar1.hasNext() && (NodeName != null)){
           while(auxiliar1.getPosition() < auxiliar1.getSize()){
               aux = auxiliar1.nextProperty();
               name = aux.getName();
               if(!name.equals("jcr:primaryType")){
                    if(name.equals(NodeName)){
                        file = n.getProperty(NodeName).getStream();
                    }
               }
           }
       }else if(NodeName == null){
           aux = auxiliar1.nextProperty();
           file = n.getProperty(aux.getName()).getStream();
       }
        return file;

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
            Logger.getLogger(DropDowServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DropDowServlet.class.getName()).log(Level.SEVERE, null, ex);
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
