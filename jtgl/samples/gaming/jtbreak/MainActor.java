/*
 * MainActor.java
 *
 * Created on 8 de marzo de 2004, 0:14
 */

package samples.gaming.jtbreak;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;

/**
 * Main actor
 * @author   Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class MainActor extends Sprite{
    JTBreak jtb;
    private boolean reset;
    private int resetCounter;
    /** Creates a new instance of MainActor */
    public MainActor(JTGLImage img,JTBreak jtb) {
        super(img);
        this.jtb = jtb;
    }
    
    public void reset(){
        JTGLRect mapBounds = jtb.mapBorder;
        int y = mapBounds.getY()+mapBounds.getHeight() - getHeight() - 2;
        setLocation((mapBounds.getWidth() >> 1) - (getWidth()>>1), y);
        jtb.ball.setAbsRefLocation(getX()+(getWidth()>>1),getY()-(jtb.ball.getHeight()>>1));
        jtb.ball.setMove(0,0);
        reset = true;
        resetCounter = 200;
    }
    
    public void left(){              
        setMove(-1,0);
        checkMove();
    }
    public void right(){
        setMove(1,0);
        checkMove();
    }
    
   
    
    public void performTimedAction(long currentTimeMs){
        
        if(reset){
            jtb.ball.move(getMoveX(),0); //move with us while reset
            if(resetCounter-- == 0){
                reset = false;
                //launch ball
                jtb.ball.setMove( jtb.getRandom(10) > 5 ? 1 : -1,-1);            
            }
        }
        
        super.performTimedAction(currentTimeMs);  //Default move sprite    
        setMove(0,0);
    }
    
    private void checkMove(){
        if(preCollidesWithBounds(jtb.map))
            setMove(0,0);
    }
}
