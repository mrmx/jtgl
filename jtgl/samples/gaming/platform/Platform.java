/*
 * Platform.java
 *
 * Created on 7 de febrero de 2004, 21:00
 */

package samples.gaming.platform;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;

/**
 * Basic platform game demo code
 *
 * @version 0.2
 * @author   Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Platform extends GameMapplet{
    JTGLGraphics gc;
    ImageMatrix imgMatrix;
    TiledSurface map;
    SurfaceManager sm;   
    TimeTrigger trigger;
    Sequence wavesSeq;
    String fpsMsg;
    int fpsYPos;
    int scrWidth,scrHeight;
    int mviewX,mviewY;    
    int animatedIdx; 
    boolean autoMove;
    /** Creates a new instance of Platform */
    public Platform() {
    }
        
    
    
    public void initGame() throws Exception {   
        try {
            //imgMatrix = new ImageMatrix(createImage("/res/tmap2_48.png"),48);
            imgMatrix = new ImageMatrix(createImage("/res/tmap2_32.png"),32);
            //imgMatrix = new ImageMatrix(createImage("/res/tmap3_16.png"),16);
            //imgMatrix = new ImageMatrix(createImage("/res/tmap2_12.png"),12);//Tiny 12x12 tiles
        }catch(Exception ex){                        
            handleException(ex,"Error loading resources");
        }
        gc = getGraphics();        
        gc.begin();    
        //gc.setFontSize(JTGLFont.SIZE_SMALL);
        fpsYPos = gc.getFont().getHeight();        
        fpsMsg = "";
        enableFPSMeasure(true);
        setRefreshType(REFRESH_MANUAL);
        scrWidth = gc.getWidth();
        scrHeight = gc.getHeight();
        //setFps(MAX_FPS);        
        trigger = new TimeTrigger(300);
        wavesSeq = new Sequence(new int[]{3,1,2},Sequence.PING_PONG);
        
        /*
        map = new TiledSurface(16, 16, imgMatrix);
        map.showGrid = true;
        short[] arr_map = { 
            -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,11,15,15,15,15,15,15,15,15,15,15,15,15,12,-1,
            -1,4,41,41,41,41,41,41,41,41,41,41,41,41,13,-1,
            -1,4,41,27,16,16,16,16,16,16,16,28,41,41,13,-1,
            -1,4,41,32,41,41,41,41,41,41,41,23,41,41,13,-1,
            -1,4,41,24,41,41,41,41,41,41,41,31,41,41,13,-1,
            -1,4,41,32,41,41,41,41,41,41,41,41,41,41,13,-1,
            -1,4,41,24,41,41,25,29,30,29,37,41,41,41,13,-1,
            -1,4,41,32,41,41,22,41,41,41,41,41,41,41,13,-1,
            -1,4,41,32,41,41,22,41,41,41,41,41,41,41,13,-1,
            -1,4,41,24,41,41,33,8,8,8,16,28,41,41,13,-1,
            -1,4,41,35,30,37,41,41,41,41,41,23,41,41,13,-1,
            -1,4,41,41,41,41,41,38,30,29,30,36,41,41,13,-1,
            -1,4,41,41,41,41,41,41,41,41,41,41,41,41,13,-1,
            -1,19,6,6,6,6,6,6,6,6,6,6,6,6,20,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1
        };
        */
        
        //Debug Map:
        map = new TiledSurface(3, 3, imgMatrix);
        map.showGrid = true;
        short[] arr_map = { 
            -1,1,10,
            13,20,5,
            8,9,11
        };        
        // */
        
        map.setMap(arr_map);  
        animatedIdx = map.createAnimatedTile(1);        
        sm = new SurfaceManager();        
        sm.append(map);
        //Set fullscreen view window:
        //sm.setViewWindow(0, map.getHeight() >> 1, gc.getWidth(), gc.getHeight());                
        //sm.setViewWindow(0, 32*1, gc.getWidth(), gc.getHeight());                
        //map.setViewWindow(0, 32*1, map.getWidth(),map.getHeight()-32);             
        //updateMapViewPort();
        //sets initial movement vector
        mviewX = mviewY = 1;
        autoMove = false;
    }
    
    protected void drawGameScene() {        
        gc.clear();
        sm.draw(gc,0,0);
        if(trigger.isTime){ //avoid gc
            fpsMsg = "FPS:"+ getRealFPS() + " mFPS:"+getRealMFPS();        
        }
        gc.setColor(JTGLColor.BLACK);
        gc.drawString(fpsMsg,1,fpsYPos+1);        
        gc.setColor(JTGLColor.YELLOW);
        gc.drawString(fpsMsg,0,fpsYPos);
        gc.flush();
    }
    
    protected void processGameLogic() {
        if(autoMove){
            /*
            int vx = sm.getViewX() + mviewX;
            int vy = sm.getViewY() + mviewY;
            if(vx <= 0 || (vx + sm.getViewWidth()) >= map.getWidth())
                mviewX = -mviewX;
            if(vy <= 0 || (vy + sm.getViewHeight()) >= map.getHeight())
                mviewY = -mviewY;        
            sm.moveView(mviewX,mviewY);
             */
        }
        if(trigger.checkTime()){
            map.setAnimatedTile(animatedIdx,wavesSeq.nextFrame());                   
            //if(getRefreshType() == REFRESH_MANUAL) refreshGraphics();
        }
        
        int key = getKeys();
        if(key == KeyInputControl.KEY_NUM0)
            autoMove = !autoMove;
       
        if(key == KeyInputControl.KEY_LEFT){
            
            sm.moveView(-1,0);            
            if(getRefreshType() == REFRESH_MANUAL) refreshGraphics();
        }
        if(key == KeyInputControl.KEY_RIGHT){
            
            sm.moveView(1,0);
            if(getRefreshType() == REFRESH_MANUAL) refreshGraphics();
        }
        if(key == KeyInputControl.KEY_UP){
            
            sm.moveView(0,-1);
            if(getRefreshType() == REFRESH_MANUAL) refreshGraphics();
        }
        if(key == KeyInputControl.KEY_DOWN){
            
            sm.moveView(0,1);
            if(getRefreshType() == REFRESH_MANUAL) refreshGraphics();
        }   
        
            
        if(key == KeyInputControl.KEY_ENTER){            
            sm.setViewLocation(0,0);        
            
            if(getRefreshType() == REFRESH_MANUAL) refreshGraphics();
        }   
        if(key != KeyInputControl.KEY_NONE){
            JTGLContext.debugMsg("Manager view at"+sm.getViewX()+","+sm.getViewY());
            updateMapViewPort();
        }
        
    }
    
    private void updateMapViewPort(){
        map.setViewWindowFromViewPort(sm.getViewX(), sm.getViewY(), sm.getViewWidth(), sm.getViewHeight());
    }
}
