/*
 * JTGLExtensionException.java
 *
 * Created on 21 de noviembre de 2003, 13:34
 */

package org.jtgl.ext;

import org.jtgl.core.JTGLException;

/**
 * Base Extension exception class
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class JTGLExtensionException extends JTGLException{
    
    /** Creates a new instance of JTGLExtensionException */
    public JTGLExtensionException() {
    }
    
    /**
     * Constructs an instance of <code>JTGLExtensionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public JTGLExtensionException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>JTGLExtensionException</code> wrapping a Throwable object instance.
     * @param exception the Throwable object instance.
     */
    public JTGLExtensionException(Throwable exception) {
        super(exception);
    }
    
}
