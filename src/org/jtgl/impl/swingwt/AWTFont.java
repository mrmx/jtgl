/*
 * AWTFont.java
 *
 * Created on 14 de enero de 2004, 17:05
 */

package org.jtgl.impl.swingwt;

import swingwt.awt.*;
import org.jtgl.core.*;

/**
 * AWT JTGLFont implementation
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class AWTFont extends JTGLFont{
    private static AWTFont defaultInstance;
    private Font font;
    private FontMetrics fontMetrics;
    
    
    
    /**
     * Predefined sizes for <code>JTGLFont</code>'s constants:
     *  <code>JTGLFont.SIZE_SMALL</code>
     *  <code>JTGLFont.SIZE_MEDIUM</code>
     *  <code>JTGLFont.SIZE_LARGE</code>
     */
    private static int [] predefinedSizes = { 8 , 16 , 32};   
    
    /** Creates a new instance of AWTFont */
    private AWTFont(){
    }
    
    /** Creates a new instance of AWTFont */
    private AWTFont(Font font) {
        this.font = font;
        init(font.getName(),font.getStyle(), font.getSize());        
    }    

    public String[] getAvailableFontNames(){
        String[] fontNames = null;
        /*
         //Not implemented GraphicsEnvironment and getAvailableFontFamilyNames
        try {
          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
          fontNames = ge.getAvailableFontFamilyNames();
        }catch(Throwable t){
          fontNames = Toolkit.getDefaultToolkit().getFontList();
        }
         */        
        return fontNames;    
    }
    
    
    public JTGLFont getDefaultFont() {
        return createFont("Dialog", -1, -1);
    }
    
    public JTGLFont createFont(String name, int style, int size) {
        style = style == -1 ? JTGLFont.STYLE_PLAIN : style;
        size = size == -1 ? 12 : size;
        return new AWTFont(new Font(name, style, size));
    }
    
    public Object getNativeFont() {
        return font;
    }
   
    public int [] getSizes(){
        return predefinedSizes;
    }        


    public int getHeight() {
        checkFontMetrics();
        return fontMetrics.getHeight();
    }        
    
    public int charWidth(char ch) {
        checkFontMetrics();
        return fontMetrics.charWidth(ch);
    }        
    
    public int stringWidth(String str, int offset, int length) {
        checkFontMetrics();
        return fontMetrics.stringWidth(str.substring(offset,offset+length));
    }    
  
    private void checkFontMetrics() {
        if(fontMetrics == null)
            fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);        
    }
    
    static JTGLFont getDefault(){
        if(defaultInstance == null)
            defaultInstance = new AWTFont();
        return defaultInstance.getDefaultFont();
    }    
    

    
}
