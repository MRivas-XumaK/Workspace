

/*
 * Apache Felix OSGi tutorial.
**/

package com.testing.tut3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.osgi.framework.Bundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import tut2.example.service.DictionaryService;

/**
 * This class implements a bundle that uses a dictionary
 * service to check for the proper spelling of a word by
 * check for its existence in the dictionary. This bundle
 * uses the first service that it finds and does not monitor
 * the dynamic availability of the service (i.e., it does not
 * listen for the arrival or departure of dictionary services).
 * When starting this bundle, the thread calling the start()
 * method is used to read words from standard input. You can
 * stop checking words by entering an empty line, but to start
 * checking words again you must stop and then restart the bundle.
**/
public class Activator implements BundleActivator, BundleListener
{
    /**
     * Implements BundleActivator.start(). Queries for
     * all available dictionary services. If none are found it
     * simply prints a message and returns, otherwise it reads
     * words from standard input and checks for their existence
     * from the first dictionary that it finds.
     * (NOTE: It is very bad practice to use the calling thread
     * to perform a lengthy process like this; this is only done
     * for the purpose of the tutorial.)
     * @param context the framework context for the bundle.
    **/
    public void start(BundleContext context) throws Exception
    {
        // Query for all service references matching any language.
        ServiceReference[] refs = context.getServiceReferences(
            DictionaryService.class.getName(), "(Language=*)");
        
        context.addBundleListener(this);
        long bundleID = context.getBundle().getBundleId();
        System.out.println("The ID of the Tut3 Bundle is:" + bundleID);
        
        if (refs != null)
        {
            try
            {
                System.out.println("Enter a blank line to exit.");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String word = "";

                // Loop endlessly.
                while (true)
                {
                    // Ask the user to enter a word.
                    System.out.print("Enter word: ");
                    word = in.readLine();

                    // If the user entered a blank line, then
                    // exit the loop.
                    if (word.length() == 0)
                    {
                        this.stop(context);
                        break;
                    }

                    // First, get a dictionary service and then check
                    // if the word is correct.
                    DictionaryService dictionary =
                        (DictionaryService) context.getService(refs[0]);
                    if (dictionary.checkWord(word))
                    {
                        System.out.println("Correct.");
                    }
                    else
                    {
                        System.out.println("Incorrect.");
                    }

                    // Unget the dictionary service.
                    context.ungetService(refs[0]);
                }
            } catch (IOException ex) { 
                System.out.println("ERROR: "+ ex);
            }
        }
        else
        {
            System.out.println("Couldn't find any dictionary service...");
        }
    }

    /**
     * Implements BundleActivator.stop(). Does nothing since
     * the framework will automatically unget any used services.
     * @param context the framework context for the bundle.
    **/
    @Override
    public void stop(BundleContext context) throws Exception
    {
        context.addBundleListener(this);
        long bundleID = context.getBundle().getBundleId();
        // NOTE: The service is automatically released.
        System.out.println("Dictionary stopped. Need to start again. ID of the bundle: " + bundleID);
        context.getBundle(bundleID);
    }

    public void bundleChanged(BundleEvent be) {
       final Bundle bundle = be.getBundle();
       
       System.out.println("SIGNAL " + bundle.getSymbolicName() + "-" + be.getType());
    }
}
