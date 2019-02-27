/*
 * AppletMappletRunner.java
 *
 * Created on 12 de enero de 2004, 17:58
 */

package org.jtgl.micro.impl.awt;

import java.applet.*;
import java.awt.*;

import org.jtgl.micro.*;

/**
 * Applet Mapplet runner
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class AppletMappletRunner extends Applet implements Runnable{
    private Mapplet mapplet;
    private MappletContextCanvas mappletCanvas;
    private Thread mainThread;
    
    public AppletMappletRunner(){
        setLayout(new BorderLayout());
        mappletCanvas = new MappletContextCanvas(this);
        try {
            mappletCanvas.setIgnoreRepaint(true);            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    public void init() {
        mappletCanvas.setSize(getSize());
        String mappletClassName = getParameter("mappletClassName");
        try {
            setStatus("Loading "+mappletClassName+" ...");
            Class clazz = getClass().forName(mappletClassName);
            setStatus("Done!");
            mapplet = (Mapplet)clazz.newInstance();
            add(BorderLayout.CENTER, mappletCanvas);
            setStatus("Initializing Mapplet...");
            mapplet.initialize(mappletCanvas);
            setStatus("Done!");
        }catch(Exception ex){
            add(BorderLayout.CENTER, new Label(ex.getLocalizedMessage()));
        }
    }
    
    public void start(){
        setStatus("Starting applet...");
        if(mapplet != null && mainThread == null){
            setStatus("Starting mapplet...");
            mainThread = new Thread(this);
            mainThread.start();
            setStatus("Done!");
        }        
    }
    
    private void setStatus(String msg){
        System.out.println("Status: "+msg);
        showStatus(msg);
    }
    
    public void run() {
        mapplet.start();        
    }
    
}
