/*
 * DefaultJavaThreadManager.java
 *
 * Created on 22 de diciembre de 2004, 17:52
 */

package org.jtgl.impl.common;

import org.jtgl.core.JTGLThreadClient;
import org.jtgl.core.JTGLContext;

import java.util.*;

/**
 * Manages the JTGL Thread client-model via standard java.lang.Thread 
 * This may be subclassed to implement thread pools or whatever.
 * @author mrmx
 */
public class DefaultJavaThreadManager {    
    private Hashtable clients;
    
    /** Creates a new instance of DefaultJavaThreadManager */
    public DefaultJavaThreadManager() {
        clients = new Hashtable();
    }
    
    public void addThreadClient(JTGLThreadClient client,int priority){
        if(client == null)
            return ;
        if(clients.containsKey(client)){
            JTGLContext.debugMsg("JTGLThreadClient "+client+"  already added!");
            return ;
        }
        RunnableAdapter adapter = new RunnableAdapter();
        adapter.client = client;
        Thread clientThread = new Thread(adapter);
        clientThread.setPriority(priority);
        clients.put(client,adapter);        
        clientThread.start();
    }
    
    public void removeThreadClient(JTGLThreadClient client){    
        if(client == null)
            return ;        
        if(!clients.containsKey(client)){
            JTGLContext.debugMsg("JTGLThreadClient "+client+"  not added previously!");
            return ;
        }   
        RunnableAdapter adapter = (RunnableAdapter)clients.get(client);
        adapter.isRunning = false;
    }    
    
    public void killAll(){
        for(Enumeration e = clients.keys(); e.hasMoreElements(); )
            removeThreadClient((JTGLThreadClient)e.nextElement());
    }
    
    private synchronized void removeInternal(JTGLThreadClient client){    
        clients.remove(client);
    }    
    

    final class RunnableAdapter implements Runnable {
        JTGLThreadClient client;
        boolean isRunning = true;        
        public void run(){
            client.started();
            while(isRunning){
                //TODO: measure client run time to avoid abuse on resources
                client.run();                
            }
            client.stopped();
            removeInternal(client);
        }       
        
        public boolean equals(Object object){
            return client.equals(object);
        }
        
    }
    
    
}
