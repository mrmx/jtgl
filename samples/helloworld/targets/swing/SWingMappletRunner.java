/*
 * SWingMappletRunner.java
 *
 * Created on 25 de noviembre de 2004, 17:59
 */
package samples.helloworld;

import org.jtgl.micro.impl.swing.*;

/**
 *
 * @author  mrmx
 */
public final class SWingMappletRunner extends SwingMappletRunner {
    
    /** Creates a new instance of SWingMappletRunner */
    public SWingMappletRunner() {
         runMapplet(new HelloWorldMapplet());
    }
    
    public static void main(String [] args){
        new SWingMappletRunner();
    }
    
}
