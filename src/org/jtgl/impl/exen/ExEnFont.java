/*
 * MidpFont.java
 *
 * Created on 14 de enero de 2004, 19:30
 */

package org.jtgl.impl.exen;


import java.util.Vector;
import org.jtgl.core.*;

/**
 * JTGLFont implementation for ExEn
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class ExEnFont extends JTGLFont{        
    private static String [] fontNames = { "ExEn"};
    private int charWidth,charHeight;
    
    /** Creates a new instance of ExEnFont */
    ExEnFont(int charWidth,int charHeight) {
        this.charWidth = charWidth;
        this.charHeight = charHeight;
    }
    
    /** Creates a new instance of ExEnFont */
    protected ExEnFont(int charWidth,int charHeight,String name, int style) {        
        this(charWidth,charHeight);
        init(name, style,1);
    }
    
    
    public JTGLFont createFont(String name, int style, int size) {
        return new ExEnFont(charWidth,charHeight,name,style);
    }
    
    
    public String[] getAvailableFontNames() {
        return fontNames;
    }
    
   
    public JTGLFont getDefaultFont() {
        return this;
    }
    
    public int getHeight() {
        return charHeight;
    }
    
    public Object getNativeFont() {
        return null;
    }

    public int charWidth(char ch) {
        return charWidth;
    }
    
    public int stringWidth(String str){
        return str.length() * charWidth;
    }
    
    public int stringWidth(String str, int offset, int length) {
        return length * charWidth;
    }              
}
