/*
 * Mapplet.java
 *
 * Created on 19 de diciembre de 2003, 16:46
 */

package org.jtgl.micro.impl.midp;


import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.impl.common.*;
import org.jtgl.impl.midp.*;
import org.jtgl.micro.*;

/**
 * MappletContext implementation for MIDP
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class MidpMappletContext extends MIDlet implements MappletContext{    
    private Display display;    // The display for this MIDlet
    private Canvas canvas;    
    private Thread mainThread;
    private Mapplet mapplet;
    private DefaultJavaThreadManager threadManager;
    
    /** Creates a new instance of Mapplet */
    public MidpMappletContext() {        
        display = Display.getDisplay(this);
        canvas = createCanvas(display);      
        threadManager = new DefaultJavaThreadManager();
        JTGLContext.registerContext(new DefaultJavaJTGLContext());         

    }    

    public final void runMapplet(Mapplet mapplet) {
        this.mapplet = mapplet;
    }
    
    /**
     * Start up the MIDlet     
     */
    public void startApp() {        
        display.setCurrent(canvas);        
        if(mapplet != null){
            try {
                mapplet.initialize(this);
            }catch(JTGLException ex){
                handleException(mapplet,ex,"Error initializing");
                display.setCurrent(canvas);        
            }   
            mainThread = new Thread(
                new Runnable(){
                    public void run() {
                        try {
                            mapplet.start();
                        }catch(Exception ex){
                            handleException(mapplet,ex,"Error starting");
                        }
                    }                
                }
            );
            mainThread.start();     
        }
    }    
 
    public void pauseApp() {
        display.setCurrent(null);
        mainThread = null;
    }
    
    /**
     * Destroy must cleanup everything not handled by the garbage collector.     
     */
    public void destroyApp(boolean unconditional) {        
        try {
            mapplet.kill(unconditional);
        }catch(JTGLException ex){
            if(!unconditional)
                return;
        }
        notifyDestroyed();
    }
    
    public void notifyKill(Mapplet mapplet) {
        //Kill all mapplet threads:        
        threadManager.killAll();        
        destroyApp(true);
    }       
    
    public final JTGLGraphics getJTGLGraphics(Mapplet mapplet) {
        return ((MidpCanvas)canvas).getGC();
    }
    
    public final void flushGraphics(Mapplet mapplet){
        canvas.repaint();
        canvas.serviceRepaints();
    }
    
    public final int getWidth(Mapplet mapplet){
        return canvas.getWidth();
    }

    public final int getHeight(Mapplet mapplet){
        return canvas.getHeight();
    }    
    
    protected Canvas createCanvas(Display display){
        return new MidpCanvas(display);
    } 
    
    protected Canvas getCanvas(){
        return canvas;
    }
   
    public final void delay(Mapplet mapplet,int ms) {
        try{
            Thread.currentThread().sleep(ms);
        }catch(InterruptedException ex){
        }
    }
    

    //Thread managenent:
    public final void threadSleep(long ms){
        try {                         
            Thread.sleep(ms);                    
        }catch(Exception ex){
            //handleException(currentMapplet, ex, null);
        }
    }
    public final void threadYield(){
        Thread.yield();
    }
    
    public final void addThreadClient(JTGLThreadClient client,int priority){
        threadManager.addThreadClient(client,priority);
    }
    
    public final void removeThreadClient(JTGLThreadClient client){
        threadManager.removeThreadClient(client);
    }
    
    
    
    public JTGLImage createJTGLImage(String src) throws JTGLException {
        try {      
            System.out.println("Image.createImage("+src+")");
            Image image = Image.createImage(src);
            System.out.println("Image loaded : "+image);
            if(image == null)
                return null;            
            return createJTGLImage(image);
        }catch(Exception ex){            
            ex.printStackTrace();
            throw new JTGLException(ex);
        }
    }
    
    public JTGLImage createJTGLImage(JTGLImage image) {
        return createJTGLImage(Image.createImage((Image)image.getNative()));
    }    

    public JTGLImage createJTGLImage(int width, int height) {
        return createJTGLImage(Image.createImage(width,height));
    }
    
    public JTGLImage createJTGLImage(byte[] imageData, int imageOffset, int imageLength) {
        return createJTGLImage(Image.createImage(imageData,imageOffset,imageLength));
    }
    
    
    protected JTGLImage createJTGLImage(Image image){
        return new MidpImage(image);        
    }
    
    public void handleException(Mapplet mapplet,Throwable ex,String title) {
        ex.printStackTrace();
        Alert alert = new Alert(title == null ? "Exception" : title);
        String msg = ex.toString();        
        if(ex.getMessage() != null)
            msg += "\n" + ex.getMessage();
        if(ex instanceof JTGLException){
            JTGLException jex = (JTGLException)ex;
            if(jex.getException() != null)
                msg += "\n" + jex.getException();
            if(jex.getMessage() != null)
                msg += "\n" + jex.getMessage();            
        }
        alert.setString(msg);
        alert.setTimeout(Alert.FOREVER);
        alert.setType(AlertType.CONFIRMATION);
        display.setCurrent(alert,canvas);         
    }
    
    public final Object get(Mapplet mapplet, String property) {        
        return getAppProperty(property);
    }    
    
    public ImageUtil getImageUtil() {    return new DefaultImageUtil(this);    }
    
    public final int getDisplayColors(Mapplet mapplet) {
        return display.numColors();
    }    
    
    public final long getCurrentTimeMs(){
        return System.currentTimeMillis();
    }
    
    public String[] getAvailableInputControls(Mapplet mapplet) {
        return ((MidpCanvas)canvas).getAvailableInputControls();
    }    
   
    public InputControl getInputControl(Mapplet mapplet,String inputControlClassName) {
        return ((MidpCanvas)canvas).getInputControl(inputControlClassName);
    }          


    
}
