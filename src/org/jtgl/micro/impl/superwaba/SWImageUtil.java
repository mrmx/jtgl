/*
 * SWImageUtil.java
 *
 * Created on 28 de diciembre de 2004, 18:10
 */
package org.jtgl.micro.impl.superwaba;

import org.jtgl.core.JTGLContext;
import org.jtgl.image.JTGLImage;
import org.jtgl.micro.MappletContext;
import org.jtgl.micro.DefaultImageUtil;
import org.jtgl.impl.superwaba.SWImage;

import waba.fx.Image;

/**
 * Manages SuperWaba Image functions
 * @author Manuel
 */
public final class SWImageUtil extends DefaultImageUtil{
    
    /** Creates a new instance of SWImageUtil */
    public SWImageUtil(MappletContext context) {
        super(context);
    }

    public JTGLImage createScaledImage(JTGLImage imageSrc, int width, int height, boolean inmutableImage) {        
        if(imageSrc == null || (width <= 0 && height <= 0))
            return null;
        int sourceWidth = imageSrc.getWidth();
        int sourceHeight = imageSrc.getHeight();               
        JTGLContext.debugMsg("Scaling from "+sourceWidth+"x"+sourceHeight);
        JTGLContext.debugMsg("Scaling to "+width+"x"+height);                
        long start = JTGLContext.getCurrentTimeMs();
        Image swImage = (Image)imageSrc.getNative();
        Image swScaled = swImage.getScaledInstance(width,height);
        JTGLImage scaledImage = new SWImage(swScaled);
        JTGLContext.debugMsg("Total scaling time: "+(JTGLContext.getCurrentTimeMs() - start));
        return inmutableImage ? createImage(scaledImage) : scaledImage;        
    }
        
    
}
