/*
 * HelloWorldUI.java
 *
 * Created on 16 de diciembre de 2004, 11:22
 */

package samples.ui.helloworld;

import org.jtgl.core.*;
import org.jtgl.micro.*;
import org.jtgl.ui.*;

/**
 *
 * @author mrmx
 */
public class HelloWorldUI extends Mapplet{
    
    MWindow main;
    MLabel label;
    
    /** Creates a new instance of HelloWorldUI */
    public HelloWorldUI() {
        main = new MWindow();        
        main.add(label = new MLabel("HelloWorld JTGLui"));
        label.setLocation(10,10);
        label.inverse();
    }
    
    public void start(){
        JTGLGraphics g = getGraphics();
        FontManager.setDefaultFont(g.getFont());
        main.pack();
        main.paint(g);   
    }
    
}
