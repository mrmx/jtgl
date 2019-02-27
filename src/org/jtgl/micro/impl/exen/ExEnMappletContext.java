/*
 * ExEnMappletContext.java
 *
 * Created on 24 de enero de 2004, 20:33
 */

package org.jtgl.micro.impl.exen;

import exen.*;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.micro.*;
import org.jtgl.impl.exen.*;

/**
 * MappletContext implementation for ExEn
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class ExEnMappletContext extends Gamelet implements MappletContext{
    private final static char [] digits = { '0','1','2','3','4','5','6','7','8','9'};
    
    /** Creates a new instance of ExEnMappletContext */
    public ExEnMappletContext() {
    }
    
    public JTGLImage createJTGLImage(String src) throws JTGLException {        
        try {
            int resourceID = parseInt(src);
            Image image = Resource.getImage(resourceID);
            return createJTGLImage(image);
        }catch(Exception ex){
            throw new JTGLException(ex);
        }
    }
    
    public JTGLImage createJTGLImage(JTGLImage image) {
        return image;
    }
    
    public JTGLImage createJTGLImage(int width, int height) {
        return createJTGLImage(Image.createImage(width,height));
    }
    
    public JTGLImage createJTGLImage(byte[] imageData, int imageOffset, int imageLength) {
        return null; // not available!
    }
    
        
    public void delay(Mapplet mapplet, int ms) {
    }
    
    public Object get(Mapplet mapplet, String property) {
        return null;
    }
    
    public String[] getAvailableInputControls(Mapplet mapplet) {
        return null;
    }
    
    public int getDisplayColors(Mapplet mapplet) {
        return -1;
    }
    
    public InputControl getInputControl(Mapplet mapplet, String inputControlClassName) {
        return null;
    }
    
    public JTGLGraphics getJTGLGraphics(Mapplet mapplet) {
        return null;
    }
    
    public void handleException(Mapplet mapplet, Exception ex, String title) {
    }
    
    public void notifyKill(Mapplet mapplet) {
    }    
    
    public void runMapplet(Mapplet mapplet) {
    }
    
    private JTGLImage createJTGLImage(Image image){
        return new ExEnImage(image);
    }
    
    private final int parseInt(String str) throws Exception{
        if(str == null)
            throw new Exception("Illegal argument : "+str);
        char [] chrs = str.toCharArray();
        int pos = 1;
        int c = chrs.length;
        int i,parsed;
        parsed = 0;
        while(c > 0){
            char ch = chrs[--c];
            for(i = 0 ; i < digits.length ;i++)
                if(ch == digits[i]) 
                    break;
            if(i == digits.length)
                throw new Exception("Illegal argument : "+str);
            parsed += i * pos;
            pos *= 10;
            System.out.println(parsed);
        }
        return parsed;
    }
}
