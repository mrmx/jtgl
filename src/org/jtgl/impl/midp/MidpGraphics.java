/*
 * MIDPGraphics.java
 *
 * Created on 30 de julio de 2003, 14:17
 */

package org.jtgl.impl.midp;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Canvas;

import org.jtgl.core.*;
import org.jtgl.image.JTGLImage;

/**
 * JTGLGraphics implementation for MIDP
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class MidpGraphics extends JTGLGraphics{  
    private final static int TOP_LEFT = Graphics.TOP | Graphics.LEFT;
    private Image buffer;
    private Graphics g;
    private Canvas canvasClient;
    private JTGLRect tmpRect = new JTGLRect();    //To save memory reuse this
    
    /** Creates a new instance of MidpGraphics */
    public MidpGraphics(Image buffer, Canvas client) {
        this.canvasClient = client;
        this.g = buffer.getGraphics();
        this.buffer = buffer;
    }
    
     
    public void drawImage(JTGLImage img, int x, int y) {                
        g.drawImage((Image)img.getNative(),x,y, TOP_LEFT);        
    }
    
    public void drawLine(int xf, int yf, int xt, int yt) {
        g.drawLine(xf, yf, xt, yt);
    }
    
    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
    }
    
    public void flush() {        
        if(canvasClient != null){
            canvasClient.repaint();
            canvasClient.serviceRepaints();            
        }
    }
    
    
    public JTGLRect getClip() {
        return setClip(g.getClipX(), g.getClipY(),g.getClipWidth(), g.getClipHeight(),false);
    }


    protected void setColorInternal(int argbColor) {
        g.setColor(argbColor);
    }

    protected void setClipInternal(int x, int y, int width, int height) {
        g.setClip(x,y, width, height);
    }
    
    protected void translateInternal(int tx, int ty) {
        g.translate(tx,ty);
    }
    
    public void drawChar(char ch, int x, int y) {
        g.drawChar(ch,x,y,TOP_LEFT);
    }
    
    public void drawChars(char [] chars,int offset,int length,int x,int y){    
        g.drawChars(chars,offset,length,x,y,TOP_LEFT);
    }
    
    public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest) {        
        
        //TODO: if(!overlap){
        /*
            _drawRegion(buffer, x_src, y_src, width, height, x_dest, y_dest);         
         }else ...
         */
        //Generic copy of backbuffer to a temporal buffer
            Image temp = Image.createImage(width, height);
            Graphics tg = temp.getGraphics();
            tg.drawImage(buffer,-x_src, -y_src,TOP_LEFT);            
            g.drawImage(temp, x_dest, y_dest,TOP_LEFT);                 
            temp = null;
            System.gc();                
    }    
    
    public void drawRegion(JTGLImage src, int x_src, int y_src, int width, int height, int x_dest, int y_dest) {
        Image imageSrc = (Image)src.getNative();
        if(buffer == imageSrc)
            throw new IllegalArgumentException();        
        _drawRegion(imageSrc, x_src, y_src, width, height, x_dest, y_dest);
    }
    
    protected void _drawRegion(Image src, int x_src, int y_src, int width, int height, int x_dest, int y_dest) {
        int clipX = getClipX();
        int clipY = getClipY();
        int clipWidth = getClipWidth();
        int clipHeight = getClipHeight();   
        if(isFullContextClipped()){            
            g.setClip(x_dest,y_dest, width, height);
            g.drawImage(src, x_dest-x_src, y_dest-y_src,TOP_LEFT);                         
        }else {              
            JTGLRect.intersection(x_dest,y_dest,width,height,clipX,clipY,clipWidth,clipHeight,tmpRect);        
            g.setClip(tmpRect.getX(),tmpRect.getY(),tmpRect.getWidth(),tmpRect.getHeight());                                
            g.drawImage(src, x_dest-x_src, y_dest-y_src,TOP_LEFT);                 
        }        
        g.setClip(clipX,clipY, clipWidth, clipHeight);             
    }
    
    public JTGLFont getDefaultFont() {
        return MidpFont.getDefault();
    }
    
    protected void setFontInternal(Object nativeFont) {
        g.setFont((Font)nativeFont);
    }    
}
