/*
 * Border.java
 *
 * Created on 17 de diciembre de 2004, 13:23
 */

package org.jtgl.ui.border;

import org.jtgl.ui.*;

/**
 * Paints a border
 * @author Manuel Polo (manuel_polo@yahoo.es)
 */
public abstract class Border {
    
    /** Creates a new instance of Border */
    public Border() {
    }
    
    public abstract void paintBorder(MComponent comp,int x,int y,int width,int height);
    
}
