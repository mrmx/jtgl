/*
 * JTGLFont.java
 *
 * Created on 6 de noviembre de 2003, 20:57
 */

package org.jtgl.core;

/**
 * Represents a Font used in text rendering.
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class JTGLFont {    
    public static final int SIZE_SMALL          =   0;
    public static final int SIZE_MEDIUM         =   1;
    public static final int SIZE_LARGE          =   2;   

    public static final int STYLE_PLAIN         =   0;
    public static final int STYLE_BOLD          =   1;
    public static final int STYLE_ITALIC        =   2;   
    public static final int STYLE_UNDERLINED    =   4;   
    
    private String name;
    private int style;
    private int size;    
    
    /** Creates a new instance of JTGLFont */
    protected JTGLFont(){
    }
    
    protected void init(String name,int style,int size) {
        this.name = name;
        style = translateToJTGLStyle(style);
        this.style = (style & ~7) == 0 ? style : STYLE_PLAIN;
        this.size = size < 0 ? 0 : size;        
    }
    
    public String getName(){
        return name;
    }

    public int getStyle(){
        return style;
    }

    public int getSize(){
        return size;
    }
    
    public boolean isPlain(){
        return (style & STYLE_PLAIN) != 0;
    }

    public boolean isBold(){
        return (style & STYLE_BOLD) != 0;
    }

    public boolean isItalic(){
        return (style & STYLE_ITALIC) != 0;
    }    
    
    public boolean isUnderlined(){
        return (style & STYLE_UNDERLINED) != 0;
    }    
    
    
    public int stringWidth(String str){
        return stringWidth(str,0,str.length());
    }
    
    public int [] getSizes(){
        return null;    //no defined predefined or fixed sizes on implementation (default)
    }
    
    
    
    public abstract int getHeight();
    public abstract int charWidth(char ch);    
    public abstract int stringWidth(String str,int offset,int length);    
    public abstract String [] getAvailableFontNames();
    public abstract Object getNativeFont();
    public abstract JTGLFont getDefaultFont();        
    public JTGLFont createFont(String name){
        return createFont(name, -1, -1);
    }    
    public JTGLFont createFont(int style){
        return createFont(getName(), style, -1);
    }
    public JTGLFont createFont(String name,int style){
        return createFont(name, style, -1);
    }
    public abstract JTGLFont createFont(String name,int style,int size);        
    
    protected int translateToJTGLStyle(int nativeStyle){
        return nativeStyle;
    }

    protected int translateToNativeStyle(int style){
        return style;
    }
    
    public String toString(){
        return "JTGLFont[name:"+name+",style:"+style+",size:"+size+"]";
    }
}
