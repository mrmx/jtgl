/*
 * KeyInputControl.java
 *
 * Created on 6 de diciembre de 2003, 15:26
 */

package org.jtgl.input;

/**
 * Keyboard InputControl
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class KeyInputControl extends InputControl{
    public final static String CONTROL_CLASSNAME    =   "KeyInputControl"; 
    //Basic action keys:
    public final static int KEY_NONE        = 0;
    public final static int KEY_UP          = 1;
    public final static int KEY_DOWN        = 2;
    public final static int KEY_LEFT        = 3;
    public final static int KEY_RIGHT       = 4;
    public final static int KEY_ENTER       = 5;
    public final static int KEY_MASK_UP     = 1;
    public final static int KEY_MASK_DOWN   = 1  <<  1;
    public final static int KEY_MASK_LEFT   = 1  <<  2;
    public final static int KEY_MASK_RIGHT  = 1  <<  3;
    public final static int KEY_MASK_ENTER  = 1  <<  4;
    //Numeric KeyPad
    public final static int KEY_NUM0        = 10;
    public final static int KEY_NUM1        = 11;
    public final static int KEY_NUM2        = 12;
    public final static int KEY_NUM3        = 13;
    public final static int KEY_NUM4        = 14;
    public final static int KEY_NUM5        = 15;
    public final static int KEY_NUM6        = 16;
    public final static int KEY_NUM7        = 17;
    public final static int KEY_NUM8        = 18;
    public final static int KEY_NUM9        = 19;
    
    
    public final static char CHAR_NONE  = '0';

    protected int keyPressed;
    protected int keyReleased;    
    protected int keysPressed;    
    protected char typedChar;
    
    /** Creates a new instance of KeyInputControl */
    public KeyInputControl() {
        typedChar = CHAR_NONE;
        keyPressed = keyReleased = keysPressed = KEY_NONE;
    }
    
    /**
     * @return
     */    
    public boolean supportsSimultaneusKeys(){
        return false;
    }
    
    /**
     * @return
     */    
    public int getKeyPressed() {
        int lastKeyPressed = keyPressed;
        if(keyPressed == keyReleased)
            keyPressed = KEY_NONE;        //reset when consumed
        return lastKeyPressed;
    }

    /**
     * @return
     */    
    public int getKeyReleased() {        
        return keyReleased;
    }   
    
    public char getTypedChar(){
        return typedChar;
    }
    
    /** 
     * Gets last single key pressed translated to KeyInputControl.KEY_XXX codes
     * This method is an alias of <code>getKeyPressed()</code> method.
     * @see #getKeyPressed    
     * @return KeyInputControl.KEY_XXX code
     */    
    public int getKey() {
        return getKeyPressed();        
    }    
    
    /** 
     * Gets multiple keys pressed if supported by implementation as single bits.
     * Value returned may content any combination of KeyInputControl.KEY_MASK_XXX
     * codes.
     * @return 
     */    
    public int getKeys() {
        if(supportsSimultaneusKeys())
            return keysPressed;        
        return getKey();
    }    

    /** 
     * Gets pressed keys bitmask status.
     * If implementation support multiple keys pressed then return value will contain all
     * detected keys pressed as a enabled bit, otherwise only one bit may be checked.          
     * @return Any combination of KeyInputControl.KEY_MASK_XXX
     */        
    public int getKeyStates(){
        return keysPressed;
    }
    
    /**
     * @param nativeKey
     */    
    public void updateKeyPressed(int nativeKey){
        //System.out.println("updateKeyPressed "+nativeKey);
        keyPressed = translateKey(nativeKey);
    }

    /**
     * @param nativeKey
     */    
    public void updateKeyReleased(int nativeKey){
        //System.out.println("updateKeyReleased "+nativeKey);
        keyReleased = translateKey(nativeKey);
    }   
    
    /**
     * @param nativeKey
     */    
    public void updateKeyRepeated(int nativeKey){        
        updateKeyPressed(nativeKey);
    }    
    
    public void updateKeyTyped(char key){
        typedChar = key;
    }
    
    /** 
     * Translates from native keycode to KeyInputControl.KEY_XXX codes
     * @param nativeKey
     * @return KeyInputControl.KEY_XXX code
     */    
    protected int translateKey(int nativeKey){
        return nativeKey;
    }
}
