/*
 * AWTImage.java
 *
 * Created on 19 de junio de 2003, 11:55
 */

package org.jtgl.impl.awt;

import org.jtgl.core.JTGLGraphics;
import org.jtgl.image.*;

import java.awt.Graphics;
import java.awt.Image;

/**
 * JTGLImage implementation for AWT
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class AWTImage extends JTGLImage{
    private Image image;
    /** Creates a new instance of AWTImage */
    public AWTImage(Image image) {
        this.image = image;
        setNative(true);
    }   
    
    public Object getNative(){
        return image;
    }        
    
    public int getHeight() {
        return image.getHeight(null);
    }
    
    public int getWidth() {
        return image.getWidth(null);
    }
    
    public JTGLGraphics getGraphics() {
        Graphics g = image.getGraphics();
        if(g == null)
            return null;        
        return new AWTGraphics(g,this);
    }
    
}
