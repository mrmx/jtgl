/*
 * JTGLExtension.java
 *
 * Created on 20 de noviembre de 2003, 18:32
 */

package org.jtgl.ext;

/**
 * Defines a pluggable extension to the JTGL api. Extensions such device and media interfaces are provided.
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public interface JTGLExtension {    
    public String getName();
    public String getVersion();
    public String getVendor();
    public String getInfo();
    public Object getProperty(String key);
    public void setProperty(String key,Object value);
    public void init() throws JTGLExtensionException;
    public void release() throws JTGLExtensionException;
}
