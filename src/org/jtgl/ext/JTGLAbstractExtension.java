/*
 * JTGLAbstractExtension.java
 *
 * Created on 21 de noviembre de 2003, 12:18
 */

package org.jtgl.ext;

/**
 * Base Extension
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class JTGLAbstractExtension implements JTGLExtension {
    
    /** Creates a new instance of JTGLAbstractExtension */
    public JTGLAbstractExtension() {
    }

    public String getName() {
        String className = getClass().getName();
        return className.substring(className.lastIndexOf('.')+1);
    }

    public void init() throws JTGLExtensionException {
    }

    public void release() throws JTGLExtensionException {
    }
    
    public void setProperty(String key, Object value) {
    }
    
    public Object getProperty(String key) {
        return null;
    }
    
}
