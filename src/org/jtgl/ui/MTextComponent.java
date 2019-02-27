/*
 * TextComponent.java
 *
 * Created on 16 de diciembre de 2004, 11:55
 */

package org.jtgl.ui;

/**
 *
 * @author mrmx
 */
public abstract class MTextComponent extends MComponent{
    protected String text;
    
    /** Creates a new instance of TextComponent */
    public MTextComponent() {
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public String getText(){
        return text;
    }
    
}
