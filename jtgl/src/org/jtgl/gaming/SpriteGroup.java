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
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class SpriteGroup {
    private Sprite [] vector;
    private int numElements;
    private int index;  //Global index used in counters
    
    /** Creates a new instance of IntVector */
    public SpriteGroup(){
        this(4);
    }
    
    /** Creates a new instance of IntVector */
    public SpriteGroup(int initialSize) {
        vector = new Sprite[initialSize];        
    }
    
    public void debug(){
        index = 0;
        while(index < numElements)
            JTGLContext.debugMsg(index+":"+vector[index++]);                    
    }
    
    public void setActionTriggerListener(TriggerListener client){
        index = numElements;
        while(index > 0){
            TimeTrigger trigger = vector[--index].getActionTrigger();
            if(trigger != null)
                trigger.client = client;              
        }        
    }    

    public void setAnimationTriggerListener(TriggerListener client){
        index = numElements;
        while(index > 0){
            TimeTrigger trigger = vector[--index].getAnimationTrigger();
            if(trigger != null)
                trigger.client = client;              
        }        
    }    
    
    public void action(long currentTime){
        index = numElements;
        while(index > 0)
            vector[--index].action(currentTime);
    }

    public void animate(long currentTime){
        index = numElements;
        while(index > 0)
            vector[--index].animate(currentTime);
    }

    public void setVisible(boolean visible){
        index = numElements;
        while(index > 0)
            vector[--index].setVisible(visible);
    }
    
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
    
    public void move(int dx,int dy){
        index = numElements;
        while(index > 0)
            vector[--index].move(dx,dy);
    }
    
    public void moveRefTo(int x ,int y ,boolean doValidate){
        index = numElements;
        while(index > 0)
            vector[--index].moveRefTo(x,y, doValidate);
    }    
 
    public void draw(JTGLGraphics gc){
        index = numElements;
        while(index > 0)
            vector[--index].draw(gc);
    }
    
    public boolean collidesWithBounds(int x,int y,int width,int height){    
        index = numElements;
        while(index > 0)
            if(vector[--index].collidesWithBounds(x,y,width,height))
                return true;   
        return false;
    }

    public boolean collidesWithBounds(JTGLRect rect){
        return rect == null? false : collidesWithBounds(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
    }

    /**
     * Draw from Front to Background
     */
    public void drawFB(JTGLGraphics gc){     
        index = 0;
        while(index < numElements)
            vector[index++].draw(gc);
    }
    
    public int getCapacity(){
        return vector.length;        
    }    
    
    public int getSize(){
        return numElements;
    }
    
    public Sprite getFirst() {
        return vector[0];
    }
    
    public Sprite getSprite(int index) {
        return vector[index];
    }     
    
    public void setSprite(int index,Sprite sprite){
        vector[index] = sprite;
    }
    
    public void add(Sprite sprite){
        ensureCapacity(numElements + 1);
	vector[numElements++] = sprite;         
    }
    
    public void insertSpriteAt(Sprite sprite, int index) {	
	ensureCapacity(numElements + 1);
	JTGLContext.arraycopy(vector, index, vector, index + 1, numElements - index);
	vector[index] = sprite;
	numElements++;
    }     
    
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
    
    public void setSize(int newSize) {	
	if(newSize > numElements){
	    ensureCapacity(newSize);
	}else {
	    for(int i = newSize ; i < numElements ; i++)
		vector[i] = null;	    
	}
	numElements = newSize;
    }     
    
    public boolean isEmpty() {
	return numElements == 0;
    }
    
    public int indexOf(Sprite sprite, int index) {       
        for(int i = index ; i < numElements ; i++)
            if(vector[i] == sprite)
                return i;
        return -1;
    }
    
    public int indexOf(Sprite sprite){
        return indexOf(sprite,0);
    }
    
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
