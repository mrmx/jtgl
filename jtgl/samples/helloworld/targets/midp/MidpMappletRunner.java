/*
 * MidpMappletRunner.java
 *
 * Created on 12 de enero de 2004, 16:34
 */

package samples.helloworld;

import org.jtgl.micro.impl.midp.*;

/**
 * Base MIDP context and launcher for any Mapplet
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class MidpMappletRunner extends MidpMappletContext{
    
    /** Creates a new instance of MidpMappletRunner */
    public MidpMappletRunner() {
        runMapplet(new HelloWorldMapplet());
    }
    
}
