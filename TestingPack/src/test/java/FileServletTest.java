/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import javax.jcr.Node;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
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
 * @author xumak-pc
 */
public class FileServletTest {
    
    public FileServletTest() {
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
     * Test of processRequest method, of class FileServlet.
     */
    @Test
    public void testProcessRequest() throws Exception {
        System.out.println("processRequest");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        FileServlet instance = new FileServlet();
        instance.processRequest(request, response);

    }

    /**
     * Test of run method, of class FileServlet.
     */
    @Test
    public void testRun() throws Exception {
        System.out.println("run");
        InputStream file = null;
        String fileName = "";
        FileServlet instance = new FileServlet();
        instance.run(file, fileName);
    }

    /**
     * Test of impression method, of class FileServlet.
     */
    @Test
    public void testImpression() throws Exception {
        System.out.println("impression");
        Node n = null;
        FileServlet instance = new FileServlet();
        String expResult = "";
        String result = instance.impression(n);
        assertEquals(expResult, result);
    }

    /**
     * Test of fileExt method, of class FileServlet.
     */
    @Test
    public void testFileExt() {
        System.out.println("fileExt");
        String file = "";
        FileServlet instance = new FileServlet();
        String expResult = "";
        String result = instance.fileExt(file);
        assertEquals(expResult, result);
    }

    /**
     * Test of fileName method, of class FileServlet.
     */
    @Test
    public void testFileName() {
        System.out.println("fileName");
        String file = "";
        FileServlet instance = new FileServlet();
        String expResult = "";
        String result = instance.fileName(file);
        assertEquals(expResult, result);
    }

    /**
     * Test of doGet method, of class FileServlet.
     */
    @Test
    public void testDoGet() throws Exception {
        System.out.println("doGet");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        FileServlet instance = new FileServlet();
        instance.doGet(request, response);
    }

    /**
     * Test of doPost method, of class FileServlet.
     */
    @Test
    public void testDoPost() throws Exception {
        System.out.println("doPost");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        FileServlet instance = new FileServlet();
        instance.doPost(request, response);
    }
    
}
