/*
 * Midp2Image.java
 *
 * Created on 29 de enero de 2004, 1:04
 */

package org.jtgl.impl.midp2;

import javax.microedition.lcdui.Image;

import org.jtgl.core.JTGLGraphics;
import org.jtgl.image.JTGLImage;


/**
 * JTGLImage implementation for MIDP2
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Midp2Image extends JTGLImage{    
    private Image image;
    
    /** Creates a new instance of MIDPImage */    
    public Midp2Image(Image image) {
        this.image = image;
        setNative(true);
    }   
    
    public Object getNative(){
        return image;
    }        
    
    public int getHeight() {
        return image.getHeight();
    }
    
    public int getWidth() {
        return image.getWidth();
    }
    
    public JTGLGraphics getGraphics() {
        if(image.isMutable())
            return createGraphics(image);
        return null;
    }
    
    protected JTGLGraphics createGraphics(Image image){
        return new Midp2Graphics(image.getGraphics(), null);
    }
}
