/*
 * MediaException.java
 *
 * Created on 11 de diciembre de 2003, 19:38
 */

package org.jtgl.ext.media;

import org.jtgl.core.JTGLException;

/**
 * Encapsulates any media exception
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class MediaException extends JTGLException {
    
    /**
     * Creates a new instance of <code>MediaException</code> without detail message.
     */
    public MediaException() {
    }
    
    
    /**
     * Constructs an instance of <code>MediaException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MediaException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>MediaException</code> wrapping a Throwable object instance.
     * @param exception the Throwable object instance.
     */
    public MediaException(Throwable exception) {
        super(exception);
    }
}