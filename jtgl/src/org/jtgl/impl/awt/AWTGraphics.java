/*
 * AWTGraphics.java
 *
 * Created on 19 de junio de 2003, 12:24
 */

package org.jtgl.impl.awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Toolkit;

import org.jtgl.core.*;
import org.jtgl.image.*;

/**
 * JTGLGraphics implementation for AWT
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class AWTGraphics extends JTGLGraphics{   
    private Component awtClient;
    private JTGLImage imgClient;    
    private Graphics g;
    private char [] charData;
    private JTGLRect tmpRect = new JTGLRect();         //To save memory reuse this
    private Rectangle tmpAWTRect = new Rectangle();    //To save memory reuse this
    private int clipX,clipY,clipWidth,clipHeight;
    private FontMetrics fontMetrics;
    private int fontMetricsMaxAscent;
    /** Creates a new instance of AWTRenderer */    
    public AWTGraphics(Graphics g,Object client) {
        this.g = g;        
        if(client instanceof Component)
            awtClient = (Component)client;
        else
        if(client instanceof JTGLImage)
            imgClient = (JTGLImage)client;
        else
            throw new IllegalArgumentException("client must be instance of Component or JTGLImage");
        charData = new char[1];
    }       
  
    
    public JTGLRect getClip(){
        Rectangle clip = g.getClipBounds(tmpAWTRect);        
        if(clip == null || clip == tmpAWTRect) {
            if(imgClient != null){                
                return setClip(0,0,imgClient.getWidth(),imgClient.getHeight(),false);            
            }
            clip = awtClient.getBounds();            
        }
        return setClip(clip.x, clip.y,clip.width, clip.height,false);
    }      
    
    public void drawImage(JTGLImage img, int x, int y) {
        if(img == null) 
            return;
        Image nativeImg = (Image)img.getNative();    
        if(nativeImg != null)            
            g.drawImage(nativeImg,x,y,awtClient);        
    }
    
    public void drawLine(int x1, int y1, int x2, int y2) {       
        g.drawLine(x1, y1 , x2 , y2 );
    }

    public void drawPolyLine(int[] xPoints, int[] yPoints, int nPoints) {
        g.drawPolyline(xPoints, yPoints, nPoints);
    }    
    
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        g.drawPolygon(xPoints, yPoints, nPoints);
    }           
  
    public void drawRect(int x, int y, int width, int height) {
        g.drawRect(x , y , width, height);
    }

    public void fillRect(int x, int y, int width, int height) {        
        g.fillRect(x, y, width, height);
    }     
  

    public void end() {
        g.dispose();
    }    
    
    public void flush() {
        if(awtClient != null)
            awtClient.repaint();
    }

    public void drawChar(char ch, int x, int y) {
        charData[0] = ch;
        g.drawChars(charData, 0, 1, x, y + fontMetricsMaxAscent);
    }
    
    public void drawChars(char [] chars, int offset, int length, int x, int y){
        g.drawChars(chars,offset,length,x, y + fontMetricsMaxAscent);
    }
    
    
    public void drawString(String str,int x,int y){
        g.drawString(str, x, y + fontMetricsMaxAscent);
    }
    

    protected void translateInternal(int tx,int ty){        
        g.translate(tx,ty);
    }  
    
    protected void setClipInternal(int x, int y, int width, int height) {
        g.setClip(x, y, width, height);
    }      

    protected void setColorInternal(int argbColor) {
        g.setColor(new Color(argbColor, true));
    }    
    
    public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest) {        
        g.copyArea(x_src, y_src, width, height, x_dest - x_src, y_dest - y_src);
    }    
    
    public void drawRegion(JTGLImage src, int x_src, int y_src, int width, int height, int x_dest, int y_dest) {
        Image imageSrc = (Image)src.getNative();
        clipX = getClipX();
        clipY = getClipY();
        clipWidth = getClipWidth();
        clipHeight = getClipHeight();                                 
        //if(isFullContextClipped()){    
        //    System.out.println("isFullContextClipped!!!!!");
            //System.out.println("clipX:"+clipX+" clipY:"+clipY+" cw:"+clipWidth+" ch:"+clipHeight);
            //g.setClip(x_dest,y_dest, width, height);
            JTGLRect.intersection(x_dest,y_dest,width,height,clipX-getTranslationX(),clipY-getTranslationY(),clipWidth,clipHeight,tmpRect);        
            if(tmpRect.isEmpty() == false){
                setClip(tmpRect);            
                g.drawImage(imageSrc, x_dest-x_src, y_dest-y_src,awtClient);                         
                setClip(clipX,clipY, clipWidth, clipHeight);        
            }
            //drawRect(x_dest,y_dest, width, height,JTGLColor.YELLOW);
            //JTGLRect.intersection(x_dest,y_dest,width,height,clipX-getTranslationX(),clipY-getTranslationY(),clipWidth,clipHeight,tmpRect);        
            //fillRect(tmpRect,JTGLColor.RED.translucent());
            /*
            System.out.println("tX:"+getTranslationX()+" tY"+getTranslationY());
            System.out.println("x_src:"+x_src+" y_src:"+y_src);
            System.out.println("x_dest:"+x_dest+" y_dest:"+y_dest+" w:"+width+" h:"+height);
            System.out.println("Clip intersection: "+tmpRect);
            
        */
        
    }    
    
    public JTGLFont getDefaultFont() {
        return AWTFont.getDefault();
    }
    
    protected void setFontInternal(Object nativeFont) {
        Font font = (Font)nativeFont;
        fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);        
        fontMetricsMaxAscent = fontMetrics.getMaxAscent();
        g.setFont(font);
    }
    
}
