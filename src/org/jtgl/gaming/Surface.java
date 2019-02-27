/*
 * Surface.java
 *
 * Created on 1 de diciembre de 2003, 12:12
 */

package org.jtgl.gaming;

import org.jtgl.core.*;

/**
 * A drawable element
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class Surface {
    boolean visible;
    JTGLPoint location;
    JTGLRect bounds;
    /*
     * Location of bounding rect
     */
    int x,y;    
    /*
     * Size of bounding rect
     */    
    int width,height;

    /*
     * Half Size of bounding rect
     */    
    int halfWidth,halfHeight;
    
     /*
     * Reference location of bounding rect (eg: for line movement)
     */
    int xRef,yRef;    
    
    /**
     * Determines if this surface is collidable
     * @see setCollisionDisable()
     * @see isCollisionDisable()
     */
    boolean collissionDisabled  = false;
    
    
    
    /** Creates a new instance of Surface */
    public Surface(int width,int height) {      
        setWidth(width);
        setHeight(height);
        setRefLocation(halfWidth, halfHeight);  //Sets reference location to center;
        visible = true; //Visible as default
    }
    
    public void setLocation(int x,int y){
        this.x = x;
        this.y = y;
    }
    
    public void setRefLocation(int xRef,int yRef){
        this.xRef = xRef;
        this.yRef = yRef;
    }
    
    public void setAbsRefLocation(int x,int y){    
        setLocation(x-xRef, y-yRef);
    }
    
    
    public JTGLPoint getLocation(){
        if(location == null)
            return location = new JTGLPoint(x,y);                    
        else
            location.setLocation(x,y);
        return location;
    }
    
    
    public JTGLPoint getRefLocation(){
        if(location == null)
            return location = new JTGLPoint(xRef,yRef);                    
        else
            location.setLocation(xRef,yRef);
        return location;        
    }
    
    public JTGLPoint getAbsRefLocation(){
        JTGLPoint location = getLocation();
        location.translate(xRef,yRef);
        return location;
    }

    public JTGLPoint getCenterLocation(){
        JTGLPoint location = getLocation();
        location.translate(halfWidth,halfHeight);
        return location;        
    }
    
    public JTGLRect getBounds(){
        if(bounds == null)
            bounds = new JTGLRect(x,y,width,height);
        else
            bounds.copy(x, y, width,height);
        return bounds;            
    }
    

    public void move(int dx,int dy){
        setLocation(x + dx, y + dy);
    }

    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public void setVisible(boolean visible){
        this.visible = visible;        
    }
    
    public boolean isVisible(){
        return visible;
    }
    
    public void setCollisionDisabled(boolean collissionDisabled){
        this.collissionDisabled = collissionDisabled;
    }
    
    public final boolean isCollisionDisabled(){
        return collissionDisabled;
    }
    
    public String toString(){        
        return getClass().getName()+"[x = "+x+", y = "+y+", width = "+width+", height = "+height+"]";
    }
    
    
    public abstract void draw(JTGLGraphics g);
    
    void setWidth(int width){
        this.width = width < 0 ? 0 : width;
        halfWidth = this.width >> 1;
    }

    void setHeight(int height){
        this.height = height < 0 ? 0 : height;
        halfHeight = this.height >> 1;
    }  
    
}
