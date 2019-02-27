/*
 * RectSurface.java
 *
 * Created on 1 de diciembre de 2003, 12:26
 */

package org.jtgl.gaming;

import org.jtgl.core.*;

/**
 * A Rectangle surface
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class RectSurface extends Surface{
    private JTGLColor color;
    private boolean filled;
    /** Creates a new instance of RectSurface */
    public RectSurface(int width,int height,JTGLColor color) {
        super(width,height);
        setColor(color);
        setFilled(true);
    }
    
    public RectSurface(int width,int height) {
        this(width, height, null);
    }
    
    public RectSurface(JTGLRect rect,JTGLColor color){
        this(rect.getWidth(),rect.getHeight(),color);
        setLocation(rect.getX(), rect.getY());
    }
    
    public RectSurface(JTGLRect rect){
        this(rect, null);
    }
    
    public void setColor(JTGLColor color){        
        this.color = color == null ? JTGLColor.BLACK : color ;
    }
    
    public void setFilled(boolean filled){
        this.filled = filled;
    }
    
    public void draw(JTGLGraphics g) {
        if(visible){
            if(filled)
                g.fillRect(x,y,width,height,color);
            else
                g.drawRect(x,y,width,height,color);        
        }
    }
    
}
