/*
 * JTGLRect.java
 *
 * Created on 25 de julio de 2003, 14:56
 */

package org.jtgl.core;

/**
 * Rectangle primitive
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class JTGLRect {
    public int x,y;
    public int width,height;
    
    /** Creates a new instance of JTGLRect */
    public JTGLRect(int x,int y,int width,int height) {
        copy(x,y,width, height);
    }
    
    public JTGLRect(int width,int height) {
        this(0,0, width, height);
    }
    
    public JTGLRect(){
        this(0,0, 1,1);
    }
    
    public JTGLRect(JTGLRect rect){
        copy(rect);
    }
    
    public void setWidth(int width){
        this.width = width < 0 ? 0 : width;
    }
    
    public void setHeight(int height){
        this.height = height < 0 ? 0 : height;
    }
    
    public void setLocation(int x,int y){
        this.x = x;
        this.y = y;
    }
    
    
    public void translate(int x,int y){
        this.x += x;
        this.y += y;
    }
    
    public void center(JTGLRect container){
        center(container.x, container.y, container.width, container.height);
    }
    
    public void center(int offsetX,int offsetY,int width,int height){
        x = offsetX + (width >> 1) - (this.width >> 1);
        y = offsetY + (height >> 1) - (this.height >> 1);
    }
    
    
    public int getX(){
        return x;
    }
    
    
    public int getY(){
        return y;
    }
    
    public void empty(){
        width = height = 0;
    }
    
    public boolean isEmpty(){
        return width == 0 && height == 0;
    }    
    
    public void setDimension(int width,int height){
        setWidth(width);
        setHeight(height);
    }    
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }

    
    ////////////////////////////////////////////    Operations:    ////////////////////////////////////////////
    public JTGLRect addDimension(int w,int h){
        return new JTGLRect(x,y,width + w, height + h);
    }
    
    public JTGLRect addDimension(int factor){
        return addDimension(factor,factor);
    }
    
    public JTGLRect mulDimension(int w,int h){
        return new JTGLRect(x,y,width * w, height * h);
    }
    
    public JTGLRect mulDimension(int factor){
        return mulDimension(factor, factor);
    }
    
    public boolean contains(int px,int py){
        return contains(px,py, x, y,x + width,y + height);
    }
    
    public static final boolean contains(int x,int y,int r1x,int r1y,int r2x,int r2y){
        return x >= r1x && x <= r2x && y >= r1y && y <= r2y;
    }
    
    public boolean intersects(JTGLRect r) {
        int tw = this.width;
        int th = this.height;
        int rw = r.width;
        int rh = r.height;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = this.x;
        int ty = this.y;
        int rx = r.x;
        int ry = r.y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
        (rh < ry || rh > ty) &&
        (tw < tx || tw > rx) &&
        (th < ty || th > ry));
    }
    
    
    
    public static final boolean intersects(int r1x,int r1y,int r1w,int r1h,
                                           int r2x,int r2y,int r2w,int r2h
                                           ){
        if(
                r2y > r1y + r1h || // check r2's top side with r1's bottom side                
                r2x + r2w < r1x || // check r2's right side with r1's left side
                r2y + r2h < r1y || // check r2's bottom side with r1's top side                
                r2x > r1x + r1w    // check r2's left side with r1's right side                
        )
            return false;
        return true;        
    }
    
    /**
     * Computes the intersection of this <code>JTGLRect</code> with the
     * specified <code>Rectangle</code>. Returns a new <code>JTGLRect</code>
     * that represents the intersection of the two rectangles.
     * If the two rectangles do not intersect, the result will be
     * an empty rectangle.
     *
     * @param     r   the specified <code>Rectangle</code>
     * @return    the largest <code>Rectangle</code> contained in both the
     *            specified <code>Rectangle</code> and in
     *		  this <code>Rectangle</code>; or if the rectangles
     *            do not intersect, an empty rectangle.
     */
    public JTGLRect intersection(JTGLRect r) {
        return intersection(x,y,width, height, r.x,r.y,r.width, r.height);
    }
    
    public static JTGLRect intersection(int r1x,int r1y,int r1width,int r1height,
                                        int r2x,int r2y,int r2width,int r2height,JTGLRect result){
        
        long tx2 = r1x; tx2 += r1width;
        long ty2 = r1y; ty2 += r1height;
        long rx2 = r2x; rx2 += r2width;
        long ry2 = r2y; ry2 += r2height;
        if (r1x < r2x) r1x = r2x;
        if (r1y < r2y) r1y = r2y;
        if (tx2 > rx2) tx2 = rx2;
        if (ty2 > ry2) ty2 = ry2;
        tx2 -= r1x;
        ty2 -= r1y;
        if (tx2 < Integer.MIN_VALUE) tx2 = 0;
        if (ty2 < Integer.MIN_VALUE) ty2 = 0;
        if(result == null)
            result = new JTGLRect(r1x, r1y, (int) tx2, (int) ty2);
        else
            result.copy(r1x, r1y, (int) tx2, (int) ty2);
        return result;                                            
    }

    public static JTGLRect intersection(int r1x,int r1y,int r1width,int r1height,int r2x,int r2y,int r2width,int r2height){
        return intersection(r1x,r1y,r1width,r1height,r2x,r2y,r2width,r2height,null);
    }
    
    /**
     * Computes the union of this <code>JTGLRect</code> with the
     * specified <code>JTGLRect</code>. Returns a new
     * <code>Rectangle</code> that
     * represents the union of the two rectangles
     * @param r the specified <code>JTGLRect</code>
     * @return    the smallest <code>JTGLRect</code> containing both
     *		  the specified <code>JTGLRect</code> and this
     *		  <code>JTGLRect</code>.
     */
    public JTGLRect union(JTGLRect r) {
        int x1 = Math.min(x, r.x);
        int x2 = Math.max(x + width, r.x + r.width);
        int y1 = Math.min(y, r.y);
        int y2 = Math.max(y + height, r.y + r.height);
        return new JTGLRect(x1, y1, x2 - x1, y2 - y1);
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void copy(JTGLRect from){
        if(from != null)
            copy(from.x, from.y,from.width, from.height);
    }
    
    public void copy(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public JTGLRect getClone(){
        return new JTGLRect(this);
    }
    
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof JTGLRect){
            JTGLRect r = (JTGLRect)obj;
            return this.x == r.x && this.y == r.y && this.width == r.width && this.height == r.height;
        }
        return false;
    }
    
    
    public String toString(){
        return "JTGLRect[x = "+x+", y = "+y+", width = "+width+", height = "+height+"]";
    }
}

