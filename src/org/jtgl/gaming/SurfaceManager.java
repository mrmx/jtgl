/*
 * SurfaceManager.java
 *
 * Created on 1 de diciembre de 2003, 12:27
 */

package org.jtgl.gaming;

import org.jtgl.core.*;

/**
 * SurfaceManager provides a container of Surfaces
 * API based on J2ME/MIDP 2.0
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class SurfaceManager {
    private static final int MIN_SURFACE_GROW   = 2;
    private int surfaceGrow;
    private Surface surfaces[];
    private int numSurfaces;
    private int viewX;
    private int viewY;
    private int viewWidth;
    private int viewHeight;
    private boolean hasViewWindow;
    private int clipX;
    private int clipY;
    private int clipWidth;
    private int clipHeight;
        
    public SurfaceManager() {
        this(MIN_SURFACE_GROW);
    }
    
    public SurfaceManager(int surfaceGrow) {
        this.surfaceGrow = surfaceGrow < 1 ? MIN_SURFACE_GROW : surfaceGrow;
        surfaces = new Surface[4];
        setViewWindow(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    public void append(Surface s) {
        remove(s);
        add(s, numSurfaces);
    }
    
    public void insert(Surface s, int index) {
        if(index < 0 || index > numSurfaces) {
            throw new IndexOutOfBoundsException();
        } else {
            remove(s);
            add(s, index);
            return;
        }
    }
    
    public Surface getSurfaceAt(int index) {
        return surfaces[index];
    }
    
    public int getSize() {
        return numSurfaces;
    }
    
    public void remove(Surface s) {
        for(int i = numSurfaces; --i >= 0;)
            if(surfaces[i] == s)
                remove(i);        
    }
    
    public void removeAll(){
        for(int i = numSurfaces; --i >= 0;)
            remove(i);
    }
    
    public void draw(JTGLGraphics g, int x, int y) {
        clipX = g.getClipX();
        clipY = g.getClipY();
        clipWidth = g.getClipWidth();
        clipHeight = g.getClipHeight();
        if(hasViewWindow)
            g.setClip(x, y, viewWidth, viewHeight);
        g.translate(x - viewX, y - viewY);        
        for(int i = numSurfaces; --i >= 0;) {
            Surface surface = surfaces[i];
            if(surface.visible)
                surface.draw(g);
        }        
        g.translate(-x + viewX, -y + viewY);
        //Restore previous clipping viewport:
        if(hasViewWindow)
            g.setClip(clipX,clipY,clipWidth,clipHeight);
    }
    
    public void setViewWindow(int x, int y, int width, int height) {
        if(width < 0 || height < 0) {
            throw new RuntimeException();
        } else {            
            viewX = x;
            viewY = y;
            viewWidth = width;
            viewHeight = height;            
            hasViewWindow = true;
        }
    }
    
    public int getViewX(){
        return viewX;
    }
    public int getViewY(){
        return viewY;
    }
    public int getViewWidth(){
        return viewWidth;
    }
    public int getViewHeight(){
        return viewHeight;
    }
    
    public void setViewLocation(int x,int y){    
        viewX = x;
        viewY = y;
    }
    
    public void moveView(int dx,int dy){
        viewX += dx;
        viewY += dy;
    }
    
    /**
     * Centers view window relative to a <code>surface</code>.
     * This code is usefull to scroll the view following a main surface (eg: Sprite)
     * across its move around a map (TiledSurface).
     */
    public void centerView(Surface surface,int viewMaxWidth,int viewMaxHeight){
        JTGLPoint sCenter = surface.getCenterLocation();
        centerView(sCenter.getX(),sCenter.getY(), viewMaxWidth, viewMaxHeight);
    }

    /**
     * Centers view window relative to a <code>(vx,vy)</code> point.
     * This code is usefull to scroll the view following a point.     
     */    
    public void centerView(int vx,int vy,int viewMaxWidth,int viewMaxHeight){        
        vx -= (viewWidth >> 1);
        vy -= (viewHeight >> 1);
        if(viewMaxWidth > 0 && viewMaxHeight > 0){ // Limit view to this bounds (generally a map bounds)
            vx = vx < 0 ? 0 : (vx > viewMaxWidth - viewWidth ? viewMaxWidth - viewWidth : vx );            
            vy = vy < 0 ? 0 : (vy > viewMaxHeight - viewHeight ? viewMaxHeight - viewHeight : vy );  
        }
        setViewLocation(vx, vy);
    }
    
    
    private void add(Surface surface, int index) {
        if(numSurfaces == surfaces.length) {
            Surface newsurfaces[] = new Surface[numSurfaces + surfaceGrow];
            JTGLContext.arraycopy(surfaces, 0, newsurfaces, 0, numSurfaces);
            JTGLContext.arraycopy(surfaces, index, newsurfaces, index + 1, numSurfaces - index);
            surfaces = newsurfaces;
        } else {
            JTGLContext.arraycopy(surfaces, index, surfaces, index + 1, numSurfaces - index);
        }
        surfaces[index] = surface;
        numSurfaces++;
    }    
    
    private void remove(int index) {
        JTGLContext.arraycopy(surfaces, index + 1, surfaces, index, numSurfaces - index - 1);
        surfaces[--numSurfaces] = null;
    }

}
