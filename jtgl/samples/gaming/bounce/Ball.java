/*
 * Ball.java
 *
 * Created on 7 de marzo de 2004, 18:37
 */

package samples.gaming.bounce;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;

/**
 * Sprite ball
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Ball extends Sprite{
    int map_cols,map_rows;  
    BounceMapDemo cmap;
    TiledSurface map;        
    
    /** Creates a new instance of Ball */
    public Ball(JTGLImage img,BounceMapDemo cmap) {
        super(img);
        this.cmap = cmap;
        map = cmap.map;        
        map_cols = map.getColumns();
        map_rows = map.getRows();        
    }    
    
    /**
     * Called from action(time)
     */
    public void performTimedAction(long currentTimeMs){
        move();  //Default move sprite        
        //Validate next move based on search posible 45 degree bounces
        if(collidesWith(map,true)){
            int x = getX();
            int y = getY();
            int vx = getMoveX();
            int vy = getMoveY();                                       
            //Check next move and if it is llegal
            if(!collidesAtWith(x+vx,y-vy,map)){
                setMove(vx,-vy);            
                return;
            }            
            //Check next move and if it is llegal
            if(!collidesAtWith(x-vx,y+vy,map)){
                setMove(-vx,vy);            
                return;
            }                        
            setMove(-vx, -vy);  //inverse direction
        }              
        
    }
    /**
     * Resets ball position on a free tile (tile value of 0)
     */
    public void reset(){
        int ballCellCol,ballCellRow;
        
        do {      
            ballCellCol = cmap.getRandom(map_cols-1);
            ballCellRow = cmap.getRandom(map_rows-1);
        }while(map.getCell(ballCellCol, ballCellRow) != 0);
        
        int ballX = (ballCellCol*map.getCellWidth())+(map.getCellWidth()>>1);
        int ballY = (ballCellRow*map.getCellHeight())+(map.getCellHeight()>>1);
        setAbsRefLocation(ballX,ballY);                
    }    
}
