/*
 * SiemensGraphics.java
 *
 * Created on 17 de febrero de 2004, 23:50
 */

package org.jtgl.impl.midp.siemens;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Canvas;

import org.jtgl.core.*;
import org.jtgl.image.JTGLImage;
import org.jtgl.impl.midp.*;

/**
 * JTGLGraphics implementation for Siemens API
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class SiemensGraphics extends MidpGraphics{    
    private SiemensCanvas client;
    /** Creates a new instance of MIDPGraphics */
    public SiemensGraphics(Image buffer,Canvas client) {
        super(buffer,client);
        this.client = (SiemensCanvas)client;
    }

    public void flush() {       
        client.flush();
    }   
}
