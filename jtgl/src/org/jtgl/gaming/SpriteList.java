/*
 * SpriteList.java
 *
 * Created on 27 de diciembre de 2004, 15:22
 */
package org.jtgl.gaming;

import org.jtgl.core.*;

/**
 * Represents a group of independent Sprites that can be managed as one.
 * This class is an optimized double-linked list of Sprites that manages group drawing,animation,action,visibility,etc.  
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class SpriteList {
    private Sprite root,last;
    private int numSprites;
    /** Creates a new instance of SpriteList */
    public SpriteList() {
        numSprites = 0;
    }
    
    public boolean add(Sprite sprite){
        if(sprite == null)
            return false;
        if(sprite.list != null)
            sprite.list.remove(sprite);
        if(root == null){
            root = sprite;
            root.next = root.previous = null;
            last = root;
        }else {            
            sprite.previous = last;
            last.next = sprite;
            last = sprite;
            sprite.next = null;            
        }
        sprite.list = this;
        numSprites++;
        return true;
    }
    
    public boolean remove(Sprite sprite){
        if(sprite == null || numSprites == 0 || sprite.list != this )
            return false;        
        if(sprite == root){
            if(root.next == null){
                root = null;
                last = null;
            }else {
                root = sprite.next;
                root.previous = null;                
            }
        }else {
            sprite.previous.next = sprite.next;
            if(sprite.next != null)
                sprite.next.previous = sprite.previous;
        }
        sprite.list = null;
        numSprites--;
        return true;
    }
    
    public int getSize(){
        return numSprites;
    }
    
    public boolean isEmpty() {
	return numSprites == 0;
    }

    public void clear() { 
        while(remove(root))
            ;
    }
    
    public Sprite getFirst() {
        return root;
    }
        
    public Sprite getSprite(int index) {
        if(index >= numSprites || index < 0)
            return null;
        Sprite sprite = root;
        for(int i=0; i < index ; i++)
            sprite = sprite.next;
        return sprite;
    }         
    
    public void debug(){
        Sprite sprite = root;
        while(sprite != null){
            JTGLContext.debugMsg(sprite);
            sprite = sprite.next;
        }
    }
    
    public void setActionTriggerListener(TriggerListener client){
        Sprite sprite = root;
        while(sprite != null){
            TimeTrigger trigger = sprite.getActionTrigger();
            if(trigger != null)
                trigger.client = client;            
            sprite = sprite.next;
        }
    }

    public void setAnimationTriggerListener(TriggerListener client){
        Sprite sprite = root;
        while(sprite != null){
            TimeTrigger trigger = sprite.getAnimationTrigger();
            if(trigger != null)
                trigger.client = client;            
            sprite = sprite.next;
        }
    }

    
    public void action(long currentTime){
        Sprite sprite = root;
        while(sprite != null){
            sprite.action(currentTime);
            sprite = sprite.next;
        }
    }

    public void animate(long currentTime){
        Sprite sprite = root;
        while(sprite != null){
            sprite.animate(currentTime);
            sprite = sprite.next;
        }
    }

    public void setVisible(boolean visible){
        Sprite sprite = root;
        while(sprite != null){
            sprite.setVisible(visible);
            sprite = sprite.next;
        }
    }
    
    public void setMove(int moveX,int moveY){
        Sprite sprite = root;
        while(sprite != null){
            sprite.setMove(moveX, moveY);
            sprite = sprite.next;
        }
    }

    public void reverse(){
        Sprite sprite = root;
        while(sprite != null){
            sprite.reverse();
            sprite = sprite.next;
        }
    }
    
    public void move(){
        Sprite sprite = root;
        while(sprite != null){
            sprite.move();
            sprite = sprite.next;
        }
    }
    
    public void move(int dx,int dy){
        Sprite sprite = root;
        while(sprite != null){
            sprite.move(dx,dy);
            sprite = sprite.next;
        }
    }
    
    public void moveRefTo(int x ,int y ,boolean doValidate){
        Sprite sprite = root;
        while(sprite != null){
            sprite.moveRefTo(x,y, doValidate);
            sprite = sprite.next;
        }        
    }    
 
    public void draw(JTGLGraphics gc){
        Sprite sprite = root;
        while(sprite != null){
            sprite.draw(gc);
            sprite = sprite.next;
        }
    }
    
    /**
     * Draw from Front to Background
     */
    public void drawFB(JTGLGraphics gc){     
        Sprite sprite = last;
        while(sprite != null){
            sprite.draw(gc);
            sprite = sprite.previous;
        }
    }    
    
    public boolean collidesWithBounds(int x,int y,int width,int height){
        Sprite sprite = root;
        while(sprite != null){
            if(sprite.collidesWithBounds(x,y,width,height))
                return true;
            sprite = sprite.next;
        }
        return false;
    }
    
    public boolean collidesWithBounds(JTGLRect rect){
        return rect == null? false : collidesWithBounds(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
    }
    
}
