/*
 * JTBReak.java
 *
 * Created on 7 de marzo de 2004, 16:14
 */

package samples.gaming.jtbreak;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;

/**
 * Classical Arcanoid style block breaker implemented with JTGL Gaming API
 * @version 0.70b
 * @author   Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class JTBreak extends GameMapplet{
    static final int BLOCK_INDEX    = 1;
    static final int WALL_INDEX     = 13;    
    int map_cols;
    int map_rows;
    JTGLGraphics gc;    
    JTGLImage imgBackground;
    TiledSurface map;
    JTGLRect mapBorder;
    Ball ball;
    MainActor actor;    
    
      
    /** Creates a new instance of JTBReak */
    public JTBreak() {
    }
    
      
    public void initGame() throws Exception {                
        gc = getGraphics();
        gc.begin();                   
        map_cols = gc.getWidth() / 16;
        map_rows = gc.getHeight() / 8;
        JTGLImage imgBall = createImage("/res/ball.png");
        JTGLImage imgActor = createImage("/res/actor.png");
        try {
            ImageUtil imageUtil = getImageUtil();            
            imgBackground = createImage("/res/space.png");            
            imgBackground = imageUtil.createScaledImage(imgBackground,gc.getWidth(),gc.getHeight());            
        }catch(Throwable ex){        
            ex.printStackTrace();
        }
             
        map = new TiledSurface(map_rows,map_cols,createImage("/res/blocks.png"),16,8);
        int max_rows = 1 + (map_rows>>2) + (map_rows>>3);
        for(int i = 0 ; i < (map_rows << 2) ; i++)
            map.setCell(getRandom(1,map_cols-2), getRandom(1,max_rows),getRandom(1,5));
        mapBorder = map.getBounds().addDimension(-1);
        actor = new MainActor(imgActor,this);        
        actor.setMove(0,0);
        actor.setActionDelay(20);   
        ball = new Ball(imgBall,this);
        ball.setActionDelay(20);           
        actor.reset();
    }
    
    protected void drawGameScene() {
        if(imgBackground != null)
            gc.drawImage(imgBackground,0,0);            
        else
            gc.clear();
        map.draw(gc);
        gc.drawRect(mapBorder,JTGLColor.BLACK);
        actor.draw(gc);
        ball.draw(gc);
        gc.flush();        
    }
    
    protected void processGameLogic() {
        int keys = getKeys();
        if(keys == KeyInputControl.KEY_LEFT){
            actor.left();
        }
        else
        if(keys == KeyInputControl.KEY_RIGHT){
            actor.right();                        
        }               
        long time = System.currentTimeMillis();
        ball.action(time);
        actor.action(time);         
    }
    
    void ballLost(){
        actor.reset();
    }   
}
