/*
 * JTGLPoint.java
 *
 * Created on 25 de julio de 2003, 15:05
 */

package org.jtgl.core;

/**
 * Point primitive
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class JTGLPoint {
    int x,y;
    
    public JTGLPoint(){
        this(0,0);
    }
    
    /** Creates a new instance of JTGLPoint */
    public JTGLPoint(int x,int y) {
        this.x = x;
        this.y = y;
    }
    
    public JTGLPoint(JTGLPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    
    public void setLocation(int x,int y){
        this.x = x;
        this.y = y;        
    }
    
    public void setLocation(JTGLPoint point){
        if(point != null)
            setLocation(point.x, point.y);
    }
    
    public void translate(int x,int y){
        this.x += x;
        this.y += y;        
    }
    
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void copy(JTGLPoint from){
        setLocation(from);
    }
    
    public JTGLPoint getClone(){
        return new JTGLPoint(this);
    }
    
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof JTGLPoint){            
            JTGLPoint p = (JTGLPoint)obj;
            return x == p.x && y == p.y;
        }
        return false;
    }
    
    public String toString(){
        return "JTGLPoint[x = "+x+", y = "+y+"]";
    }
}
