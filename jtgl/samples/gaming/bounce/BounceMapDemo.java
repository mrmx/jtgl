/*
 * BounceMapDemo.java
 *
 * Created on 10 de marzo de 2004, 19:57
 */

package samples.gaming.bounce;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;

/**
 * A Bouncing ball within a closed map with paralax scroll and auto-view ("camera follows ball")
 *
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class BounceMapDemo extends GameMapplet{
    static final int MAP_ROWS       = 16;
    static final int MAP_COLS       = 16;    
    static final int WALL_INDEX     = 16;
    static final int MAX_BLOCKS     = MAP_ROWS << 2;
    JTGLGraphics gc;    
    TiledSurface map,bmap;
    Ball ball;
    SurfaceManager sm,bsm;
    
    /** Creates a new instance of BounceMapTest */
    public BounceMapDemo() {
    }   
    

    public void initGame() throws Exception {                
        //Get double buffer graphic context:
        gc = getGraphics();
        gc.begin();    
        //Image loading:
        JTGLImage imgBall = createImage("/res/ball.png");        
        JTGLImage imgMap = createImage("/res/wall.png");
        //Create main tiled surface map:
        map = new TiledSurface(MAP_ROWS,MAP_COLS,imgMap,24,24);
        //Create a surrounding wall on map bounds:
        map.fillCells(0,0, MAP_COLS, 1, WALL_INDEX);//Top wall
        map.fillCells(0,0, 1, MAP_ROWS, WALL_INDEX);//Left wall
        map.fillCells(MAP_COLS-1,0, 1, MAP_ROWS, WALL_INDEX);//Right wall
        map.fillCells(0,MAP_ROWS-1, MAP_COLS, 1, WALL_INDEX);//Bottom wall
        //Fill interior with random blocks:
        for(int i = 0 ; i < MAX_BLOCKS; i++)
            map.setCell(getRandom(1,MAP_COLS-2), getRandom(1,MAP_ROWS-2),WALL_INDEX);
        //Background map:
        bmap = new TiledSurface(MAP_ROWS,MAP_COLS,imgMap,24,24);        
        bmap.modCell(0,1); //Fill map with default tile        
        for(int i = 0 ; i < MAX_BLOCKS << 3; i++)
            bmap.setCell(getRandom(1,MAP_COLS-2), getRandom(1,MAP_ROWS-2),getRandom(1,WALL_INDEX-1));        
        //Create main Sprite:
        ball = new Ball(imgBall,this);
        ball.setMove(1,1);
        ball.setActionDelay(20);        
        ball.reset();
        //Create main SurfaceManager (main view)
        sm = new SurfaceManager();        
        sm.append(map);
        sm.append(ball);
        sm.setViewWindow(0, 0, gc.getWidth(), gc.getHeight());
        //Create background SurfaceManager (background view)
        bsm = new SurfaceManager();
        bsm.append(bmap);        
    }       
    
    protected void drawGameScene() {                
        bsm.draw(gc, 0,0);        
        sm.draw(gc, 0,0);
        gc.flush();
    }   
    
    protected void processGameLogic() {        
        long time = getCurrentTimeMs();        
        ball.action(time);        
        //Follow ball as moves:
        sm.centerView(ball, map.getWidth(),map.getHeight());        
        //Follow main view as moves with ball but at half rate, thus simulating a paralax deep effect:
        bsm.setViewWindow(sm.getViewX()>> 1, sm.getViewY()>>1, map.getWidth(),map.getHeight());
    }       
}
