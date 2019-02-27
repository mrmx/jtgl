/*
 * SiemensMediaPlayer.java
 *
 * Created on 11 de diciembre de 2003, 21:06
 */

package org.jtgl.impl.ext.media.siemens;


import org.jtgl.ext.*;
import org.jtgl.ext.media.*;


/**
 *
 * @author  Manuel
 */
public class SiemensMediaPlayer implements org.jtgl.ext.media.Player{
    private com.siemens.mp.media.Player player;
    /** Creates a new instance of SiemensMediaPlayer */
    public SiemensMediaPlayer(com.siemens.mp.media.Player player) {
        this.player = player;
    }
    
    public void deallocate() {
        player.deallocate();
    }    
    
    public void prefetch() throws MediaException {
        try {
            player.prefetch();
        }catch(com.siemens.mp.media.MediaException ex){
            throw new MediaException(ex);
        }     
    }
    
    public void realize() throws MediaException {
        try {
            player.prefetch();
        }catch(com.siemens.mp.media.MediaException ex){
            throw new MediaException(ex);
        }            
    }
    
    public void setLoopCount(int count) {
        player.setLoopCount(count);
    }
    
    public void start() throws MediaException {
        try {
            player.start();
        }catch(com.siemens.mp.media.MediaException ex){
            throw new MediaException(ex);
        }            
    }
    
    public void stop() throws MediaException {
        try {
            player.stop();
        }catch(com.siemens.mp.media.MediaException ex){
            throw new MediaException(ex);
        }            
    }
    
}
