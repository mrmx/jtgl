/*
 * SWGraphics.java
 *
 * Created on 10 de octubre de 2004, 19:25
 */

package org.jtgl.impl.superwaba;

import org.jtgl.core.*;
import org.jtgl.image.*;

import waba.fx.Graphics;
import waba.fx.Image;
import waba.fx.Rect;
import waba.fx.Font;
import waba.ui.Control;

/**
 *
 * @author  Manuel
 */
public final class SWGraphics extends JTGLGraphics {
    private Control client;
    private Graphics g;    
    private Rect clip;
    private char [] charData;        
    private JTGLRect tmpRect = new JTGLRect();    //To save memory reuse this
    private int clipX,clipY,clipWidth,clipHeight;
    private static int drawPaintOp = Graphics.DRAW_PAINT;
    private boolean doClip = true;
    private boolean screenGC;
    
    /** Creates a new instance of SWGraphics */
    public SWGraphics(Graphics g,Control client,boolean screenGC) {
        this.g = g;
        this.client = client;        
        this.screenGC = screenGC;
        clip = new Rect();
        charData = new char[1];        
    }
    
    public void clear(){
        if(screenGC){
            saveColor(getBackgroundColor());
            g.clearScreen();
            restoreColor();
        }
        else    
            clear(getClip());
    }

    
    public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest) {
        //TODO
        JTGLContext.debugMsg("SWGraphics.copyArea not implemented!");
    }
    
    public void drawChar(char ch, int x, int y) {
        charData[0] = ch;        
        g.drawText(charData,0,1,x,y);
    }
    
    public void drawString(String str,int x,int y){
        g.drawText(str,x,y);
    }
    
    public void drawImage(JTGLImage img, int x, int y) {
        if(img == null) 
            return;
        Image nativeImg = (Image)img.getNative();    
        if(nativeImg != null)            
            //g.drawImage(nativeImg,x,y);                
            g.drawImage(nativeImg,x,y,drawPaintOp,null,doClip);                
    }
    
    public void drawLine(int xf, int yf, int xt, int yt) {
        g.drawLine(xf, yf, xt, yt);
    }
    
    public void drawPolyLine(int[] xPoints,int[] yPoints,int offset,int nPoints,boolean closePath){        
        //TODO implement Offset shifting both arrays
        if(closePath)
            g.drawPolygon(xPoints,yPoints, nPoints); 
        else
            g.drawPolyline(xPoints,yPoints, nPoints);
    }
    
    public void drawRegion(JTGLImage src, int x_src, int y_src, int width, int height, int x_dest, int y_dest) {
        //TODO: Test this!
        
        Image imageSrc = (Image)src.getNative();
        /*
        clipX = getClipX();
        clipY = getClipY();
        clipWidth = getClipWidth();
        clipHeight = getClipHeight();                                 
        JTGLRect.intersection(x_dest,y_dest,width,height,clipX-getTranslationX(),clipY-getTranslationY(),clipWidth,clipHeight,tmpRect);        
        if(tmpRect.isEmpty() == false){
            setClip(tmpRect);            
            g.drawImage(imageSrc, x_dest-x_src, y_dest-y_src);                         
            setClip(clipX,clipY, clipWidth, clipHeight);        
        } 
         */
        g.copyRect(imageSrc,x_src,y_src,width,height,x_dest,y_dest);
    }
    
    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x,y, width, height);
    }
    
    public void fillPolygon(int[] xPoints,int[] yPoints,int offset,int nPoints){            
        //TODO implement Offset shifting both arrays        
        g.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void fillPolygon(int[] xPoints,int[] yPoints,int nPoints){            
        g.fillPolygon(xPoints, yPoints, nPoints);
    }
    
    public void flush() {
        if(client != null)
            client.repaintNow();
    }
    
    public void end(){
        g.free();
    }
    
    public JTGLRect getClip() {
        g.getClip(clip);
        return setClip(clip.x,clip.y,clip.width,clip.height,false);        
    }
    
    public JTGLFont getDefaultFont() {
        return SWFont.getDefault(null); //TODO: get current ISurface and pass it here instead of null
    }
    
    protected void setClipInternal(int x, int y, int width, int height) {
        g.setClip(x,y, width, height);
    }
    
    protected void setColorInternal(JTGLColor color){
        g.setForeColor(color.getRed(), color.getGreen(), color.getBlue());        
        //In JTGL only fore color is used mostly for all drawings (but clearing) so let's emulate here:
        g.setBackColor(color.getRed(), color.getGreen(), color.getBlue());        
    }
    
    /*
     * This method is unused (Real call is from above method) but implementation is provided (Maybe commented out)
     */
    protected void setColorInternal(int argbColor) {
        g.setForeColor((argbColor & 0x00FF0000) >> 16, (argbColor & 0x0000FF00) >> 8 ,argbColor & 0xFF);        
        g.setBackColor((argbColor & 0x00FF0000) >> 16, (argbColor & 0x0000FF00) >> 8 ,argbColor & 0xFF); 
    }
    
    protected void setFontInternal(Object nativeFont) {
        g.setFont((Font)nativeFont);
    }
    
    protected void translateInternal(int tx, int ty) {
        g.translate(tx,ty);
    }
    
}
