import java.io.InputStream;
import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mario Rivas
 */
public class DropDowServletTest {
    
    public DropDowServletTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of processRequest method, of class DropDowServlet.
     */
    @Test
    public void testProcessRequest() throws Exception {
        System.out.println("processRequest");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        DropDowServlet instance = new DropDowServlet();
        instance.processRequest(request, response);
    }

    /**
     * Test of listing method, of class DropDowServlet.
     */
    @Test
    public void testListing() throws Exception {
        System.out.println("listing");
        Node n = null;
        DropDowServlet instance = new DropDowServlet();
        String expResult = "";
        String result = instance.listing(n);
    }

    /**
     * Test of impression method, of class DropDowServlet.
     */
    @Test
    public void testImpression() throws Exception {
        System.out.println("impression");
        String NodeName = "";
        Node n = null;
        DropDowServlet instance = new DropDowServlet();
        InputStream expResult = null;
        InputStream result = instance.impression(NodeName, n);
        assertEquals(expResult, result);
    }

    /**
     * Test of doGet method, of class DropDowServlet.
     */
    @Test
    public void testDoGet() throws Exception {
        System.out.println("doGet");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        DropDowServlet instance = new DropDowServlet();
        instance.doGet(request, response);
    }

    /**
     * Test of doPost method, of class DropDowServlet.
     */
    @Test
    public void testDoPost() throws Exception {
        System.out.println("doPost");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        DropDowServlet instance = new DropDowServlet();
        instance.doPost(request, response);
    }
    
}
