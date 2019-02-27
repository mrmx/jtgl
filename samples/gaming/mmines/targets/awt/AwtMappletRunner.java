/*
 * AwtMappletRunner.java
 *
 * Created on 25 de noviembre de 2004, 17:59
 */
package samples.gaming.mmines;

import org.jtgl.micro.impl.awt.*;

/**
 *
 * @author  mrmx
 */
public final class AwtMappletRunner extends AWTMappletRunner {
    
    /** Creates a new instance of AwtMappletRunner */
    public AwtMappletRunner() {
         runMapplet(new MMines());
    }
    
    public static void main(String [] args){
        new AwtMappletRunner();
    }
    
}
