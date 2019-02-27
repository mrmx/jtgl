/*
 * NokiaFullCanvas.java
 *
 * Created on 18 de febrero de 2004, 0:14
 */

package org.jtgl.impl.midp.nokia;



import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Display;

import com.nokia.mid.ui.FullCanvas;

import org.jtgl.core.*;
import org.jtgl.input.*;
import org.jtgl.impl.midp.*;


/**
 * NokiaFullCanvas
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class NokiaFullCanvas extends FullCanvas{
    private Object lock = new Object();
    private Display display;
    private Image buffer;
    private JTGLGraphics gc;    
    private String [] availableInputControls ;
    private MidpKeyInputControl midpKeyInputControl;
    //private MidpPMouseInputControl midpMouseInputControl;
    //private MidpMouseMotionInputControl midpMouseMotionInputControl;
    
    
    /** Creates a new instance of NokiaFullCanvas */
    public NokiaFullCanvas(Display display) {        
        this.display = display;
    }

    public void showNotify(){        
        if(buffer == null)
        synchronized(lock){
            try {
                buffer = Image.createImage(getWidth(), getHeight());                
                gc = new NokiaGraphics(buffer,this);   
                gc.init();
            }catch(Throwable ex){                
            }        
            lock.notify();
        }
    }
    
    
    public JTGLGraphics getGC() {
        while(!isShown())
            synchronized(lock){
                try {            
                    lock.wait(100);
                }catch(InterruptedException ex){
                    break;
                }
            }            
        return gc;
    }
    

    public String[] getAvailableInputControls() {
        if(availableInputControls == null){
            int available = 1; //Minimun KeyInputControl available
            /*
            if(hasPointerEvents())
                available++;
            if(hasPointerMotionEvents())
                available++;
            */
            availableInputControls = new String[available];
            available = 0; //reset to reuse index            
            availableInputControls[available++] = KeyInputControl.CONTROL_CLASSNAME;
            /*
            if(hasPointerEvents())
                availableInputControls[available++] = MouseInputControl.CONTROL_CLASSNAME;
            if(hasPointerMotionEvents())
                availableInputControls[available] = MouseMotionInputControl.CONTROL_CLASSNAME;            
            */
        }
        return availableInputControls ;
    }
    
    public InputControl getInputControl(String inputControlClassName) {
        if(inputControlClassName == null)
            return null;
        if(KeyInputControl.CONTROL_CLASSNAME.equals(inputControlClassName)){
            if(midpKeyInputControl == null)
                midpKeyInputControl = new MidpKeyInputControl(this);
            return midpKeyInputControl;
        }
        return null;
    }
    
    public void keyPressed(int key){
        if(midpKeyInputControl != null)
            midpKeyInputControl.updateKeyPressed(key);
    }
    
    public void keyReleased(int key){
        //System.out.println("keyReleased = "+key);
        if(midpKeyInputControl != null)
            midpKeyInputControl.updateKeyReleased(key);
    }
    
    public void keyRepeated(int key){
        if(midpKeyInputControl != null)
            midpKeyInputControl.updateKeyRepeated(key);
    }
    

    protected final void paint(Graphics g) {
        if(buffer == null)
            return;
        g.drawImage(buffer, 0,0,  Graphics.TOP | Graphics.LEFT);
    }
}
