/*
 * JTGLException.java
 *
 * Created on 19 de junio de 2003, 12:09
 */

package org.jtgl.core;

/**
 * JTGL core Exception class
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class JTGLException extends java.lang.Exception {
    private Throwable exception;
    /**
     * Creates a new instance of <code>JTGLException</code> without detail message.
     */
    public JTGLException() {
    }
    
    
    /**
     * Constructs an instance of <code>JTGLException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public JTGLException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>JTGLException</code> wrapping a Throwable object instance.
     * @param exception the Throwable object instance.
     */
    public JTGLException(Throwable exception) {
        this.exception = exception;
    }
    
    public Throwable getException(){
        return exception;
    }
    
}
