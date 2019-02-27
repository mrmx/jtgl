/*
 * Dimension.java
 *
 * Created on 25 de julio de 2003, 16:31
 */

package org.jtgl.ui;

/**
 * Represents an inmutable 2D Dimension
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Dimension {
    int width,height;
    /** Creates a new instance of Dimension */
    public Dimension(int width,int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getWidth(){
        return width;        
    }
    
    public int getHeight(){
        return height;
    }    
    
   public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof Dimension){            
            Dimension d = (Dimension)obj;
            return width == d.width && height == d.height;
        }
        return false;
    }  
    
    
    public String toString(){
        return "org.jtgl.ui.Dimension[width = "+width+", height = "+height+"]";
    }    
}
