/*
 * Container.java
 *
 * Created on 16 de diciembre de 2004, 13:19
 */

package org.jtgl.ui;

import org.jtgl.core.*;

/**
 *
 * @author mrmx
 */
public abstract class MContainer extends MComponent {
    private static final int    DEFAULT_CHILDREN_SIZE   =   2;
    private MComponent[] children;
    private int numChildren;
    //when a container has screen resources (visible or not on screen)
    boolean onScreen = false;
    
    /** Creates a new instance of Container */
    public MContainer() {
        this(DEFAULT_CHILDREN_SIZE);
    }

    /** Creates a new instance of Container */
    public MContainer(int size) {
        children = new MComponent[size];
        numChildren = 0;
    }
    
    public void add(MComponent comp){
        if(comp == this)
            return ;
        //TODO: remove from old parent if any
        children[numChildren++] = comp;
        comp.parent = this;
        validate();
    }
    
    public MComponent getComponent(int index){
        return children[index];
    }
    
    //called by a root container (eg:desktop) when this containter has available screen resources
    public void validateChildren(){
        onScreen = true;
        validate();        
    }
    
    //called by a root container (eg:desktop) when this containter has no available screen resources
    public void invalidateChildren(){        
        invalidate();        
        onScreen = false;
    }
    
    
    public void validate(){
        if(onScreen){
            super.validate();
            MComponent child;
            for(int i = 0; i < numChildren ; i++){
                child = children[i];                        
                child.validate();
            }
        }
    }
    
    public void invalidate(){
        super.invalidate();
        MComponent child;
        for(int i = 0; i < numChildren ; i++){
            child = children[i];                        
            child.invalidate();
        }        
    }    
    
    
    public void paint(JTGLGraphics g){
        MComponent child;
        for(int i = 0; i < numChildren ; i++){
            child = children[i];    
            if(child.visible){
                //TODO: clip & translate? child region
                JTGLContext.debugMsg("Painting child:"+child);
                if(child.opaque){
                    JTGLContext.debugMsg("Painting child background with "+child.bgColor);
                    g.setColor(child.bgColor);                
                    child.paintBackground(g, child.x,child.y,child.width,child.height);
                }   
                JTGLContext.debugMsg("Painting child with "+child.color);
                g.setColor(child.color);
                g.setFont(child.font);
                child.paint(g);
            }
        }
    }
    
}
