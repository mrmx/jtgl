/*
 * MIDPCanvas.java
 *
 * Created on 7 de noviembre de 2003, 18:30
 */

package org.jtgl.impl.midp2;

import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Display;

import org.jtgl.core.*;
import org.jtgl.input.*;

/**
 * Midp2 Canvas
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Midp2Canvas extends GameCanvas{
    private Display display;
    //private Image buffer;
    private JTGLGraphics gc;    
    private String [] availableInputControls ;
    private Midp2KeyInputControl midpKeyInputControl;
    //TODO: Allow preprocessing here for mouse input
    //private MidpPMouseInputControl midpMouseInputControl;
    //private MidpMouseMotionInputControl midpMouseMotionInputControl;
    
    
    /** Creates a new instance of MIDP2Canvas */
    public Midp2Canvas(Display display) {
        super(false);        
        this.display = display;
    }
    
    public JTGLGraphics getGC() {
        if(gc == null){
            gc = new Midp2Graphics(getGraphics(), this);
            gc.init();
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
                midpKeyInputControl = new Midp2KeyInputControl(this);
            return midpKeyInputControl;
        }
        return null;
    }
     
    
}
