/*
 * SpriteGroup.java
 *
 * Created on 11 de abril de 2004, 13:33
 */

package org.jtgl.gaming;

import org.jtgl.core.*;

/**
 * Represents a group of independent Sprites that can be managed as one.
 * This class is an optimized vector of Sprites that manages group drawing,animation,action,visibility,etc.
 * The api used is similar to <code>java.util.Vector</code> *
 * @author Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class SpriteGroup {
    private Sprite [] vector;
    private Sprite collidedSprite;
    private int numElements;
    private int index;  //Global index used in counters
    
    /**
     * Creates a new instance of SpriteGroup
     */
    public SpriteGroup(){
        this(4);
    }
    
    /**
     * Creates a new instance of SpriteGroup
     */
    public SpriteGroup(int initialSize) {
        vector = new Sprite[initialSize];        
    }
    
    public void debug(){
        index = 0;
        while(index < numElements)
            JTGLContext.debugMsg(index+":"+vector[index++]);                    
    }
    
    /**
     * 
     * @param client 
     */
    public void setActionTriggerListener(TriggerListener client){
        index = numElements;
        while(index > 0){
            TimeTrigger trigger = vector[--index].getActionTrigger();
            if(trigger != null)
                trigger.client = client;              
        }        
    }    

    /**
     * 
     * @param client 
     */
    public void setAnimationTriggerListener(TriggerListener client){
        index = numElements;
        while(index > 0){
            TimeTrigger trigger = vector[--index].getAnimationTrigger();
            if(trigger != null)
                trigger.client = client;              
        }        
    }    
    
    /**
     * 
     * @param currentTime 
     */
    public void action(long currentTime){
        index = numElements;
        while(index > 0)
            vector[--index].action(currentTime);
    }

    /**
     * 
     * @param currentTime 
     */
    public void animate(long currentTime){
        index = numElements;
        while(index > 0)
            vector[--index].animate(currentTime);
    }

    /**
     * 
     * @param visible 
     */
    public void setVisible(boolean visible){
        index = numElements;
        while(index > 0)
            vector[--index].setVisible(visible);
    }
    
    /**
     * 
     * @param moveX 
     * @param moveY 
     */
    public void setMove(int moveX,int moveY){
        index = numElements;
        while(index > 0)
            vector[--index].setMove(moveX, moveY);
    }

    public void reverse(){
        index = numElements;
        while(index > 0)
            vector[--index].reverse();
    }
    
    public void move(){
        index = numElements;
        while(index > 0)
            vector[--index].move();
    }
    
    /**
     * 
     * @param dx 
     * @param dy 
     */
    public void move(int dx,int dy){
        index = numElements;
        while(index > 0)
            vector[--index].move(dx,dy);
    }
    
    /**
     * 
     * @param x 
     * @param y 
     * @param doValidate 
     */
    public void moveRefTo(int x ,int y ,boolean doValidate){
        index = numElements;
        while(index > 0)
            vector[--index].moveRefTo(x,y, doValidate);
    }    
 
    /**
     * 
     * @param gc 
     */
    public void draw(JTGLGraphics gc){
        index = numElements;
        while(index > 0)
            vector[--index].draw(gc);
    }
    
    /**
     * 
     * @param x 
     * @param y 
     * @param width 
     * @param height 
     * @return 
     */
    public boolean collidesWithBounds(int x,int y,int width,int height){    
        index = numElements;
        while(index > 0)
            if(vector[--index].collidesWithBounds(x,y,width,height))
                return true;   
        return false;
    }

    /**
     * 
     * @param rect 
     * @return 
     */
    public boolean collidesWithBounds(JTGLRect rect){
        return rect == null? false : collidesWithBounds(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
    }
    
    /**
     * Method to test if any sprite element within this <CODE>SpriteGroup</CODE> has collided with a given <CODE>Sprite</CODE>.
     * This method only checks visible sprites.
     * @param sprite external <CODE>Sprite</CODE> to test collision.
     * @return index of SpriteGroup element that collided with given sprite.
     *         Return -1 if no element collided with the Sprite
     */
    public int collidesWith(Sprite sprite){
        index = numElements;
        int collideIndex = - 1;
        Sprite internalSprite = null;
        collidedSprite = null;
        while(index > 0){            
            internalSprite = vector[--index];
            if(internalSprite.visible  && internalSprite.collidesWith(sprite)) {
                collideIndex = index;
                collidedSprite = internalSprite;
                break;
            }
        }
        return collideIndex;
    }
        
    /**
     * 
     * @return last <CODE>Sprite</CODE> that collided on method collidesWith(Sprite),
     * or null if none.
     */
    public Sprite getLastCollided(){
        return collidedSprite;
    }

    /**
     * Draw from Front to Background
     * @param gc 
     */
    public void drawFB(JTGLGraphics gc){     
        index = 0;
        while(index < numElements)
            vector[index++].draw(gc);
    }
    
    /**
     * 
     * @return 
     */
    public int getCapacity(){
        return vector.length;        
    }    
    
    /**
     * 
     * @return 
     */
    public int getSize(){
        return numElements;
    }
    
    /**
     * 
     * @return 
     */
    public Sprite getFirst() {
        return vector[0];
    }
    
    /**
     * 
     * @param index 
     * @return 
     */
    public Sprite getSprite(int index) {
        return vector[index];
    }     
    
    /**
     * 
     * @param index 
     * @param sprite 
     */
    public void setSprite(int index,Sprite sprite){
        vector[index] = sprite;
    }
    
    /**
     * 
     * @param sprite 
     */
    public void add(Sprite sprite){
        ensureCapacity(numElements + 1);
	vector[numElements++] = sprite;         
    }
    
    /**
     * 
     * @param sprite 
     * @param index 
     */
    public void insertSpriteAt(Sprite sprite, int index) {	
	ensureCapacity(numElements + 1);
	JTGLContext.arraycopy(vector, index, vector, index + 1, numElements - index);
	vector[index] = sprite;
	numElements++;
    }     
    
    /**
     * 
     * @param index 
     */
    public void removeSpriteAt(int index) {	
	if(index >= numElements)
	    throw new ArrayIndexOutOfBoundsException(index + " >= " + numElements);	
	else 
            if(index < 0)
                throw new ArrayIndexOutOfBoundsException(index);	
	int j = numElements - index - 1;
	if (j > 0)
	    JTGLContext.arraycopy(vector, index + 1, vector, index, j);	
	numElements--;
	vector[numElements] = null; 
    }     
    
    /**
     * 
     * @param sprite 
     * @return 
     */
    public boolean remove(Sprite sprite) {
	int i = indexOf(sprite);
	if(i >= 0){
	    removeSpriteAt(i);
	    return true;
	}
	return false;
    }
     
    
    public void clear() { 
        numElements = 0;
    }
    
    public void trimToSize() {	
	int oldCapacity = vector.length;
	if(numElements < oldCapacity) {
	    Sprite oldVector[] = vector;
	    vector = new Sprite[numElements];
	    JTGLContext.arraycopy(oldVector, 0, vector, 0, numElements);
	}
    }     
    
    /**
     * 
     * @param newSize 
     */
    public void setSize(int newSize) {	
	if(newSize > numElements){
	    ensureCapacity(newSize);
	}else {
	    for(int i = newSize ; i < numElements ; i++)
		vector[i] = null;	    
	}
	numElements = newSize;
    }     
    
    /**
     * 
     * @return 
     */
    public boolean isEmpty() {
	return numElements == 0;
    }
    
    /**
     * 
     * @param sprite 
     * @param index 
     * @return 
     */
    public int indexOf(Sprite sprite, int index) {       
        for(int i = index ; i < numElements ; i++)
            if(vector[i] == sprite)
                return i;
        return -1;
    }
    
    /**
     * 
     * @param sprite 
     * @return 
     */
    public int indexOf(Sprite sprite){
        return indexOf(sprite,0);
    }
    
    /**
     * 
     * @return 
     */
    public String toString(){
        StringBuffer buf = new StringBuffer("SpriteGroup(");
        for(int i = 0; i < numElements ; i++){
            buf.append('[').append(vector[i]).append(']');
        }
        return buf.append(')').toString();
    }
    
    private void ensureCapacity(int minCapacity) {
	int oldCapacity = vector.length;
	if(minCapacity > oldCapacity) {
	    Sprite oldVector[] = vector;
	    int newCapacity = oldCapacity << 1;
	    vector = new Sprite[newCapacity];
	    JTGLContext.arraycopy(oldVector, 0, vector, 0, numElements);
	}
    }  
}
