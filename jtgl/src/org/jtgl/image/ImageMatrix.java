/*
 * ImageMatrix.java
 *
 * Created on 4 de diciembre de 2003, 22:39
 */

package org.jtgl.image;

import org.jtgl.core.*;

/**
 * Represents a group of equal sized images arranged in a 2D Matrix within a single big source image.
 * This class serves as an access point for Sprites and TileSets (see TiledSurface class)
 * @see org.jtgl.gaming.TiledSurface
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class ImageMatrix {
    private JTGLImage imageSource;
    private int frameWidth;
    private int frameHeight;
    private int numImages;
    private int numHImages;
    private int numVImages;
    private int [] imageX;
    private int [] imageY;    
    
    /*
     * Creates a new instance of ImageMatrix 
     */
    public ImageMatrix(JTGLImage imageSource,int frameWidth,int frameHeight) {
        if(
            frameWidth < 1 || frameHeight < 1 || 
            imageSource.getWidth() % frameWidth != 0 || imageSource.getHeight() % frameHeight != 0
        )        
            throw new RuntimeException();
        init(imageSource,frameWidth,frameHeight);        
    }
    
    /**
     * Creates a new instance of ImageMatrix 
     * suited for square dimensions
     */    
    public ImageMatrix(JTGLImage imageSource,int frameWidth) {
        this(imageSource, frameWidth,frameWidth);
    }
    
    public int getImageWidth(){
        return frameWidth;
    }

    public int getImageHeight(){
        return frameHeight;
    }
    
    public int getNumImages(){
        return numImages;
    }

    public int getNumHImages(){
        return numHImages;
    }
    
    public int getNumVImages(){
        return numVImages;
    }
    /**
     * Draw a single image located by <code>imageIndex</code>
     * at location (x,y)
     */
    public void draw(JTGLGraphics g,int x,int y,int imageIndex){        
        g.drawRegion(imageSource, imageX[imageIndex], imageY[imageIndex], 
            frameWidth, frameHeight, x, y
        );              
    }

    public void draw(JTGLGraphics g,int x,int y,int offsetX,int offsetY,int imageIndex){        
        g.drawRegion(imageSource, imageX[imageIndex]+offsetX, imageY[imageIndex]+offsetY, 
            frameWidth-offsetX, frameHeight-offsetY, x, y
        );              
    }
    
    private void init(JTGLImage imageSource,int frameWidth,int frameHeight){
        this.imageSource = imageSource;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;        
        int imageWidth = imageSource.getWidth();
        int imageHeight = imageSource.getHeight();        
        numHImages = imageWidth / frameWidth;
        numVImages = imageHeight / frameHeight;        
        numImages = numHImages * numVImages;
        imageX = new int[numImages];
        imageY = new int[numImages];
        //init indexing in zig-zag order (left -> right from top -> bottom)
        //TODO: add more types of indexing
        int frame = 0;
        int x,y;        
        for(y = 0; y < imageHeight; y += frameHeight)        
            for(x = 0; x < imageWidth; x += frameWidth){
                imageX[frame] = x;
                imageY[frame++] = y;
            }                       
    }
    
}
