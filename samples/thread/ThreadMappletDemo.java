/*
 * ThreadMappletDemo.java
 *
 * Created on 22 de diciembre de 2004, 16:40
 */

package samples.thread;

import org.jtgl.core.*;
import org.jtgl.micro.*;


/**
 * ThreadMappletDemo shows the context managed JTGL thread-model usage
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class ThreadMappletDemo extends Mapplet implements JTGLThreadClient {  
    JTGLGraphics g;
    String msg;
    int x,y;
    
    public void start() {                
        g = getGraphics();
        g.begin();                        
        msg = "JTGL ThreadDemo";        
        x = (getWidth() >> 1) - (g.getFont().stringWidth(msg) >> 1);
        y = 0;        
        addThreadClient(this,MAX_PRIORITY);
    }
    
 
    public void started(){
        JTGLContext.debugMsg("Thread started!");
    }
    
    public void run(){
        g.clear();
        g.drawString(msg,x,y);
        flushGraphics();        
        delay(100);
        if(y <= getHeight())
            y += 1;
        else
            y = -g.getFont().getHeight();
    }
    
    public void stopped(){
        JTGLContext.debugMsg("Thread stopped!");
        g.end();
    }
    
    
}
