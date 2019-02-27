/*
 * MIDP2Graphics.java
 *
 * Created on 29 de Enero de 2004, 00:27
 */

package org.jtgl.impl.midp2;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.*;

import org.jtgl.core.*;
import org.jtgl.image.JTGLImage;
import org.jtgl.impl.midp.*;

/**
 * JTGLGraphics implementation for MIDP2
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Midp2Graphics extends JTGLGraphics{
    private final static int TOP_LEFT = Graphics.TOP | Graphics.LEFT;
    private Graphics g;
    private GameCanvas canvasClient;
    private JTGLRect tmpRect;    //To save memory reuse this
    
    /** Creates a new instance of MIDPGraphics */
    public Midp2Graphics(Graphics g, Object client) {
        this.canvasClient = (client instanceof GameCanvas) ? (GameCanvas)client : null;
        this.g = g;
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
        if(canvasClient != null)
            canvasClient.flushGraphics();   
        
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
        g.copyArea(x_src, y_src, width, height, x_dest, y_dest, TOP_LEFT);
    }    
    
    public void drawRegion(JTGLImage src, int x_src, int y_src, int width, int height, int x_dest, int y_dest) {
        Image imageSrc = (Image)src.getNative();
        g.drawRegion(imageSrc, x_src, y_src, width, height, Sprite.TRANS_NONE, x_dest , y_dest,TOP_LEFT );    
    }
    
    
    public JTGLFont getDefaultFont() {
        return MidpFont.getDefault();
    }
    
    protected void setFontInternal(Object nativeFont) {
        g.setFont((Font)nativeFont);
    }    
}
