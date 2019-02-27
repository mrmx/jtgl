/*
 * HelloWorldMapplet.java
 *
 * Created on 19 de diciembre de 2003, 16:59
 */

package samples.helloworld;

import org.jtgl.core.*;
import org.jtgl.micro.*;

/**
 * HelloWorld Mapplet example
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class HelloWorldMapplet extends Mapplet{  
    
    public void start() {        
        JTGLGraphics g = getGraphics();
        g.begin();                        
        String msg = "Hello World from JTGL!!";        
        g.drawString(msg, (getWidth() >> 1) - (g.getFont().stringWidth(msg) >> 1) ,getHeight() >> 1);
        flushGraphics();
        g.end();
    }
    
    
}
