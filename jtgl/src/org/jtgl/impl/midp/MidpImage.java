/*
 * MIDPImage.java
 *
 * Created on 21 de octubre de 2003, 18:55
 */

package org.jtgl.impl.midp;

import javax.microedition.lcdui.Image;

import org.jtgl.core.JTGLGraphics;
import org.jtgl.image.*;

/**
 * JTGLImage implementation for MIDP
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class MidpImage extends JTGLImage {
    private Image image;
    
    /** Creates a new instance of MIDPImage */    
    public MidpImage(Image image) {
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
    //TODO: Subclass for specific vendor implementations (eg: NokiaImage (creates NokiaGraphics), SiemensImage, etc)
    protected JTGLGraphics createGraphics(Image image){
        return new MidpGraphics(image, null);
    }
    
}
