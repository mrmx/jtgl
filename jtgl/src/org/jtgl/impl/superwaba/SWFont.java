/*
 * SWFont.java
 *
 * Created on 21 de octubre de 2004, 16:48
 */

package org.jtgl.impl.superwaba;

import org.jtgl.core.*;
import waba.fx.ISurface;
import waba.fx.Font;
import waba.fx.FontMetrics;

/**
 *
 * @author  Manuel
 */
public class SWFont extends JTGLFont{
    static ISurface surface;    
    private static SWFont defaultInstance;    //Singleton instance
    private static JTGLFont defaultFont;    //Singleton instance
    private Font font;    
    private FontMetrics fontMetrics;
    
    /** Creates a new instance of SWFont */
    private SWFont(ISurface surface) {
        this.surface = surface;
    }

    /** Creates a new instance of SWFont */
    private SWFont(Font font) {
        this.font = font;        
        init(font.getName(),font.getStyle(), font.getSize());                
    }
    
    public int charWidth(char ch) {
        checkFontMetrics();
        return fontMetrics.getCharWidth(ch);
    }
    
    public JTGLFont createFont(String name, int style, int size) {
        style = style == -1 ? JTGLFont.STYLE_PLAIN : style & 1;
        size = size == -1 ? 12 : size;
        return new SWFont(new Font(name, style, size));        
    }
    
    public String[] getAvailableFontNames() {
        return null;
    }
    
    public JTGLFont getDefaultFont() {
        if(defaultFont == null)
            defaultFont = createFont("SW",-1,-1);        //Create Singleton instance
        return defaultFont;
    }
    
    public int getHeight() {
        checkFontMetrics();
        return fontMetrics.getHeight();
    }
    
    public Object getNativeFont() {
        return font;
    }
    
    public int stringWidth(String str, int offset, int length) {
        checkFontMetrics();
        return fontMetrics.getTextWidth(str.substring(offset,offset+length));
    }
    
    public int stringWidth(String str){
        checkFontMetrics();
        return fontMetrics.getTextWidth(str);
    }    
    
    private void checkFontMetrics() {
        if(fontMetrics == null)
            fontMetrics = fontMetrics = new FontMetrics(font,surface);
    }    
    
    static JTGLFont getDefault(ISurface surface){
        if(defaultInstance == null)
            defaultInstance = new SWFont(surface);
        return defaultInstance.getDefaultFont();        
    }
    
}
