/*
 * FontManager.java
 *
 * Created on 16 de diciembre de 2004, 20:03
 */

package org.jtgl.ui;

import java.util.Hashtable;
import org.jtgl.core.*;


/**
 * Manages a font collection enabling reuse.
 * @author mrmx
 */
public final class FontManager {
    private static final String DEFAULT_FONT    = "Default";
    private static Hashtable fontMap = new Hashtable();
    
    /** Creates a new instance of FontManager */
    private FontManager() {
    }
    
    public static void addFont(String name,JTGLFont font){
        JTGLContext.debugMsg("FontManager.addFont("+name+","+font+")");
        if(name != null && name.length() > 0 && font != null)
            fontMap.put(name,font);
    }
    
    public static void removeFont(String name){
        if(name != null && !name.equals(DEFAULT_FONT))
            fontMap.remove(name);
    }
    
    public static JTGLFont getFont(String name){
        if(name == null)
            name = DEFAULT_FONT;
        return (JTGLFont)fontMap.get(name);
    }
    
    public static JTGLFont getDefaultFont(){
        return getFont(null);
    }
    
    public static void setDefaultFont(JTGLFont font){
        addFont(DEFAULT_FONT, font);        
    }
    
}
