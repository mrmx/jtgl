/*
 * MIDPGraphics.java
 *
 * Created on 30 de julio de 2003, 14:17
 */

package org.jtgl.impl.midp.nokia;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Canvas;

import org.jtgl.core.*;
import org.jtgl.image.JTGLImage;

import org.jtgl.impl.midp.MidpGraphics;

import com.nokia.mid.ui.*;

/**
 * JTGLGraphics implementation for Nokia API
 * @author Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class NokiaGraphics extends MidpGraphics{       
    private DirectGraphics dg;
    
    /**
     * Creates a new instance of MIDPGraphics
     * @param buffer 
     * @param client 
     */
    public NokiaGraphics(Image buffer,Canvas client) {
        super(buffer,client);        
        this.dg = DirectUtils.getDirectGraphics(buffer.getGraphics());        
    }
    
    /**
     * Sets the internal color state
     * @param argbColor Color packed in AARRGGBB format
     */
    protected void setColorInternal(int argbColor) {
        dg.setARGBColor(argbColor);
    }
    
    //Nokia Accelerated primitives:
    /**
     * 
     * @param xPoints 
     * @param yPoints 
     * @param offset 
     * @param nPoints 
     * @param closePath 
     */
    public void drawPolyLine(int[] xPoints,int[] yPoints,int offset,int nPoints,boolean closePath){            
        if(closePath)
            dg.drawPolygon(xPoints, offset, yPoints, offset, nPoints, _currColor.getARGB());
        else
            super.drawPolyLine(xPoints, yPoints, offset,nPoints, false);
    }
    
    /**
     * 
     * @param xPoints 
     * @param yPoints 
     * @param offset 
     * @param nPoints 
     */
    public void fillPolygon(int[] xPoints,int[] yPoints,int offset,int nPoints){
        dg.fillPolygon(xPoints, offset, yPoints, offset, nPoints, _currColor.getARGB());
    }
    /**
     * 
     * @param x1 
     * @param y1 
     * @param x2 
     * @param y2 
     * @param x3 
     * @param y3 
     */
    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        dg.drawTriangle(x1,y1, x2, y2,x3, y3,_currColor.getARGB()); 
    }
    
    /**
     * 
     * @param x1 
     * @param y1 
     * @param x2 
     * @param y2 
     * @param x3 
     * @param y3 
     */
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        dg.fillTriangle(x1,y1, x2, y2,x3, y3,_currColor.getARGB()); 
    }

    
}
