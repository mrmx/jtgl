/*
 * Ball.java
 *
 * Created on 7 de marzo de 2004, 18:37
 */

package samples.gaming.jtbreak;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;

/**
 * Ball Sprite
 * @author   Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Ball extends Sprite{
    int map_cols,map_rows;  
    JTBreak jtb;
    TiledSurface map;    
    MainActor actor;
    JTGLRect mapBounds;    
    /** Creates a new instance of Ball */
    public Ball(JTGLImage img,JTBreak jtb) {
        super(img);
        this.jtb = jtb;
        map = jtb.map;
        actor = jtb.actor;
        map_cols = map.getColumns();
        map_rows = map.getRows();
        mapBounds = map.getBounds();        
    }
    
    public void performTimedAction(long currentTimeMs){
        move();
        int x = getX();
        int y = getY();        
        int vx = getMoveX();
        int vy = getMoveY();                                                           
        if( (y + getHeight()) > (actor.getY() + 1)){            
            if( y  >= mapBounds.getHeight()){
                //Ball lost!
                jtb.ballLost();            
            }              
            return; //no check more collisions if ball goes down actor
        }
        else                       
        if(collidesWithBounds(mapBounds)){
            //Check next move and if it is llegal
            if(!collidesAtWithBounds(x+vx,y-vy,mapBounds)){
                setMove(vx,-vy);            
                return;
            }            
            //Check next move and if it is llegal
            if(!collidesAtWithBounds(x-vx,y+vy,mapBounds)){
                setMove(-vx,vy);            
                return;
            }                        
            setMove(-vx, -vy);  //inverse direction            
        }
        else
        if(collidesWith(map,true)){              
            int last_ccol = getLastCollidedCol();
            int last_crow = getLastCollidedRow();
            int cell = map.getCell(last_ccol, last_crow);
            if(cell > 0)
                map.setCell(last_ccol, last_crow, cell - 1);
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
        else
        if(collidesWith(actor)){                                    
            //Check next move and if it is llegal
            if(!collidesAtWith(x+vx,y-vy,actor)){
                setMove(vx,-vy);            
                return;
            }            
            //Check next move and if it is llegal
            if(!collidesAtWith(x-vx,y+vy,actor)){
                setMove(-vx,vy);            
                return;
            }                        
            setMove(-vx, -vy);  //inverse direction                        
        }              

    }
    
}
