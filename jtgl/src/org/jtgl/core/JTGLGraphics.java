/*
 * Renderer.java
 *
 * Created on 19 de junio de 2003, 11:23
 */

package org.jtgl.core;

import org.jtgl.image.JTGLImage;


/**
 * Graphics context where all drawing operations are performed
 * @author Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class JTGLGraphics {    
    /** Current foreground color */    
    protected JTGLColor _currColor;
    /** Current background color */    
    protected JTGLColor _currBGColor;    
    private JTGLColor oldColor;    
    private int tx,ty;
    private int []triX;
    private int []triY;
    private int contextWidth;
    private int contextHeight;
    private JTGLRect clipRect;
    /**
     * <code>fullContextClipped</code> is true if current context is fully clipped
     * to o,o,contextWidth,contextHeight, or false elsewhere. Such information could
     * be used to optimize certains operations. This value is updated when <code>begin()</code>
     * is called or every call to <code>setClip</code>
     * @see setClip
     * @see isFullContextClipped
     */
    private boolean fullContextClipped;
    //BEWARE: Just one slot for save/restore clipping window:    
    private JTGLRect oldClipRect;
    
    private JTGLFont font;    
   
    /** Creates a new instance of JTGLGraphics*/
    public JTGLGraphics() {
        this.tx = this.ty = 0;     
        //Triangle vertex array allocation:
        triX = new int[3];
        triY = new int[3]; 
        _currBGColor = JTGLColor.WHITE;
        _currColor = JTGLColor.BLACK;
        contextWidth = contextHeight = -1; //Signal uninitialized
    }    

    /** 
     * Initializes this graphics context.
     * This *MUST* be called once before any operation 
     */        
    public void init(){
        if(contextWidth == contextHeight && contextWidth == -1){
            JTGLRect initialClipRect = getClip();
            contextWidth = initialClipRect.width;
            contextHeight = initialClipRect.height;
            fullContextClipped = true;
        }
        setColor(_currColor); //default initial color
        clear(); //default initial background        
        setFont(null);//set default font        
    }
    
    /** 
     * Signals the begining of usage of this graphics context. 
     */    
    public void begin(){
    }    
    
    /** 
     * Signals the end of usage of this graphics context. 
     * Implementations must release any acquired resources.
     */    
    public void end(){
    }
    
    /** Flush all pending drawing operations to destination such as screen. */    
    public abstract void flush();
    
    

    ////////////////////////////////////////////// TEXT RENDERING //////////////////////////////////////////////         
    
    /**
     * @param font
     */    
    public final void setFont(JTGLFont font){
        this.font = font == null ? getDefaultFont() : font;
        if(this.font != null)
            setFontInternal(this.font.getNativeFont());
    }
    
    protected abstract void setFontInternal(Object nativeFont);
    
    /**
     * @return
     */    
    public final JTGLFont getFont(){
        if(font == null)
            setFont(null);//sets default font
        return font;
    }
    
    /**
     * Changes current font's size to a fixed font size defined by <code>JTGLFont</code>'s constants:
     *  <code>JTGLFont.SIZE_SMALL</code>
     *  <code>JTGLFont.SIZE_MEDIUM</code>
     *  <code>JTGLFont.SIZE_LARGE</code>
     * Subsclasses of <code>JTGLFont</code> should return a minimun integer array of three predefined sizes.
     */
    public final void setFontSize(int sizeIndex){
        if(sizeIndex < JTGLFont.SIZE_SMALL || sizeIndex > JTGLFont.SIZE_LARGE)
            return ;
        JTGLFont currentFont = getFont();
        int [] predefinedSizes = currentFont.getSizes();
        if(predefinedSizes == null)// can not set a predefined size !
            return ;
        int size = predefinedSizes[ sizeIndex ];
        setFont(getDefaultFont().createFont(currentFont.getName(),currentFont.getStyle(),size));        
    }
    
    public abstract JTGLFont getDefaultFont();
    
    /**
     * @param ch
     * @param x
     * @param y
     */    
    public abstract void drawChar(char ch,int x,int y);
    
    /**
     * @param chars
     * @param offset
     * @param length
     * @param x
     * @param y
     */    
    public void drawChars(char [] chars,int offset, int length,int x,int y){            
        char ch;
        while(offset < length){
            ch = chars[offset++];
            drawChar(ch , x , y);
            x += font.charWidth(ch);
        }
    }
    
    /**
     * @param str
     * @param x
     * @param y
     */    
    public void drawString(String str, int x, int y){
        if(str == null || str.length() == 0)
            return ;
        char [] chars = str.toCharArray();
        drawChars(chars,0,chars.length, x, y);
    }    
    
    ////////////////////////////////////////////// PRIMITIVES ////////////////////////////////////////////// 
    /**
     * @param tx
     * @param ty
     */    
    public final void translate(int tx,int ty){
        this.tx += tx;
        this.ty += ty;
        translateInternal(tx,ty);
    }
    
    /**
     * @param tx
     * @param ty
     */    
    protected abstract void translateInternal(int tx,int ty);
    
    /**
     * @return
     */    
    public final int getTranslationX(){
        return tx;
    }
    
    /**
     * @return
     */    
    public final int getTranslationY(){
        return ty;
    }
    
    /**
     * @param x
     * @param y
     */    
    public void setOrigin(int x,int y){
        translate(x - getTranslationX() ,  y - getTranslationY());        
    }
    
    /** 
     * Gets original context width independent of actual clipping width.
     * @return width of context
     */    
    public final int getWidth(){
        return contextWidth;
    }

    /**
     * Gets original context height independent of actual clipping height.
     * @return height of context
     */    
    public final int getHeight(){
        return contextHeight;
    }
    
    /**
     * @param clip
     * @return
     */    
    public JTGLRect setClip(JTGLRect clip){
        if(clip == null)         
            clipReset();            
        else
            setClip(clip.x, clip.y, clip.width, clip.height,true);            
        return clipRect;
    }
    
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     * @param updateInternal
     * @return
     */    
    public JTGLRect setClip(int x, int y, int width, int height,boolean updateInternal) {
        if(clipRect == null)
            clipRect = new JTGLRect();
        clipRect.copy(x, y,width, height);
        fullContextClipped = (x == 0 && y == 0 && width == contextWidth && height == contextHeight);
        if(updateInternal)
            setClipInternal(x,y,width,height);
        return clipRect;            
    }

    /**
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */    
    public JTGLRect setClip(int x, int y, int width, int height){
        return setClip(x, y, width, height,true);
    }
    
    
    
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */    
    protected abstract void setClipInternal(int x, int y, int width, int height);
    
    /**
     * @return
     */    
    public abstract JTGLRect getClip();    
    
    public final boolean isFullContextClipped(){
        return fullContextClipped;
    }
    
    public final void clipReset(){
        setClip(0, 0, contextWidth, contextHeight,true);
    }
    
    
    /**
     * @return
     */    
    public final int getClipX(){
        if(clipRect == null)
            return getClip().x;
        return clipRect.x;
    }

    /**
     * @return
     */    
    public final int getClipY(){
        if(clipRect == null)
            return getClip().y;
        return clipRect.y;
    }

    /**
     * @return
     */    
    public final int getClipWidth(){
        if(clipRect == null)
            return getClip().width;
        return clipRect.width;
    }

    /**
     * @return
     */    
    public final int getClipHeight(){
        if(clipRect == null)
            return getClip().height;
        return clipRect.height;
    }
    
    public final void saveClip(){
        if(oldClipRect == null)
            oldClipRect = new JTGLRect(getClip());
        else
            oldClipRect.copy(getClip());
    }
    
    public final void restoreClip(){
        setClip(oldClipRect.x, oldClipRect.y,oldClipRect.width,oldClipRect.height,true);
    }
    
    /**
     * @param r
     * @param g
     * @param b
     */    
    public void setColor(int r,int g,int b){        
        setColor( new JTGLColor( JTGLColor.OPAQUE ,r,g,b) );
    }
    /**
     * @param a
     * @param r
     * @param g
     * @param b
     */    
    public void setColor(int a,int r,int g,int b){        
        setColor( new JTGLColor(a,r,g,b) );
    }
    
    /**
     * @return
     */    
    public JTGLColor getBackgroundColor() {        
        return _currBGColor;
    }
    
    /**
     * Background color is used when clearing this context and for auxiliary color.
     * @param color
     */    
    public void setBackgroundColor(JTGLColor color) {        
        _currBGColor = color;        
    }
    
    /**
     * @param color
     */    
    public void setColor(JTGLColor color) {
        _currColor = color;
        setColorInternal(color);
    }
    
    /**
     * @return
     */    
    public JTGLColor getColor() {
        return _currColor;
    }
    
    /**
     * @param color
     */    
    public final void saveColor(JTGLColor color){
        oldColor = getColor(); //Save current color
        setColor(color);        
    }
    
    public final void restoreColor(){
        setColor(oldColor);
    }
    
    /**
     * Sets the internal foregound color
     * Subclases may overide this methof if another color packaging or translation (from JTGLColor) is needed.
     * @param color
     */    
    protected void setColorInternal(JTGLColor color){
        setColorInternal(color.getARGB());
    }
    /**
     * Sets the internal foreground color packed in ARGB format
     * @param argbColor
     */    
    protected abstract void setColorInternal(int argbColor);
      
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color
     */    
    public void clear(int x , int y, int width,int height,JTGLColor color){
        saveColor(color);
        fillRect(x, y, width, height);
        restoreColor();
    }     
    
    /**
     * @param rect
     */    
    public void clear(JTGLRect rect){
        clear(rect.x,rect.y,rect.width, rect.height, getBackgroundColor());
    }
    /**
     * @param rect
     * @param color
     */    
    public void clear(JTGLRect rect,JTGLColor color){
        clear(rect.x,rect.y,rect.width, rect.height, color);
    }
        
    public void clear(){
        clear(getClip());
    }

    /**
     * @param color
     */    
    public void clear(JTGLColor color){
        clear(getClip(),color);
    }    
    
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */    
    public void clear(int x , int y, int width,int height){
        clear(x, y, width, height , getBackgroundColor());        
    }    
    /**
     * @param x
     * @param y
     * @param color
     */    
    public void setPixel(int x,int y,JTGLColor color){
        drawLine(x,y,x,y,color);
    }
    /**
     * @param x
     * @param y
     * @return
     */    
    public int getPixel(int x, int y) {
        return -1;
    }  
    /**
     * @param xf
     * @param yf
     * @param xt
     * @param yt
     */    
    public abstract void drawLine(int xf,int yf,int xt,int yt);    
    /**
     * @param xf
     * @param yf
     * @param xt
     * @param yt
     * @param color
     */    
    public void drawLine(int xf,int yf,int xt,int yt,JTGLColor color){  
        saveColor(color);
        drawLine(xf, yf, xt, yt);
        restoreColor();        
    }
    /**
     * @param xPoints
     * @param yPoints
     * @param offset
     * @param nPoints
     * @param closePath
     */    
    public void drawPolyLine(int[] xPoints,int[] yPoints,int offset,int nPoints,boolean closePath){        
        int i = 0;
        for(; i < nPoints - 1; )
            drawLine(xPoints[offset + i], yPoints[offset + i++],xPoints[i], yPoints[i]);
        if(closePath && ( (xPoints[offset] != xPoints[i]) && (yPoints[offset] != yPoints[i]) ) )
            drawLine(xPoints[offset], yPoints[offset],xPoints[i], yPoints[i]);
    }
    /**
     * @param xPoints
     * @param yPoints
     * @param nPoints
     * @param closePath
     */    
    public void drawPolyLine(int[] xPoints,int[] yPoints,int nPoints,boolean closePath){            
        drawPolyLine(xPoints, yPoints,0,  nPoints,closePath);
    }
    /**
     * @param xPoints
     * @param yPoints
     * @param nPoints
     */    
    public void drawPolyLine(int[] xPoints,int[] yPoints,int nPoints){
        drawPolyLine(xPoints, yPoints,0,  nPoints,false);
    }    
    /**
     * @param xPoints     
     * @param yPoints
     * @param offset
     * @param nPoints
     */    
    public void drawPolygon(int[] xPoints,int[] yPoints,int offset,int nPoints){            
        drawPolyLine(xPoints, yPoints,offset, nPoints,true);
    }
    /**
     * @param xPoints
     * @param yPoints
     * @param nPoints
     */    
    public void drawPolygon(int[] xPoints,int[] yPoints,int nPoints){
        drawPolyLine(xPoints, yPoints,0,  nPoints,true);
    }   

    /**
     * @param xPoints     
     * @param yPoints
     * @param offset     
     * @param nPoints
     */    
    public void fillPolygon(int[] xPoints,int[] yPoints,int offset,int nPoints){            
        //TODO: Real Fill on fb impl
        drawPolygon(xPoints, yPoints, offset, nPoints);
    }
    
    /**
     * @param xPoints
     * @param yPoints
     * @param nPoints
     */    
    public void fillPolygon(int[] xPoints,int[] yPoints,int nPoints){                
        fillPolygon(xPoints,yPoints,0, nPoints);
    }
    
    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */    
    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        triX[0] = x1;
        triX[1] = x2;
        triX[2] = x3;
        triY[0] = y1;
        triY[1] = y2;
        triY[2] = y3;
        drawPolygon(triX, triY, 3);            
    }
    
    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */    
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        //TODO: Real Fill on fb impl
        drawTriangle(x1, y1, x2, y2, x3, y3);
    }
    
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */    
    public void drawRect(int x,int y,int width,int height){        
	if ((width < 0) || (height < 0)) 
	    return;	
	if (height == 0 || width == 0) {
	    drawLine(x, y, x + width, y + height);
	} else {
	    drawLine(x, y, x + width - 1, y);
	    drawLine(x + width, y, x + width, y + height - 1);
	    drawLine(x + width, y + height, x + 1, y + height);
	    drawLine(x, y + height, x, y + 1);
	} 
    }
    
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color
     */    
    public void drawRect(int x,int y,int width,int height,JTGLColor color){        
        saveColor(color);
        drawRect(x, y, width, height);
        restoreColor();
    }
    
    /**
     * @param rect
     */    
    public void drawRect(JTGLRect rect){
        drawRect(rect.x,rect.y,rect.width, rect.height);
    }
    
    /**
     * @param rect
     * @param color
     */    
    public void drawRect(JTGLRect rect,JTGLColor color){
        drawRect(rect.x,rect.y,rect.width, rect.height, color);        
    }
    
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */    
    public abstract void fillRect(int x , int y, int width,int height);
    
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color
     */    
    public void fillRect(int x , int y, int width,int height,JTGLColor color){
        saveColor(color);
        fillRect(x, y, width, height);
        restoreColor();
    }

    /**
     * @param rect
     */    
    public void fillRect(JTGLRect rect){
        fillRect(rect.x,rect.y,rect.width, rect.height);
    }
    
    /**
     * @param rect
     * @param color
     */    
    public void fillRect(JTGLRect rect,JTGLColor color){
        fillRect(rect.x,rect.y,rect.width, rect.height,color);
    }
    
    
    /**
     * @param img
     * @param x
     * @param y
     */    
    public abstract void drawImage(JTGLImage img,int x,int y);
    /**
     * @param src
     * @param x_src
     * @param y_src
     * @param width
     * @param height
     * @param x_dest
     * @param y_dest
     */    
    public abstract void drawRegion(JTGLImage src,int x_src,int y_src,int width,int height,int x_dest,int y_dest);
    /**
     * @param x_src
     * @param y_src
     * @param width
     * @param height
     * @param x_dest
     * @param y_dest
     */    
    public abstract void copyArea(int x_src,int y_src,int width,int height,int x_dest,int y_dest);
    
    /**
     * Change or wraps a native graphics context (Used whithin a non-double-buffered scenario)
     */
    protected void setNativeGraphics(Object nativeGraphics){
        
    }
    
}
