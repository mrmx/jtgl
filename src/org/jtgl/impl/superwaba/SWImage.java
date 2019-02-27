/*
 * SWImage.java
 *
 * Created on 10 de octubre de 2004, 20:26
 */

package org.jtgl.impl.superwaba;

import org.jtgl.core.JTGLGraphics;
import org.jtgl.image.*;

import waba.fx.Graphics;
import waba.fx.Image;


/**
 *
 * @author  Manuel
 */
public final class SWImage extends JTGLImage{
    private Image image;
    /** Creates a new instance of SWImage */
    public SWImage(Image image) {
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
        Graphics g = image.getGraphics();
        if(g == null)
            return null;        
        return new SWGraphics(g,null,false);
    }
    
}
