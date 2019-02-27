/*
 * Label.java
 *
 * Created on 16 de diciembre de 2004, 12:01
 */

package org.jtgl.ui;

import org.jtgl.core.*;

/**
 *
 * @author mrmx
 */
public class MLabel extends MComponent{
    private String text;
    
    /** Creates a new instance of Label */
    public MLabel(String text) {
        this.text = text;
    }
    
    public void setText(String text){
        this.text = text;        
        invalidate();
    }    
    
    public void doLayout(){
        JTGLFont currFont = font == null? FontManager.getDefaultFont() : font;        
        width = insetLeft + (text == null ? currFont.charWidth(' ') : currFont.stringWidth(text)) + insetRight;
        height = insetTop + currFont.getHeight() + insetBottom;
    }
    
    
    public void paint(JTGLGraphics g){                    
        g.setColor(JTGLColor.RED.translucent());
        g.drawRect(x,y,width,height);        
        g.setColor(color);
        
        g.drawString(text, x+insetLeft, y+insetTop);        
        
    }
    
}
