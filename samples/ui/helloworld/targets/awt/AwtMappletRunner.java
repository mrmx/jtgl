/*
 * AwtMappletRunner.java
 *
 * Created on 25 de noviembre de 2004, 17:59
 */
package samples.ui.helloworld;

import org.jtgl.micro.impl.awt.*;

/**
 *
 * @author  mrmx
 */
public final class AwtMappletRunner extends AWTMappletRunner {
    
    /** Creates a new instance of AwtMappletRunner */
    public AwtMappletRunner() {
         runMapplet(new HelloWorldUI());
    }
    
    public static void main(String [] args){
        new AwtMappletRunner();
    }
    
}
