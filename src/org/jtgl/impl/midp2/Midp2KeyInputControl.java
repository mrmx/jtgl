/*
 * MIDPKeyInputControl.java
 *
 * Created on 29 de Enero de 2004, 00:54
 */

package org.jtgl.impl.midp2;

import javax.microedition.lcdui.game.GameCanvas;

import org.jtgl.input.*;

/**
 * KeyInputControl implementation for MIDP2
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class Midp2KeyInputControl extends KeyInputControl{
    private GameCanvas canvas;
    
    /** Creates a new instance of MIDPKeyInputControl */
    public Midp2KeyInputControl(GameCanvas canvas) {
        this.canvas = canvas;
    }
    
    /** Gets last single key pressed translated to KeyInputControl.KEY_XX codes
     * @return
     */    
    public int getKey() {
        return translateKey(canvas.getKeyStates());        
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
                case GameCanvas.KEY_NUM0:
                    return KEY_NUM0;
                case GameCanvas.KEY_NUM1:
                    return KEY_NUM1;
                case GameCanvas.KEY_NUM2:
                    return KEY_NUM2;
                case GameCanvas.KEY_NUM3:
                    return KEY_NUM3;
                case GameCanvas.KEY_NUM4:
                    return KEY_NUM4;
                case GameCanvas.KEY_NUM5:
                    return KEY_NUM5;
                case GameCanvas.KEY_NUM6:
                    return KEY_NUM6;
                case GameCanvas.KEY_NUM7:
                    return KEY_NUM7;
                case GameCanvas.KEY_NUM8:
                    return KEY_NUM8;
                case GameCanvas.KEY_NUM9:
                    return KEY_NUM9;                    
                default:
                    return KEY_NONE;
            }            
        }else {
            try {
                key = canvas.getGameAction(nativeKey);
            }catch(Exception ex){}
            switch(key){
                case GameCanvas.UP:
                    return KEY_UP;
                case GameCanvas.DOWN:
                    return KEY_DOWN;
                case GameCanvas.LEFT:
                    return KEY_LEFT;
                case GameCanvas.RIGHT:
                    return KEY_RIGHT;
                case GameCanvas.FIRE:
                    return KEY_ENTER;
                default:
                    return KEY_NONE;
            }
        }
    }
    
}
