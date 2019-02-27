/*
 * MMAPIPlayer.java
 *
 * Created on 7 de febrero de 2004, 13:39
 */

package org.jtgl.impl.ext.media.MMAPI;

import org.jtgl.ext.media.*;

/**
 *
 * @author  Manuel
 */
public class MMAPIPlayer implements Player{
    private javax.microedition.media.Player player;
    /** Creates a new instance of MMAPIPlayer */
    public MMAPIPlayer(javax.microedition.media.Player player) {
        this.player = player;
    }
    
    public void deallocate() {
        player.deallocate();
    }
    
    public void prefetch() throws MediaException {
        try {
            player.prefetch();
        }catch(javax.microedition.media.MediaException ex){
            throw new MediaException(ex);
        }
    }
    
    public void realize() throws MediaException {
        try {
            player.realize();
        }catch(javax.microedition.media.MediaException ex){
            throw new MediaException(ex);
        }        
    }
    
    public void setLoopCount(int count) {
        player.setLoopCount(count);
    }
    
    public void start() throws MediaException {
        try {
            player.start();
        }catch(javax.microedition.media.MediaException ex){
            throw new MediaException(ex);
        }        
    }
    
    public void stop() throws MediaException {
        try {
            player.stop();
        }catch(javax.microedition.media.MediaException ex){
            throw new MediaException(ex);
        }
    }
    
}
