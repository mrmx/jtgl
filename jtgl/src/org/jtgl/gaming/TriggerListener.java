/*
 * TriggerListener.java
 *
 * Created on 23 de febrero de 2004, 19:23
 */

package org.jtgl.gaming;

/**
 * Defines a trigger listener client
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public interface TriggerListener {
    public void triggerAction(Object owner,long currTime,long preTime);
}
