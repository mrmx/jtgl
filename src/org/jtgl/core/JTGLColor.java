/*
 * JTGLColor.java
 *
 * Created on 7 de noviembre de 2003, 11:00
 */

package org.jtgl.core;

/**
 * Manages Color info (TODO: Color conversion via JTGLColorManager)
 * ColorSpace is sRGB with alpha or opacity information and default 8bits per component
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class JTGLColor {
    /** Defines maximun component level */    
    public final static int MAX_LEVEL       =   0xFF;
    public final static int OPAQUE          =   MAX_LEVEL;
    public final static int TRANSLUCENT     =   MAX_LEVEL >> 1;
    public final static int TRANSPARENT     =   0x00;
    
    /**
     * The color white.  In the default sRGB space.
     */
    public final static JTGLColor WHITE         =   new JTGLColor(255, 255, 255);
    
    /**
     * The color light gray.  In the default sRGB space.
     */
    public final static JTGLColor LIGHT_GRAY    =   new JTGLColor(192, 192, 192);

    /**
     * The color gray.  In the default sRGB space.
     */
    public final static JTGLColor GRAY          =   new JTGLColor(128, 128, 128);
    
    /**
     * The color dark gray.  In the default sRGB space.
     */
    public final static JTGLColor DARK_GRAY     =   new JTGLColor(64, 64, 64);
    
    /**
     * The color black.  In the default sRGB space.
     */
    public final static JTGLColor BLACK         =   new JTGLColor(0, 0, 0);
        
    /**
     * The color red.  In the default sRGB space.
     */
    public final static JTGLColor RED           =   new JTGLColor(255, 0, 0);
    
    /**
     * The color pink.  In the default sRGB space.
     */
    public final static JTGLColor PINK          =   new JTGLColor(255, 175, 175);

     /**
     * The color orange.  In the default sRGB space.
     */
    public final static JTGLColor ORANGE        =   new JTGLColor(255, 200, 0);
   
    /**
     * The color yellow.  In the default sRGB space.
     */
    public final static JTGLColor YELLOW        =   new JTGLColor(255, 255, 0);

    /**
     * The color magenta.  In the default sRGB space.
     */
    public final static JTGLColor MAGENTA       =   new JTGLColor(255, 0, 255);

    /**
     * The color cyan.  In the default sRGB space.
     */
    public final static JTGLColor CYAN          =   new JTGLColor(0, 255, 255);
    
    /**
     * The color blue.  In the default sRGB space.
     */
    public final static JTGLColor BLUE          =   new JTGLColor(0, 0, 255);

    /**
     * The color green.  In the default sRGB space.
     */
    public final static JTGLColor GREEN          =   new JTGLColor(0, 255, 0);
     
    private int alpha;
    private int red;
    private int green;
    private int blue; 
    private int argb;
    private int rgb;
    private int gray;   
    
    /** Creates a new instance of JTGLColor */
    public JTGLColor(int alpha,int red,int green,int blue) {
        this.alpha = alpha & 0xFF;
        this.red = red & 0xFF;
        this.green = green & 0xFF;
        this.blue = blue & 0xFF;
        this.rgb = (this.red << 16) | (this.green << 8) | this.blue;
        this.argb = (this.alpha << 24) | rgb;
        this.gray = -1; //Greyscale not computed here @see getGray()
    }
    
    public JTGLColor(int red,int green,int blue) {
        this(OPAQUE,red,green,blue);
    }

    public JTGLColor(byte red,byte green,byte blue) {
        this(OPAQUE,(int)red,(int)green,(int)blue);
    }
    
    
    /** Returns the alpha value of the color. */
    public int getAlpha(){
        return alpha;
    }

    /** Returns the red value of the color. */
    public int getRed(){
        return red;
    }

    /** Returns the green value of the color. */
    public int getGreen(){
        return green;
    }

    /** Returns the blue value of the color. */
    public int getBlue(){
        return blue;
    }    
    /** Returns the argb value of the color. */
    public int getARGB(){
        return argb;
    }
    /** Returns the rgb value of the color. */
    public int getRGB(){
        return rgb;
    }
    
    public int getGray(){
        if(gray >= 0)   //computed already?
            return gray;        
        gray = (red * 76 + green * 150 + blue * 29) >> 8;;
        return gray;
    }
    
    public JTGLColor translucent(int level){
        return new JTGLColor(level,red,green,blue);
    }
    public JTGLColor translucent(){
        return translucent(TRANSLUCENT);
    }
    public JTGLColor transparent(){
        return translucent(TRANSPARENT);
    }
    /*
    public static int packARGB(int a,int r,int g,int b){
        a =     (a << 24) & 0xff000000;
        a |=    (r << 16) & 0x00ff0000;
        a |=    (g << 8)  & 0x0000ff00;
        a |=    (b)       & 0x000000ff; 
        return a;
    }
     */
    
    //////////////////////////////////////////// Color operations: //////////////////////////////////////////// 

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public int hashCode(){
        return argb;
    }
    
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(obj instanceof JTGLColor)
            return ((JTGLColor)obj).rgb == rgb; //equal based on rgb components
        return false;        
    }
    
    public String toString(){
        return "JTGLColor[alpha = "+alpha+", red = "+red+", green = "+green+", blue = "+blue+"]";
    }      
    /*
    private final int min(int valueA, int valueB){
        return valueA < valueB ? valueA : valueB;
    }
    
    private final int max(int valueA, int valueB){
        return valueA > valueB ? valueA : valueB;
    } 
     */   
}
