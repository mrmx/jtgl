/*
 * ImageUtil.java
 *
 * Created on 25 de diciembre de 2003, 19:32
 */

package org.jtgl.micro;

import org.jtgl.image.JTGLImage;

/**
 * Provides various image manipulation methods (Scaling at this moment)
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class ImageUtil {    
    MappletContext mappletContext;
    /** Creates a new instance of ImageUtil */
    public ImageUtil(MappletContext mappletContext) {
        this.mappletContext = mappletContext;
    }
    
    public abstract int getMaxScalingQuality();
    public abstract int getMinScalingQuality();
    public abstract void setMaxScalingQuality(int quality);
    public abstract JTGLImage createScaledImage(JTGLImage imageSrc,int width,int height,boolean inmutableImage);
    
    public final JTGLImage createScaledImage(JTGLImage imageSrc,int width,int height){
        return createScaledImage(imageSrc, width, height,true);
    }
    
    protected JTGLImage createImage(int width,int height){
        return mappletContext.createJTGLImage(width, height);
    }    
    
    protected JTGLImage createImage(JTGLImage image){
        return mappletContext.createJTGLImage(image);
    }    
}
