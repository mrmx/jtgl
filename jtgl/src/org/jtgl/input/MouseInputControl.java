/*
 * MouseInputControl.java
 *
 * Created on 5 de diciembre de 2003, 18:09
 */

package org.jtgl.input;

/**
 * Mouse InputControl
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class MouseInputControl extends InputControl{  
    public final static String CONTROL_CLASSNAME    =   "MouseInputControl";    
    public final static int BUTTON_NONE =   0;
    public final static int BUTTON_1    =   1;
    public final static int BUTTON_2    =   1<<1;
    public final static int BUTTON_3    =   1<<2;
    /** Creates a new instance of MouseInputControl */
    public MouseInputControl() {
    }
    
    public abstract int getX();
    public abstract int getY();
    public abstract int getButtons();
    
    
}
