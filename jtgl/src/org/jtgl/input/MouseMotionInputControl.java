/*
 * MouseMotionInputControl.java
 *
 * Created on 6 de diciembre de 2003, 15:30
 */

package org.jtgl.input;

/**
 * Mouse Motion InputControl
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class MouseMotionInputControl extends InputControl {
    public final static String CONTROL_CLASSNAME    =   "MouseMotionInputControl";    
    
    /** Creates a new instance of MouseMotionInputControl */
    public MouseMotionInputControl() {
    }
    public abstract boolean isMoving();
    public abstract boolean isDragging();
    
}
