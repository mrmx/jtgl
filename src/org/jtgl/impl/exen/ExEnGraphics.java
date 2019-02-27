/*
 * ExEnGraphics.java
 *
 * Created on 7 de enero de 2004, 11:27
 */

package org.jtgl.impl.exen;

import exen.*;

import org.jtgl.core.*;
import org.jtgl.image.*;

/**
 * JTGLGraphics implementation for ExEn.
 * (Needed Infusio's ExEn SDK to compile - http://developer.in-fusio.com/ - )
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class ExEnGraphics extends JTGLGraphics{
    private final static int TOP_LEFT = Graphics.TOP | Graphics.LEFT;
    private Image buffer;    
    private Graphics g;
    private Gamelet client;
    private JTGLRect tmpRect = new JTGLRect();    //To save memory reuse this
    private char [] charData;    
    private static ExEnFont defaultFont;   
    /** Creates a new instance of ExEnGraphics */
    public ExEnGraphics(Image buffer,Gamelet client) {
        this.buffer = buffer;
        this.g = buffer.getGraphics();
        this.client = client;
        if(client != null && !client.isColor())
            g.setNormalVideo();
        charData = new char[1];        
    }

    
    public void drawChar(char ch, int x, int y) {
        charData[0] = ch;
        g.drawChars(charData,0, 1,x,y,TOP_LEFT);     
    }
    
    public void drawChars(char [] chars,int offset,int length,int x,int y){    
        g.drawChars(chars,offset,length,x,y,TOP_LEFT);
    }
        
    
    public void drawImage(JTGLImage img, int x, int y) {
        g.drawImage((Image)img.getNative(),x,y,TOP_LEFT);
    }
    
    public void drawLine(int xf, int yf, int xt, int yt) {
        g.drawLine(xf, yf, xt, yt);        
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
        
    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x,y, width, height);
    }
    
    public void flush() {
        if(client == null)
            return ;
        client.repaint();
        client.update();
    }
    
    public JTGLRect getClip() {
        return setClip(g.getClipX(), g.getClipY(),g.getClipWidth(), g.getClipHeight(),false);        
    }
    
    protected void setClipInternal(int x, int y, int width, int height) {
        g.setClip(x, y, width, height);
    }
    
    protected void setColorInternal(int argbColor) {
        g.setColor(argbColor);
    }
    
    protected void translateInternal(int tx, int ty) {
    }
    
    public JTGLFont getDefaultFont() {
        if(defaultFont == null)
            defaultFont = new ExEnFont(g.getCharWidth(),g.getCharHeight());
        return defaultFont;
    }
    
    public void setFontInternal(Object nativeFont) {
    }
    
}
