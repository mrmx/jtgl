/*
 * MidpFont.java
 *
 * Created on 14 de enero de 2004, 19:30
 */

package org.jtgl.impl.midp;


import javax.microedition.lcdui.Font;
import java.util.Vector;
import org.jtgl.core.*;

/**
 * JTGLFont implementation for MIDP
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class MidpFont extends JTGLFont{
    private static MidpFont defaultInstance;
    private Font font;
    private static String [] fontNames = { "System","Monospace","Proportional"};
    private static int [] fixedSizes = { Font.SIZE_SMALL , Font.SIZE_MEDIUM , Font.SIZE_LARGE};   
    
    /** Creates a new instance of MidpFont */
    protected MidpFont() {
    }
    
    /** Creates a new instance of MidpFont */
    protected MidpFont(String name,Font font) {
        this.font = font;
        init(name, font.getStyle(), font.getSize());
    }
    
    
    public JTGLFont createFont(String name, int style, int size) {
        int face = Font.FACE_SYSTEM;        
        if(fontNames[1].equals(name)){
            face = Font.FACE_MONOSPACE;            
        }
        else
        if(fontNames[2].equals(name)){
            face = Font.FACE_PROPORTIONAL;                     
        }
        style = style == -1 ? Font.STYLE_PLAIN : style;
        size = size == -1 ? Font.SIZE_MEDIUM : size;
        return new MidpFont(name,Font.getFont(face,style,size));
    }
    
    
    public String[] getAvailableFontNames() {
        return fontNames;
    }
    
    public int [] getSizes(){    
        return fixedSizes;
    }
    
    public JTGLFont getDefaultFont() {
        Font defaultFont = Font.getDefaultFont();
        return new MidpFont(getFaceName(defaultFont.getFace()),defaultFont);
    }
    
    public int getHeight() {
        return font.getHeight();
    }
    
    public Object getNativeFont() {
        return font;
    }

    public int charWidth(char ch) {
        return font.charWidth(ch);
    }
    
    public int stringWidth(String str){
        return font.stringWidth(str);
    }
    
    public int stringWidth(String str, int offset, int length) {
        return font.substringWidth(str, offset,length);
    }   
    
    
    public static JTGLFont getDefault(){
        if(defaultInstance == null)
            defaultInstance = new MidpFont();
        return defaultInstance.getDefaultFont();
    }     
    
    private final String getFaceName(int face){
        if(face == Font.FACE_MONOSPACE)
            return fontNames[1];
        if(face == Font.FACE_PROPORTIONAL)
            return fontNames[2];        
        return fontNames[0];
    }    
}
