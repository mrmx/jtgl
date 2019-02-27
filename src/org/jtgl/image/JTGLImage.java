    /*
 * Image.java
 *
 * Created on 19 de junio de 2003, 11:31
 */

package org.jtgl.image;

import org.jtgl.core.*;

/**
 * Base abstract Image class
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class JTGLImage {       
    
    private boolean isNative = false;    
    
    /** Creates a new instance of Image */
    public JTGLImage() {
   
    }    
    
    protected void setNative(boolean _native){
        this.isNative = _native;
    }
    
    public boolean isNative(){
        return isNative;
    }
    
    public Object getNative(){
        return null;
    }    
    
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract JTGLGraphics getGraphics();
    
    
    public String toString(){
        return "JTGLImage["+getWidth()+"x"+getHeight()+"]";
    }
}
