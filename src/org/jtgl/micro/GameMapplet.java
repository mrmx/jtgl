/*
 * MidpGameMapplet.java
 *
 * Created on 27 de diciembre de 2003, 16:46
 */

package org.jtgl.micro;


import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.micro.*;

/**
 * GameMapplet is a base class for a gaming Mapplet.
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class GameMapplet extends Mapplet implements JTGLThreadClient{
    public final static int REFRESH_MANUAL  = -1;
    public final static int REFRESH_AUTO    = 0;
    public final static int MIN_FPS         = 1;
    public final static int MAX_FPS         = 1000;    
    
    private KeyInputControl keyInputControl;
    private JTGLGraphics gc;    
    private boolean initialized;
    private boolean runningGameLoop;
    private boolean refreshGraphics;
    private int refreshType;
    private int fps;
    private int refreshInterval;
    private long lastRefreshInterval;
    private boolean measureFPS;
    private long startFrame;
    private long frameCounter;
    private long framesSum;   
    private long frameMCounter;
    private long frameTime,maxFrameTime,minFrameTime;
    private long realtimeFPS;
    private long realtimeMFPS;
    
    public GameMapplet() {        
        setFps(50);
    }    
  
    
    public void start(){
        gc = getGraphics();
        if(gc == null)
            return;        
        addThreadClient(this, JTGLThreadClient.MAX_PRIORITY);
    }
    
    public void pause() {
        runningGameLoop = false;           
        removeThreadClient(this);             
    }        

    
    /** Kill Mapplet */    
    public void kill(boolean force) throws JTGLException{        
        pause();        
        initialized = false;
    }       
    public void setRefreshType(int type){
        refreshType = type;
    }
    
    public int getRefreshType(){
        return refreshType;
    }
    
    public void refreshGraphics(){
        refreshGraphics = true;
    }
    
    
    public final void setFps(int fps){
        this.fps = fps < MIN_FPS ? MIN_FPS : fps > MAX_FPS ? MAX_FPS : fps;
        refreshInterval = 1000/fps;
        debugMsg("refreshInterval = "+refreshInterval);
    }
    
    public final int getFps(){
        return fps;
    }     
    
    
    public void enableFPSMeasure(boolean enable){
        measureFPS = enable;
        if(!enable){
            realtimeFPS = realtimeMFPS = 0;
        }
    }
    
    public final long getFrameCounter(){
        return frameCounter;
    }
    
    public final long getRealFPS(){
        return realtimeFPS;
    }
    
    public final long getRealMFPS(){        
        return frameMCounter > 0 ? realtimeMFPS = framesSum/frameMCounter : 0;
    }    
    
    public final long getFrameTime(){
        return frameTime;
    }
    
    public final long getMaxFrameTime(){
        return maxFrameTime;
    }

    public final long getMinFrameTime(){
        return minFrameTime;
    }
   
    public final int getKeys(){
        if(keyInputControl == null)
            keyInputControl = (KeyInputControl)getInputControl(KeyInputControl.CONTROL_CLASSNAME);        
        return keyInputControl == null ? KeyInputControl.KEY_NONE : keyInputControl.getKeys();
    }        
        
   
    
    public final int getRandom(int to) {
        return getRandom(0,to);
    }
    
    public final int getRandom(int from, int to) {
        return JTGLContext.getContext().getRandom(from, to);
    }

    public final boolean isRunning(){
        return runningGameLoop;
    }
    
    public void started(){
        debugMsg("Started game loop!");            
        maxFrameTime = frameTime = startFrame = 0L;
        minFrameTime = 1<<30;        
        frameCounter = frameMCounter = 0;    
        runningGameLoop = true;            
    }
    
    public void stopped(){
        debugMsg("Stopped game loop!");
    }
    
    public final void run() {
        if(!initialized){
            try {
                debugMsg("Initializing Game...");                
                initGame();
                debugMsg("Game initialized!");
                initialized = true;
            }            
            catch(Throwable ex){                
                handleException(ex);
                notifyKill();  
                return;
            }                    
            refreshGraphics();            
        }else 
            if(!runningGameLoop) {                 
                threadSleep(50);
                return ;
            }        
        frameCounter++;
        startFrame = getCurrentTimeMs();
        //#if debug            
        try {             
        //#endif
            processGameLogic();
            if(refreshType == REFRESH_AUTO)
                drawGameScene();                        
            else
                if(refreshGraphics){ //REFRESH_MANUAL
                    drawGameScene();
                    refreshGraphics = false;
                }
        //#if debug
        }catch(Throwable t){
            ;
        }                
        //#endif
        frameTime = getCurrentTimeMs() - startFrame;
        if(frameTime > maxFrameTime) maxFrameTime = frameTime; //Remember maximum frame time            
        if(frameTime < minFrameTime) minFrameTime = frameTime; //Remember minimum frame time            
        /////////// DELAY ////////////////            
        if(frameTime < refreshInterval){
            try {                         
                threadSleep(refreshInterval-frameTime);                    
            }catch(Exception ex){}
        }else {                                
            threadYield();
        }             
        /////////// END DELAY ////////////

        if(measureFPS && frameTime > 0){
           frameMCounter++; //Count measures
           realtimeFPS = 1000 / frameTime;            
           //framesSum += realtimeFPS;//Measure median of realtime cycle (excluding refreshInterval delay)
           framesSum += 1000 / (getCurrentTimeMs() - startFrame);//Measure median of complete cycle (including refreshInterval delay)
        }                    
    }      
    
    protected void initGame() throws Exception{
    }
   
    protected abstract void processGameLogic();
    
    //TODO: Change to receive the correct JTGLGraphics context
    protected abstract void drawGameScene();
}