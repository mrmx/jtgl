/*
 * TimeTrigger.java
 *
 * Created on 23 de febrero de 2004, 19:01
 */

package org.jtgl.gaming;

import org.jtgl.core.JTGLContext;

/**
 * Provides a thread-less time trigger suitable for animations, etc..
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class TimeTrigger {
    public static final int     DEFAULT_DELAY   =   100;
    Object owner;
    TriggerListener client;
    private long prevTime;
    private long oldPrevTime;
    private long prevDelay;
    private long delay;
    private boolean enabled;
    public boolean isTime;    
    
    /** Creates a new instance of TimeTrigger */
    public TimeTrigger() {
        this(null,DEFAULT_DELAY);
    }

    /** Creates a new instance of TimeTrigger */
    public TimeTrigger(long delay) {
        this(null,delay);
    }

    /** Creates a new instance of TimeTrigger */
    public TimeTrigger(Object owner) {
        this(owner,DEFAULT_DELAY);
    }
    
    /** Creates a new instance of TimeTrigger */
    public TimeTrigger(Object owner,long delay) {
        this.owner = owner;        
        setDelay(delay);
        setEnabled(true);        
    }
    
    public void setDelay(long delay){
        this.delay = delay;
    }
    
    public long getDelay(){
        return delay;
    }
    
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    
    public boolean isEnabled(){
        return enabled;
    }
    
    public void setTriggerListener(TriggerListener client){
        this.client = client;
    }

    public TriggerListener getTriggerListener(){
        return client;
    }
    
    public boolean checkTime(){
        return checkTime(JTGLContext.getCurrentTimeMs());
    }
    
    public void reset(){
        prevTime = JTGLContext.getCurrentTimeMs();
    }
    
    public boolean checkTime(long currentTime){                
        isTime = false;
        if(prevTime == 0)
            prevTime = currentTime;
        prevDelay += currentTime - prevTime;
        if(prevDelay >= delay){
            isTime = true;
            oldPrevTime = prevTime;
            prevDelay = prevDelay - delay; //adjust timming differences
        }        
        prevTime = currentTime;
        return isTime && enabled;
    }
    
    public void notifyClient(long currentTime){
        if(enabled && isTime && client != null)
            client.triggerAction(owner,currentTime,oldPrevTime);
        
    }
    
}
