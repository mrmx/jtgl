/*
 * DefaultImageUtil.java
 *
 * Created on 25 de diciembre de 2003, 19:52
 */

package org.jtgl.micro;

import org.jtgl.core.*;
import org.jtgl.micro.*;
import org.jtgl.image.*;

/**
 * Default ImageUtil implementation
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class DefaultImageUtil extends ImageUtil{
    
    /** Creates a new instance of MidpImageUtil */
    public DefaultImageUtil(MappletContext context) {
        super(context);
    }
    
    public JTGLImage createScaledImage(JTGLImage imageSrc, int width, int height, boolean inmutableImage) {
        if(imageSrc == null || (width <= 0 && height <= 0))
            return null;
        int sourceWidth = imageSrc.getWidth();
        int sourceHeight = imageSrc.getHeight();               

        JTGLContext.debugMsg("Scaling from "+sourceWidth+"x"+sourceHeight);
        JTGLContext.debugMsg("Scaling to "+width+"x"+height);        
        
        long start = mappletContext.getCurrentTimeMs();
        
        JTGLImage tmp = createImage(width, sourceHeight);
        JTGLGraphics g = tmp.getGraphics();
        g.begin();

        int step = (sourceWidth << 16) / width;
        int i = 0,inc = 0;        
        for (; i < width; i++){
            g.setClip(i, 0, 1, sourceHeight);
            g.drawImage(imageSrc, i - (inc >> 16), 0);
            inc += step;
        }
        g.end();
        JTGLImage scaledImage = createImage(width, height);
        g = scaledImage.getGraphics();
        g.begin();
        step = (sourceHeight << 16) / height;
        i = inc = 0;
        for (; i < height; i++){
            g.setClip(0, i, width, 1);
            g.drawImage(tmp, 0, i - (inc >> 16));
            inc += step;   
        }        
        g.end();        
        JTGLContext.debugMsg("Total scaling time: "+(mappletContext.getCurrentTimeMs() - start));
        return inmutableImage ? createImage(scaledImage) : scaledImage;        
    }
    
    public int getMaxScalingQuality() {
        return 0;
    }
    
    public int getMinScalingQuality() {
        return 0;
    }
    
    public void setMaxScalingQuality(int quality) {
    }    
}