/*
 * Mapplet.java
 *
 * Created on 19 de diciembre de 2003, 17:03
 */

package org.jtgl.micro;

import org.jtgl.core.*;
import org.jtgl.input.*;
import org.jtgl.image.JTGLImage;


/**
 * Defines a Micro Application (aka Mapplet) Context (such as MIDP,ExEn,DoJa).
 * @author Manuel Polo (manuel_polo at yahoo dot es)
 */
public interface MappletContext {
    public void runMapplet(Mapplet mapplet);
    
    /** Gets global graphics context
     * @return JTGLGraphics instance
     */    
    public JTGLGraphics getJTGLGraphics(Mapplet mapplet); 
    public void flushGraphics(Mapplet mapplet); 
    public int getWidth(Mapplet mapplet);
    public int getHeight(Mapplet mapplet);
    public int getDisplayColors(Mapplet mapplet);
    
    public JTGLImage createJTGLImage(String src) throws JTGLException;
    public JTGLImage createJTGLImage(int width,int height);
    public JTGLImage createJTGLImage(byte[] imageData,int imageOffset,int imageLength);    
    public JTGLImage createJTGLImage(JTGLImage image);
    
    public Object get(Mapplet mapplet,String property);
    public ImageUtil getImageUtil();
    
    public void delay(Mapplet mapplet,int ms);      
    
    public void notifyKill(Mapplet mapplet);    
    public void handleException(Mapplet mapplet,Throwable ex,String title);
    
    // Input handling:
    public String [] getAvailableInputControls(Mapplet mapplet);
    public InputControl getInputControl(Mapplet mapplet,String inputControlClassName);

    //System access:
    public long getCurrentTimeMs();
    //System global thread access:
    public void threadSleep(long ms);
    public void threadYield();
    public void addThreadClient(JTGLThreadClient client,int threadPriority);
    public void removeThreadClient(JTGLThreadClient client);  
    
}
