/*
 * MIDPCanvas.java
 *
 * Created on 7 de noviembre de 2003, 18:30
 */

package org.jtgl.impl.midp;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Display;

import org.jtgl.core.*;
import org.jtgl.input.*;

/**
 * Midp Canvas
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class MidpCanvas extends Canvas{
    private final static int TOP_LEFT = Graphics.TOP | Graphics.LEFT;
    private Object lock = new Object();
    private Display display;
    private Image buffer;
    private JTGLGraphics gc;    
    private String [] availableInputControls ;
    private MidpKeyInputControl midpKeyInputControl;
    //TODO: Allow preprocessing here for mouse input
    //private MidpMouseInputControl midpMouseInputControl;
    //private MidpMouseMotionInputControl midpMouseMotionInputControl;
    
    
    /** Creates a new instance of MIDPCanvas */
    public MidpCanvas(Display display) {        
        this.display = display;
    }

    public void showNotify(){        
        if(buffer == null)
        synchronized(lock){
            try {
                initGraphics();
                gc = createGraphics(buffer,this);   
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
        g.drawImage(buffer, 0,0,  TOP_LEFT);
    }    

    protected final void setDblBuffer(Image buffer){
        this.buffer = buffer;
    }
    
    protected void initGraphics() throws Exception {        
        Image buffer = Image.createImage(getWidth(), getHeight());
        setDblBuffer(buffer);        
    }
    
    protected JTGLGraphics createGraphics(Image buffer,Canvas canvas){
        return new MidpGraphics(buffer,canvas);        
    }    
}
