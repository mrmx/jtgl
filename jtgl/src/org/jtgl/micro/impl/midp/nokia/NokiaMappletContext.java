/*
 * NokiaMappletContext.java
 *
 * Created on 18 de febrero de 2004, 0:19
 */

package org.jtgl.micro.impl.midp.nokia;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Canvas;

import org.jtgl.micro.*;
import org.jtgl.input.*;
import org.jtgl.impl.midp.nokia.*;
import org.jtgl.micro.impl.midp.*;


/**
 * MappletContext implementation for Nokia mobile API
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class NokiaMappletContext extends MidpMappletContext{
    
    /** Creates a new instance of NokiaMappletContext */
    public NokiaMappletContext() {
    }
    
    protected Canvas createCanvas(Display display){
        return new NokiaFullCanvas(display);
    }      
    
    public String[] getAvailableInputControls(Mapplet mapplet) {
        return ((NokiaFullCanvas)getCanvas()).getAvailableInputControls();
    }    
   
    public InputControl getInputControl(Mapplet mapplet,String inputControlClassName) {
        return ((NokiaFullCanvas)getCanvas()).getInputControl(inputControlClassName);
    }      
}
