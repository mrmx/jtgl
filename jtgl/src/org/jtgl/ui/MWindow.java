/*
 * Window.java
 *
 * Created on 16 de diciembre de 2004, 13:19
 */

package org.jtgl.ui;

import org.jtgl.core.*;

/**
 *
 * @author mrmx
 */
public class MWindow extends MContainer{
    
    /** Creates a new instance of Window */
    public MWindow() {
    }
    
    public void pack(){
        invalidate();
        validateChildren();
    }
    
    public void paint(JTGLGraphics g){
        super.paint(g);
        g.flush();
    }
    
}
