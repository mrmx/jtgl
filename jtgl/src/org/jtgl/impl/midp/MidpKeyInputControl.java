/*
 * MIDPKeyInputControl.java
 *
 * Created on 6 de diciembre de 2003, 15:54
 */

package org.jtgl.impl.midp;

import javax.microedition.lcdui.Canvas;

import org.jtgl.input.*;

/**
 * KeyInputControl implementation for MIDP
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class MidpKeyInputControl extends KeyInputControl{
    private Canvas canvas;
    
    /** Creates a new instance of MIDPKeyInputControl */
    public MidpKeyInputControl(Canvas canvas) {
        this.canvas = canvas;
    }
    
    /** 
     * Translates from native keycode to KeyInputControl.KEY_XXX constants
     * @param nativeKey
     * @return
     */    
    protected int translateKey(int nativeKey){
        int key = KEY_NONE;
        if(nativeKey > 0){
            updateKeyTyped((char)nativeKey);
            key = nativeKey;
            switch(key){
                case Canvas.KEY_NUM0:
                    return KEY_NUM0;
                case Canvas.KEY_NUM1:
                    return KEY_NUM1;
                case Canvas.KEY_NUM2:
                    return KEY_NUM2;
                case Canvas.KEY_NUM3:
                    return KEY_NUM3;
                case Canvas.KEY_NUM4:
                    return KEY_NUM4;
                case Canvas.KEY_NUM5:
                    return KEY_NUM5;
                case Canvas.KEY_NUM6:
                    return KEY_NUM6;
                case Canvas.KEY_NUM7:
                    return KEY_NUM7;
                case Canvas.KEY_NUM8:
                    return KEY_NUM8;
                case Canvas.KEY_NUM9:
                    return KEY_NUM9;                    
                default:
                    return KEY_NONE;
            }            
        }else {
            try {
                key = canvas.getGameAction(nativeKey);
            }catch(Exception ex){}
            switch(key){
                case Canvas.UP:
                    return KEY_UP;
                case Canvas.DOWN:
                    return KEY_DOWN;
                case Canvas.LEFT:
                    return KEY_LEFT;
                case Canvas.RIGHT:
                    return KEY_RIGHT;
                case Canvas.FIRE:
                    return KEY_ENTER;
                default:
                    return KEY_NONE;
            }
        }
    }
    
}
