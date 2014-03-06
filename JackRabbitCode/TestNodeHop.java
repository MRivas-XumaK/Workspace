/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.jackrabbit.firsthops;

import java.io.FileInputStream;
import java.io.InputStream;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.core.TransientRepository;

/**
 *
 * @author xumak-pc
 */
public class TestNodeHop {
    public static TestNodeHop nodeHop;
    public TestNodeHop(){
        nodeHop = this;
    }
    
    public static void main(String[] args) throws Exception {
        Repository repository2 = new TransientRepository();
        Session ses = repository2.login(
                new SimpleCredentials("mario", "mario".toCharArray()));
        InputStream file = new FileInputStream("/home/xumak-pc/Pictures/jackrabbit.png");
        InputStream file2 = new FileInputStream("/home/xumak-pc/Music/Youtopia.mp3");
        try{
            Node root = ses.getRootNode();
            // Store content 
            Node node1 = root.addNode("node1"); 
            node1.setProperty("audio", file2);
            ses.save();
                
            
            // Retrieve content 
            ///Node node = root.getNode("node1"); 
            //System.out.println(node1.getPath()); 
            //System.out.println(node1.getProperty("message").getString());

            // Remove content 
            root.getNode("node1").remove(); 
            ses.save();
            

            
            
        } finally{
            ses.logout();
        }
        
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
    
    public static boolean nodePathExists(Node parent, Node node) throws RepositoryException{
        boolean state=false;
        String nodePath = nodeHop.preorder(node);
        String parentPath = nodeHop.preorder(parent);
        if(parentPath.contains(nodePath)){
            System.out.println("WARNING: " + nodePath + "is already in" + parentPath);
            return state;
        }else{
            state = true;
        }
            return state;
        }

}

