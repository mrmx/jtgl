/*
 * SiemensCanvas.java
 *
 * Created on 17 de febrero de 2004, 23:50
 */

package org.jtgl.impl.midp.siemens;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Display;

import org.jtgl.core.*;
import org.jtgl.impl.midp.*;

import com.siemens.mp.game.ExtendedImage;

/**
 * Siemens Canvas
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class SiemensCanvas extends MidpCanvas {    
    private ExtendedImage ebuffer;
    
    /** Creates a new instance of MIDPCanvas */
    public SiemensCanvas(Display display) {
        super(display);        
    }


    public void flush(){
        ebuffer.blitToScreen(0,0);
    }   
    
    protected void initGraphics() throws Exception {
        int width = getWidth();
        //Make width divisible by 8 for extended Image:
        while((width % 8) != 0)
            width++;                
        Image buffer = Image.createImage(width, getHeight());
        setDblBuffer(buffer);
        ebuffer = new ExtendedImage(buffer);                      
    }
    
    protected JTGLGraphics createGraphics(Image buffer,Canvas canvas){
        return new SiemensGraphics(buffer,canvas);        
    }    
    
    
}
