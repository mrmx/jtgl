/*
 * ExEnImage.java
 *
 * Created on 24 de enero de 2004, 20:21
 */

package org.jtgl.impl.exen;

import exen.Image;

import org.jtgl.core.JTGLGraphics;
import org.jtgl.image.*;

/**
 *
 * @author  Administrator
 */
public class ExEnImage extends JTGLImage{
    private Image image;
    
    /** Creates a new instance of ExEnImage */
    public ExEnImage(Image image) {
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
        return new ExEnGraphics(image, null);
    }
}
