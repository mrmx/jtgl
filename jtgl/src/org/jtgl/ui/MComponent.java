/*
 * Component.java
 *
 * Created on 16 de diciembre de 2004, 11:24
 */

package org.jtgl.ui;

import org.jtgl.core.*;
import org.jtgl.image.*;


/**
 * Root UI Component
 * @author mrmx
 */
public abstract class MComponent {
    public static final int ALIGN_LEFT      =   1;
    public static final int ALIGN_CENTER    =   2;
    public static final int ALIGN_RIGHT     =   3;
    public static final int ALIGN_TOP       =   4;    
    public static final int ALIGN_BOTTOM    =   5;
    //Location:
    int x;
    int y;
    //Size:
    int width;
    int height;
    Dimension size;
    //Insets:
    int insetTop;
    int insetBottom;
    int insetLeft;
    int insetRight;
    //Alignment
    int valign;
    int align;
    //Font;
    JTGLFont font;
    //Colors:
    JTGLColor color;
    JTGLColor bgColor;
    boolean opaque = true;
    boolean visible = true;    
    boolean validated = false;
    MContainer parent;
    
    
    /** Creates a new instance of Component */
    public MComponent() {
        setColor(null); //set default foreground color
        setBackground(null); //set default background color
    }
    
    public void setLocation(int x,int y){
        this.x = x;
        this.y = y;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public void setSize(int width,int height){
        this.width = width;
        this.height = height;
    }
    
    public Dimension getSize(){
        if(size == null){
            size = new Dimension(width, height);
        }else {
            size.width = width;
            size.height = height;
        }
        return size;
    }
    
    public void setFont(JTGLFont font){
        this.font = font == null? FontManager.getDefaultFont(): font;
    }
    
    public void setColor(JTGLColor color){
        this.color = color == null? JTGLColor.BLACK:color;
    }
    
    public JTGLColor getColor(){
        return color;
    }
    
    public void setBackground(JTGLColor color){
        this.bgColor = bgColor == null? JTGLColor.WHITE:bgColor;
    }
    
    public JTGLColor getBackground(){
        return bgColor;
    }   
    
    public void inverse(){
        JTGLColor tempColor = color;
        color = bgColor;
        bgColor = tempColor;
    }
    
    public MContainer getParent(){
        return parent;
    }
    
    public void doLayout(){
        
    }
    
    public void invalidate(){
        validated = false;        
    }
    
    public boolean isValid(){
        return validated;
    }
    
    public void validate(){
        if(validated)
            return ;
        doLayout();
        validated = true;        
    }
    
    public void setVisible(boolean visible){
        this.visible = visible;
    }
    
    public boolean isVisible(){
        return visible;
    }

    public void setOpaque(boolean opaque){
        this.opaque = opaque;
    }
    
    public boolean isOpaque(){
        return opaque;
    }
    
    public void paintBackground(JTGLGraphics g,int x,int y, int width,int height){
        g.fillRect(x,y,width, height);
    }
    
    public abstract void paint(JTGLGraphics g);
    
    
    public String toString(){
        return getClass().getName()+"["+x+","+y+","+width+","+height+"][valid:"+validated+"]";
    }
    
}
