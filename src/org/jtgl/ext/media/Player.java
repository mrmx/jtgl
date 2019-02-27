/*
 * Player.java
 *
 * Created on 11 de diciembre de 2003, 19:44
 */

package org.jtgl.ext.media;

/**
 * Represents a media player
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public interface Player {    
    public void prefetch() throws MediaException;
    public void realize() throws MediaException;
    public void deallocate();
    public void setLoopCount(int count);
    public void start() throws MediaException;
    public void stop()throws MediaException;
    
}
