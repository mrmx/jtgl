/*
 * Sprite.java
 *
 * Created on 1 de diciembre de 2003, 12:41
 */

package org.jtgl.gaming;

import org.jtgl.core.*;
import org.jtgl.image.*;

/**
 * Sprite class
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class Sprite extends Surface{
    /**
     * Constant signalling collision with top side of lastCollidedRect.
     * @see lastCollidedRect
     * @see getLastCollidedRect
     * @see getLastCollisionLocation
     */
    public final static int COLLISION_TOP       =   1;
    /**
     * Constant signalling collision with bottom side of lastCollidedRect.
     * @see lastCollidedRect
     * @see getLastCollidedRect
     * @see getLastCollisionLocation
     */
    public final static int COLLISION_BOTTOM    =   2;
    /**
     * Constant signalling collision with left side of lastCollidedRect.
     * @see lastCollidedRect
     * @see getLastCollidedRect
     * @see getLastCollisionLocation
     */
    public final static int COLLISION_LEFT      =   3;
    /**
     * Constant signalling collision with right side of lastCollidedRect.
     * @see lastCollidedRect
     * @see getLastCollidedRect
     * @see getLastCollisionLocation
     */
    public final static int COLLISION_RIGHT     =   4;

    /**
     * Constant signalling collision with a top left corner side of lastCollidedRect.
     * @see lastCollidedRect
     * @see getLastCollidedRect
     * @see getLastCollisionLocation
     */
    public final static int COLLISION_TOP_LEFT  =   5;
    
    /**
     * Constant signalling collision with a top left corner side of lastCollidedRect.
     * @see lastCollidedRect
     * @see getLastCollidedRect
     * @see getLastCollisionLocation
     */
    public final static int COLLISION_TOP_RIGHT =   6;    
    
    /**
     * Constant signalling collision with a bottom left corner side of lastCollidedRect.
     * @see lastCollidedRect
     * @see getLastCollidedRect
     * @see getLastCollisionLocation
     */
    public final static int COLLISION_BOTTOM_LEFT =   7;        
    
    /**
     * Constant signalling collision with a bottom left corner side of lastCollidedRect.
     * @see lastCollidedRect
     * @see getLastCollidedRect
     * @see getLastCollisionLocation
     */
    public final static int COLLISION_BOTTOM_RIGHT =   8;    
    
    Sprite next,previous;
    SpriteList list;
    JTGLImage staticImage;    
    ImageMatrix sequenceImageMatrix;
    
    int numImages;
    int numFrames;
    int [] sequenceFrames;
    int sequenceIndex;
    /**
     * Horizontal offset of the top left of the collision
     * rectangle from the top left of the sprite.
     */
    int cRectX;

    /**
     * Vertical offset of the top left of the collision
     * rectangle from the top left of the sprite.
     */
    int cRectY;

    /**
     * Width of the bounding rectangle for collision detection.
     */
    int cRectWidth;

    /**
     * Height of the bounding rectangle for collision detection.
     */
    int cRectHeight; 
    
    /**
     * Speed factor.
     */
    int speed = 1;   
    

    /**
     * Movement factor for constant animations in horizontal.
     * @see move()
     */
    int moveX = 0;   
    
    /**
     * Movement factor for constant animations in vertical.
     * @see move()
     */
    int moveY = 0;   
    
    /**
     * Action trigger.
     */
    private TimeTrigger actionTrigger;
    
    
    /**
     * Animation trigger.
     */
    private TimeTrigger animationTrigger;
    
    public boolean debugCollisionRect = false;
    private JTGLRect collisionRect;
    /**
     * Stores last collided Rect
     * @see getLastCollidedRect
     */
    private JTGLRect lastCollidedRect;
    
    /**
     * Stores last collided column
     * @see getLastCollidedCol
     */
    private int lastCollidedCol;
    /**
     * Stores last collided row
     * @see getLastCollidedRow
     */
    private int lastCollidedRow;
    
    /** 
     * Creates a Sprite surface (Use only for subclasses)
     */
    public Sprite(int width,int height) {
        super(width,height);
        initCollisionRectangle();
    }
    
    /** 
     * Creates a non animated Sprite using the provided image 
     */
    public Sprite(JTGLImage staticImage) {
        super(staticImage.getWidth(), staticImage.getHeight());        
        this.staticImage = staticImage;
        initCollisionRectangle();
    }

    
    /** 
     * Creates an animated Sprite using a ImageMatrix
     */
    public Sprite(ImageMatrix matrix) {
        super(matrix.getImageWidth(), matrix.getImageHeight());
        sequenceImageMatrix = matrix;
        setInitSequence();
        initCollisionRectangle();        
    }
    
    /** 
     * Creates an animated Sprite using the frames from provided image
     */
    public Sprite(JTGLImage image,int frameWidth,int frameHeight) {
        this(new ImageMatrix(image,frameWidth, frameHeight));        
    }   
    
    /** 
     * Creates a Sprite from another one 
     */    
    public Sprite(Sprite sprite) {
        super(sprite.width, sprite.height);
        this.numFrames = sprite.numFrames;
        this.numImages = sprite.numImages;
        this.sequenceIndex = sprite.sequenceIndex;
        this.staticImage = sprite.staticImage;  //Shared copy!
        this.sequenceImageMatrix = sprite.sequenceImageMatrix;  //Shared copy!
        if(numFrames != 0){
            this.sequenceFrames = new int[numFrames];            
            JTGLContext.arraycopy(sprite.sequenceFrames,0,this.sequenceFrames,0,numFrames);
        }
        this.cRectX = sprite.cRectX;
        this.cRectY = sprite.cRectY;
        this.cRectWidth = sprite.cRectWidth;
        this.cRectHeight = sprite.cRectHeight;
    }
    
    public final void setSpeed(int speed){
        this.speed = speed;
    }
    
    public final int getSpeed(){
        return speed;
    }
    
    public final void incSpeed(int amount){
        this.speed += amount;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    // Action interface:
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    public final void setMove(int moveX,int moveY){
        this.moveX = moveX;
        this.moveY = moveY;
    }
    
    public final int getMoveX(){
        return moveX;
    }
    
    public final int getMoveY(){
        return moveY;
    }    
    
    public final void setActionDelay(long delay){
        if(actionTrigger == null)
            actionTrigger = new TimeTrigger(this);        
        actionTrigger.setDelay(delay);
    }
    
    public final long getActionDelay(){
        if(actionTrigger != null)
            return actionTrigger.getDelay();        
        return -1L;
    }

    public final void setActionEnabled(boolean enabled){
        if(actionTrigger == null)
            actionTrigger = new TimeTrigger(this);
        actionTrigger.setEnabled(enabled);
    }
    
    public final boolean isActionEnabled(){
        return actionTrigger != null && actionTrigger.isEnabled();
    }
    
    
    public final TimeTrigger getActionTrigger(){
        return actionTrigger;
    }
    
    /**
     * Performs a timely action by calling <code>performTimedAction</code>.
     */
    public final void action(long currentTimeMs){        
        if(actionTrigger != null) 
            if(actionTrigger.checkTime(currentTimeMs)){
                performTimedAction(currentTimeMs);  
                actionTrigger.notifyClient(currentTimeMs);
            }        
    }
    
    /**
     * Perform actions every <code>actionDelay</code> delay
     * By default moves this sprite by <code>moveX</code>,<code>moveY</code>.
     * Subclases may override this method in order to perform any desired action(s).
     */    
    protected void performTimedAction(long currentTimeMs){        
        move();
    }
    
    /**
     * Moves this sprite by <code>moveX</code>,<code>moveY</code>.
     */
    public final void move(){
        move(moveX, moveY);
    }
    
    /**
     * Reverses current move direction
     */
    public final void reverse(){
        setMove(-moveX, -moveY);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    // Animation interface:
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    public final void setAnimationDelay(long delay){
        if(animationTrigger == null)
            animationTrigger = new TimeTrigger(this);        
        animationTrigger.setDelay(delay);
    }

    public final long getAnimationDelay(){
        if(animationTrigger != null)
            return animationTrigger.getDelay();        
        return -1L;
    }

    public final void setAnimationEnabled(boolean enabled){
        if(animationTrigger == null)
            animationTrigger = new TimeTrigger(this);
        animationTrigger.setEnabled(enabled);
    }
    
    public final boolean isAnimationEnabled(){
        return animationTrigger != null && animationTrigger.isEnabled();
    }
    
    public final TimeTrigger getAnimationTrigger(){
        return animationTrigger;
    }
    
    public final void animate(long currentTimeMs){                
        if(animationTrigger != null) 
            if(animationTrigger.checkTime(currentTimeMs)){
                performTimedAnimation(currentTimeMs);
                animationTrigger.notifyClient(currentTimeMs);
            }        
    }
    
    protected void performTimedAnimation(long currentTimeMs){
        nextFrame();
    }
    
    public void setSequence(int [] newSequence){
        if(newSequence == null){
            setInitSequence();
            return ;
        }
        sequenceFrames = newSequence;
        numFrames = sequenceFrames.length;
        sequenceIndex = 0;
    }
    
    public void setInitSequence(){
        if(sequenceImageMatrix == null)
            return ;
        numFrames = numImages = sequenceImageMatrix.getNumImages();
        if(sequenceFrames == null || numFrames != sequenceFrames.length)
            sequenceFrames = new int[numFrames];
        for(int i = 0; i < numFrames ; i++)
            sequenceFrames[i] = i;        
        sequenceIndex = 0;
    }
    
    public void setFrame(int sequenceIndex) {
        if(sequenceIndex < 0 || sequenceIndex >= sequenceFrames.length)        
            throw new IndexOutOfBoundsException();        
        this.sequenceIndex = sequenceIndex;                    
    }

    public final int getFrame(){
        return sequenceIndex;
    }

    public int getRawFrameCount(){
        return numImages;
    }

    public int getFrameSequenceLength(){
        return sequenceFrames.length;
    }

    public void nextFrame(){
        sequenceIndex = (sequenceIndex + 1) % sequenceFrames.length;
    }

    public void prevFrame(){
        if(sequenceIndex == 0)
            sequenceIndex = sequenceFrames.length - 1;
        else
            sequenceIndex--;
    }
    
    
    public void setCollisionRectangle(int x,int y,int width,int height){
	if(width < 0 || height < 0)
            throw new RuntimeException();
        cRectX = x;
        cRectY = y;
        cRectWidth = width;
        cRectHeight = height;
    }
    
    public JTGLRect getCollisionRectangle(){
        if(collisionRect == null)
            collisionRect = new JTGLRect(cRectX,cRectY,cRectWidth,cRectHeight);
        else
            collisionRect.copy(cRectX,cRectY,cRectWidth,cRectHeight);
        return collisionRect;
    }
    
    public JTGLRect getAbsCollisionRectangle(){
        JTGLRect cRect = getCollisionRectangle();
        cRect.translate(x,y);
        return cRect;
    }
    
    public JTGLRect getLastCollidedRect(){
        return lastCollidedRect;
    }
    
    public int getLastCollidedCol(){
        return lastCollidedCol;
    }

    public int getLastCollidedRow(){
        return lastCollidedRow;
    }
    
    /**
     * Detects where occurred last collision within a Rect bounds (border limit only!)     
     */
    public int getLastCollisionLocation(){
        if(lastCollidedRect == null || lastCollidedRect.isEmpty())
            return -1; //no collision location info
        int r1x1 = this.x + cRectX;
        int r1y1 = this.y + cRectY;
        int r1x2 = r1x1 + cRectWidth;
        int r1y2 = r1y1 + cRectHeight;
        
        int r2x1 = lastCollidedRect.getX();
        int r2y1 = lastCollidedRect.getY();
        int r2x2 = r2x1 + lastCollidedRect.getWidth();
        int r2y2 = r2y1 + lastCollidedRect.getHeight();
        if((r2x1 == (r1x2-1)) &&  (r2y1 == (r1y2-1))) {
            return COLLISION_TOP_LEFT;
        }
        if((r1x1 == (r2x2-1)) &&  (r2y1 == (r1y2-1))){
            return COLLISION_TOP_RIGHT;
        }
        if((r2x1 == (r1x2-1)) &&  (r1y1 == (r2y2-1))){
            return COLLISION_BOTTOM_LEFT;
        }
        if((r1x1 == (r2x2-1)) &&  (r1y1 == (r2y2-1))){
            return COLLISION_BOTTOM_RIGHT;
        }        
        if(!intersectRect(r1x1 - 1, r1y1, r1x2 - 1, r1y2, r2x1, r2y1, r2x2, r2y2)){
            //System.out.println("COLLISION_LEFT");
            return COLLISION_LEFT;
        }
        if(!intersectRect(r1x1 + 1, r1y1, r1x2 + 1, r1y2, r2x1, r2y1, r2x2, r2y2)){
            //System.out.println("COLLISION_RIGHT");
            return COLLISION_RIGHT;
        }
        if(!intersectRect(r1x1, r1y1 - 1, r1x2, r1y2 - 1, r2x1, r2y1, r2x2, r2y2)){
            //System.out.println("COLLISION_TOP");
            return COLLISION_TOP;
        }
        if(!intersectRect(r1x1, r1y1 + 1, r1x2, r1y2 + 1, r2x1, r2y1, r2x2, r2y2)){
            //System.out.println("COLLISION_BOTTOM");
            return COLLISION_BOTTOM;        
        } 
        return -1;//no collision location info
    }
    
    public final boolean collidesWith(int x,int y,int width,int height){
        return collidesAtWith(this.x,this.y,x,y,width,height);
    }
    
    public boolean collidesAtWith(int tx,int ty,int x,int y,int width,int height){
        /*
            int r1x1 = tx + cRectX;
            int r1y1 = ty + cRectY;
            int r1x2 = r1x1 + cRectWidth;
            int r1y2 = r1y1 + cRectHeight;
            int r2x2 = x + width;
            int r2y2 = y + height;
            return intersectRect(   r1x1, r1y1, r1x2, r1y2, 
                                    x, y, r2x2, r2y2
            );                    
         */
        return JTGLRect.intersects(tx + cRectX, ty + cRectY, cRectWidth, cRectHeight, x,y,width,height);
        
    }
    
    public final boolean collidesWith(JTGLRect rect){
        return rect == null ? false : collidesWith(rect.getX(), rect.getY(),rect.getWidth(),rect.getHeight());
    }

    public final boolean collidesAtWith(int tx,int ty,JTGLRect rect){
        return rect == null ? false : collidesAtWith(tx,ty,rect.getX(), rect.getY(),rect.getWidth(),rect.getHeight());
    }
    
    public final boolean collidesWith(Surface surface){
        if(surface == null || surface.collissionDisabled)
            return false;
        return collidesWith(surface.x,surface.y,surface.width,surface.height);
    }
  
    public final boolean collidesAtWith(int tx,int ty,Surface surface){
        if(surface == null || surface.collissionDisabled)
            return false;
        return collidesAtWith(tx,ty,surface.x,surface.y,surface.width,surface.height);
    }    
    
    public final boolean collidesWith(Sprite sprite){
        if(sprite == null || sprite.collissionDisabled)
            return false;        
        return collidesAtWith(x,y,sprite.x + sprite.cRectX,sprite.y + sprite.cRectY,sprite.cRectWidth,sprite.cRectHeight);
    }
    
    public boolean collidesAtWith(int tx,int ty,Sprite sprite){
        if(sprite == null || sprite.collissionDisabled)
            return false;        
        /*
        int r1x1 = tx + cRectX;
        int r1y1 = ty + cRectY;
        int r1x2 = r1x1 + cRectWidth;
        int r1y2 = r1y1 + cRectHeight;
        int r2x1 = sprite.x + sprite.cRectX;
        int r2y1 = sprite.y + sprite.cRectY;
        int r2x2 = r2x1 + sprite.cRectWidth;
        int r2y2 = r2y1 + sprite.cRectHeight;
        
        return intersectRect(   r1x1, r1y1, r1x2, r1y2, 
                                r2x1, r2y1, r2x2, r2y2
        );
         */
        return JTGLRect.intersects(
                tx + cRectX, ty + cRectY, cRectWidth, cRectHeight, 
                sprite.x + sprite.cRectX, sprite.y + sprite.cRectY, sprite.cRectWidth, sprite.cRectHeight
        );
    }
    
    public boolean collidesWith(TiledSurface tiledSurface){
        return collidesWith(x,y,tiledSurface,false);
    }
    
    public boolean collidesWith(TiledSurface tiledSurface,boolean saveCollidedRect){
        return collidesWith(x,y,tiledSurface,saveCollidedRect);
    }    
    
    public boolean preCollidesWith(TiledSurface tiledSurface){
        return collidesWith(x + moveX,y+moveY,tiledSurface,false);
    }
    public boolean collidesAtWith(int x,int y,TiledSurface tiledSurface){
        return collidesWith(x,y,tiledSurface,false);
    }
    
    /**
     * Test if this Sprite collides at location (x,y) with a non-empty cell from a <code>TiledSurface</code> instance     
     * 
     */
    //Code based on MIDP2 RI by Sun MicroSystems.     
    public boolean collidesWith(int x,int y,TiledSurface tiledSurface,boolean saveCollidedRect){
        if(saveCollidedRect && lastCollidedRect != null)
            lastCollidedRect.empty();
        lastCollidedCol = lastCollidedRow = -1;
        if(tiledSurface == null || tiledSurface.collissionDisabled)
            return false;
        int tSx1 = tiledSurface.x;
        int tSy1 = tiledSurface.y;
        int tSx2 = tSx1 + tiledSurface.width;
        int tSy2 = tSy1 + tiledSurface.height;

        int tW = tiledSurface.getCellWidth();
        int tH = tiledSurface.getCellHeight();

        int sx1 = x + this.cRectX;
        int sy1 = y + this.cRectY;
        int sx2 = sx1 + this.cRectWidth;
        int sy2 = sy1 + this.cRectHeight;

        // number of cells
        int tNumCols = tiledSurface.getColumns();
        int tNumRows = tiledSurface.getRows();        
        
        int startCol; 
        int endCol;   
        int startRow; 
        int endRow;   

        if (!intersectRect(tSx1, tSy1, tSx2, tSy2, sx1, sy1, sx2, sy2))
            return false;             

        //Check if sprite is out of tile space or compute initial tile position
        startCol = (sx1 <= tSx1) ? 0 : (sx1 - tSx1)/tW;
        startRow = (sy1 <= tSy1) ? 0 : (sy1 - tSy1)/tH;
        //Check end tile position
        endCol = (sx2 < tSx2) ? ((sx2 - 1 - tSx1)/tW) : tNumCols - 1;
        endRow = (sy2 < tSy2) ? ((sy2 - 1 - tSy1)/tH) : tNumRows - 1;

        // Check all affected tiles
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                
                if (checkTileCollision(tiledSurface,col, row)) {
                    if(saveCollidedRect){
                        int cx = tSx1 + tiledSurface.getCellX(col);
                        int cy = tSy1 + tiledSurface.getCellY(row);
                        if(lastCollidedRect == null)
                            lastCollidedRect = new JTGLRect(cx,cy,tW,tH);
                        else
                            lastCollidedRect.copy(cx,cy,tW,tH);
                    }
                    lastCollidedCol = col;
                    lastCollidedRow = row;
                    return true;               
                }                
            }
        }
        return false;              
    }
    
    /**
     * Checks where this Sprite collides with surrounding tiles.
     * This function is called from all collision checker methods that involve a TiledSurface.
     * By default checks if target tile is non empty, but may be subclassed to check for other cases.
     */
    protected boolean checkTileCollision(TiledSurface tiledSurface,int col,int row){
        return tiledSurface.getCell(col, row) != 0;
    }
    
    public boolean preCollidesWithBounds(int x,int y,int width,int height){
        return collidesWithBounds(x + moveX, y + moveY, width, height);
    }

    public boolean collidesWithBounds(int x,int y,int width,int height){
        return collidesAtWithBounds(this.x,this.y,x,y,width,height);
    }
    
    public boolean collidesAtWithBounds(int tx,int ty,int x,int y,int width,int height){        
        /*
        int r1x = tx + cRectX;
        int r1y = ty + cRectY;
        long tx2 = r1x; tx2 += cRectWidth;
        long ty2 = r1y; ty2 += cRectHeight;
        long rx2 = x; rx2 += width;
        long ry2 = y; ry2 += height;
        if (r1x < x) r1x = x;
        if (r1y < y) r1y = y;
        if (tx2 > rx2) tx2 = rx2;
        if (ty2 > ry2) ty2 = ry2;
        tx2 -= r1x;
        ty2 -= r1y;
        if (tx2 < 0) tx2 = 0;
        if (ty2 < 0) ty2 = 0;
        int iWidth  = (int) tx2;
        int iHeight = (int) ty2;            
        return (iWidth > 0 && iHeight > 0) && (iWidth != cRectWidth || iHeight != cRectHeight);
         */
        int r1x = tx + cRectX;
        int r1y = ty + cRectY;        
        if(JTGLRect.intersects(r1x, r1y,cRectWidth,cRectHeight, x, y, width, height)){            
            int r2x = r1x + cRectWidth;            
            int r2y = r1y + cRectHeight;
            int x2 = x + width;
            int y2 = y + height;
            //Test if any corner is outside
            if(r1x < x || r1x > x2 || r1y < y || r1y > y2 || 
                    r2x < x || r2x > x2 || r2y < y || r2y > y2
            )
                return true;        
            
        }
        return false;            
        
    }    

    public boolean preCollidesWithBounds(JTGLRect rect){
        return rect == null ? false : preCollidesWithBounds(rect.getX(), rect.getY(),rect.getWidth(),rect.getHeight());
    }

    public boolean collidesWithBounds(JTGLRect rect){
        return rect == null ? false : collidesWithBounds(rect.getX(), rect.getY(),rect.getWidth(),rect.getHeight());
    }
    
    public boolean collidesAtWithBounds(int tx,int ty,JTGLRect rect){
        return rect == null ? false : collidesAtWithBounds(tx,ty,rect.getX(), rect.getY(),rect.getWidth(),rect.getHeight());        
    }

    public boolean preCollidesWithBounds(Surface surface){    
        return surface == null || surface.collissionDisabled ? false : preCollidesWithBounds(surface.x, surface.y,surface.width,surface.height);
    }
    
    public boolean collidesWithBounds(Surface surface){    
        return surface == null || surface.collissionDisabled  ? false : collidesWithBounds(surface.x, surface.y,surface.width,surface.height);
    }

    public boolean collidesAtWithBounds(int tx,int ty,Surface surface){    
        return surface == null  || surface.collissionDisabled ? false : collidesAtWithBounds(tx,ty,surface.x, surface.y,surface.width,surface.height);
    }
    
    public boolean preCollidesWithBounds(Sprite sprite){    
        return sprite == null  || sprite.collissionDisabled ? false : preCollidesWithBounds(sprite.x + sprite.cRectX, sprite.y + sprite.cRectY,sprite.cRectWidth,sprite.cRectHeight);
    }

    public boolean collidesAtWithBounds(int tx,int ty,Sprite sprite){    
        return sprite == null || sprite.collissionDisabled ? false : collidesAtWithBounds(tx,ty,sprite.x + sprite.cRectX, sprite.y + sprite.cRectY,sprite.cRectWidth,sprite.cRectHeight);
    }
    
    public boolean collidesWithBounds(Sprite sprite){    
        return sprite == null || sprite.collissionDisabled ? false : collidesWithBounds(sprite.x + sprite.cRectX, sprite.y + sprite.cRectY,sprite.cRectWidth,sprite.cRectHeight);
    }
    
    /**
     * Default position validation function. <br>
     * This function can be overloaded to define your own sprite position validation or
     * collision detection.<br>
     * The overloaded function may use the sprite data members and should update
     * the sprite position centerX and centerY if needed.
     * @return true if the (centerX,centerY) is a valid position, false if it's
     * not and the position has been corrected.<br>
     * <B>NOTE</B>: false returns will stop the movements of the moveRefTo function.
     * @see #moveRefTo
     */
    public boolean isLocationValid(){
        return true;
    }

    /**
     * Moves the sprite toward the specified position. <br>    
     * Code taken from SuperWaba Game Lib (http://www.superwaba.org)
     * @param x position.
     * @param y position.
     * @param doValidate if true the position is validated which means that the
     * isLocationValid() function is called.<br>
     * <B>NOTE</B>: a false return of isLocationValid() will stop the moveRefTo function.
     * @return true if the defined position has been set
     * @see #isLocationValid
     */
    public boolean moveRefTo(int x, int y, boolean doValidate) {
        int centerX = this.x + xRef;
        int centerY = this.y + yRef;
        int dx=x-centerX;
        int dy=y-centerY;
        int steps;
        
        if (dx==0)  // vertical move
        {
            steps=Math.min(dy>=0?dy:-dy,speed);
            if (dy<0)
                centerY-=steps;
            else
                if (dy>0)
                    centerY+=steps;
            this.y = centerY - yRef;    //Update y pos
            if (doValidate) return isLocationValid();
        }
        else
            if (dy==0)  // horizontal move
            {
                steps=Math.min(dx>=0?dx:-dx,speed);
                if (dx<0)
                    centerX-=steps;
                else
                    if (dx>0)
                        centerX+=steps;
                this.x = centerX - xRef;    //Update x pos
                if (doValidate) return isLocationValid();
            }
            else { // diagonal moves
                
                // derived from SUPERWABA drawLine algorithm, thx to Guich.
                // It's Bresenham's fastest implementation!
                
                dx = dx>=0?dx:-dx;    // store the change in X and Y of the line endpoints
                dy = dy>=0?dy:-dy;
                
                int CurrentX = centerX;      // store the starting point (just point A)
                int CurrentY = centerY;
                
                // DETERMINE "DIRECTIONS" TO INCREMENT X AND Y (REGARDLESS OF DECISION)
                int Xincr=(centerX > x)? -1:1;    // which direction in X?
                int Yincr=(centerY > y)? -1:1;    // which direction in Y?
                
                // DETERMINE INDEPENDENT VARIABLE (ONE THAT ALWAYS INCREMENTS BY 1 (OR -1) )
                // AND INITIATE APPROPRIATE LINE DRAWING ROUTINE (BASED ON FIRST OCTANT
                // ALWAYS). THE X AND Y'S MAY BE FLIPPED IF Y IS THE INDEPENDENT VARIABLE.
                
                steps=speed;
                
                if (dx >= dy)                          // if X is the independent variable
                {
                    int dPr     = dy<<1;                 // amount to increment decision if right is chosen (always)
                    int dPru    = dPr - (dx<<1);         // amount to increment decision if up is chosen
                    int P       = dPr - dx;              // decision variable start value
                    
                    for (; dx>=0 && steps>0; dx--)       // process each point in the line one at a time (just use dX)
                    {
                        // update the sprite's position                        
                        this.x = CurrentX - xRef;    //Update x pos
                        this.y = CurrentY - yRef;    //Update y pos
                        
                        if (doValidate && !isLocationValid()) return false;
                        
                        CurrentX+=Xincr;                      // increment independent variable
                        steps--;
                        if (P > 0)                            // is the pixel going right AND up?
                        {
                            CurrentY+=Yincr;                    // increment dependent variable
                            steps--;
                            P+=dPru;                            // increment decision (for up)
                        }
                        else                                  // is the pixel just going right?
                            P+=dPr;                             // increment decision (for right)
                    }
                }
                else                                      // if Y is the independent variable
                {
                    int dPr     = dx<<1;                    // amount to increment decision if right is chosen (always)
                    int dPru    = dPr - (dy<<1);            // amount to increment decision if up is chosen
                    int P       = dPr - dy;                 // decision variable start value
                    
                    for (; dy>=0 && steps>0; dy--)          // process each point in the line one at a time (just use dY)
                    {
                        // update the sprite's position                        
                        this.x = CurrentX - xRef;    //Update x pos
                        this.y = CurrentY - yRef;    //Update y pos
                        
                        if (doValidate && !isLocationValid()) return false;
                        
                        CurrentY+=Yincr;                      // increment independent variable
                        steps--;
                        if (P > 0)                            // is the pixel going up AND right?
                        {
                            CurrentX+=Xincr;                    // increment dependent variable
                            steps--;
                            P+=dPru;                            // increment decision (for up)
                        }
                        else                                  // is the pixel just going up?
                            P+=dPr;                             // increment decision (for right)
                    }
                }
            }
        return true;
    }
	
    
    public void draw(JTGLGraphics g) {
        if(visible){
            if(staticImage == null){
                //draw current frame:
                sequenceImageMatrix.draw(g, x, y,sequenceFrames[sequenceIndex]);
            }else {
                //draw static image:
                g.drawImage(staticImage,x,y);
            }
            if(debugCollisionRect) g.drawRect(x+cRectX,y+cRectY,cRectWidth-1,cRectHeight-1,JTGLColor.BLACK.translucent());
        }
    }
    
    public String toString(){        
        return super.toString()+"[mX = "+moveX+", mY = "+moveY+", act = " + getActionDelay()+"ms, anim = "+getAnimationDelay()+"ms, cR = "+getAbsCollisionRectangle()+"]";
    }
    
    
    private void initCollisionRectangle(){
        cRectX = 0;
        cRectY = 0;
        cRectWidth = width;
        cRectHeight = height;
    }
    
    /**
     * Test rectangle intersection
     * 
     * @param r1x1 left coord of first rectangle
     * @param r1y1 top coord of first rectangle
     * @param r1x2 right coord of first rectangle
     * @param r1y2 bottom coord of first rectangle
     * @param r2x1 left coord of second rectangle
     * @param r2y1 top coord of second rectangle
     * @param r2x2 right coord of second rectangle
     * @param r2y2 bottom coord of second rectangle
     * @return true if both rectangles intersects
     */
    private boolean intersectRect(int r1x1, int r1y1, int r1x2, int r1y2, 
                                  int r2x1, int r2y1, int r2x2, int r2y2){
	if (r2x1 >= r1x2 || r2y1 >= r1y2 || r2x2 <= r1x1 || r2y2 <= r1y1)
	    return false;
        return true;	
    }     
        
}
