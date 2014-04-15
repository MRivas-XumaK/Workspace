/**
 *
 * @author Mario Rivas
 */
package com.testing.mavenhelloservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Installer implements BundleActivator {
    
    @Override
    public void start(BundleContext context) throws Exception {
        String userName = context.getProperty("user.name");
        System.out.println("Maven Hello Service: Started OSGi Bundle");
        System.out.println("User Name: " + userName);
    }
    
    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Maven Hello Service: Stopped OSGi bundle");
    }
    
}
