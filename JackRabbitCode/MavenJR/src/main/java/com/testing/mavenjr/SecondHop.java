/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.testing.mavenjr;

import java.io.FileInputStream;
import java.io.InputStream;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.core.TransientRepository;

/**
 * Second hop example. Stores, retrieves, and removes example content.
 */
public class SecondHop {

    /**
     * The main entry point of the example application.
     * 
     * @param args
     *            command line arguments (ignored)
     * @throws Exception
     *             if an error occurs
     */
    public static void main(String[] args) throws Exception {
        Repository testrep = new TransientRepository();
        Session session = testrep.login(new SimpleCredentials("admin", "admin".toCharArray()));
        InputStream file = new FileInputStream("/home/xumak-pc/Pictures/jackrabbit.png");
        InputStream file2 = new FileInputStream("/home/xumak-pc/Pictures/coloredjb.jpg");
        try {
            Node root = session.getRootNode();

            // Store content
            Node hello = root.addNode("hello");
            Node world = hello.addNode("world");
            hello.setProperty("file", file2);
            world.setProperty("message", file);
            session.save();

            // Retrieve content
            /*Node node = root.getNode("hello/world");
            System.out.println(node.getPath());
            InputStream newstream = node.getProperty("message").getStream();
            int i =0;
            while((i=newstream.read()) != -1){
                System.out.print(i + " ");
            }*/

            // Remove content
            root.getNode("hello").remove();
            session.save();
        } finally {
            session.logout();
        }
    }

}
