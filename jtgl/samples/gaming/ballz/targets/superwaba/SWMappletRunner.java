/*
 * AwtMappletRunner.java
 *
 * Created on 25 de noviembre de 2004, 17:59
 */
package samples.gaming.ballz;

import org.jtgl.micro.impl.superwaba.*;

/**
 *
 * @author  mrmx
 */
public final class SWMappletRunner extends SWMappletContext {
    
    /** Creates a new instance of AwtMappletRunner */
    public SWMappletRunner() {
         runMapplet(new Ballz());
    }
    
    public static void main(String [] args){
        new SWMappletRunner();
    }
    
}
