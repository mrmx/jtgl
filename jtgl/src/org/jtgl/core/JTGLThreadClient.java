/*
 * JTGLThreadClient.java
 *
 * Created on 22 de diciembre de 2004, 17:12
 */

package org.jtgl.core;

/**
 *
 * @author mrmx
 */
public interface JTGLThreadClient {
    public static final int MAX_PRIORITY    =   10;
    public static final int MIN_PRIORITY    =   0;
    
    public void started();
    public void run();
    public void stopped();
    
}
