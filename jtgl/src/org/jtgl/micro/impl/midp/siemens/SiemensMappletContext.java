/*
 * SiemensMappletContext.java
 *
 * Created on 17 de febrero de 2004, 23:34
 */

package org.jtgl.micro.impl.midp.siemens;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Canvas;
import org.jtgl.micro.*;
import org.jtgl.impl.midp.siemens.*;
import org.jtgl.micro.impl.midp.*;

/**
 * MappletContext implementation for Siemens mobile API
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class SiemensMappletContext extends MidpMappletContext{
    
    /** Creates a new instance of SiemensMappletContext */
    public SiemensMappletContext() {
    }
    
    protected Canvas createCanvas(Display display){
        return new SiemensCanvas(display);
    }     
}
