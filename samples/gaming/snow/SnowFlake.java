/*
 * SnowFlake.java
 *
 * Created on 26 de diciembre de 2004, 1:08
 */
package samples.gaming.snow;

import org.jtgl.core.*;
import org.jtgl.gaming.*;

/**
 *
 * @author Manuel
 */
public final class SnowFlake extends Sprite{
    Snow snow;
    short [] snowLine;
    int snowWidth;
    JTGLColor color;
    /** Creates a new instance of SnowFlake */
    public SnowFlake(Snow snow) {
        super(1,1);
        this.snow = snow;
        snowLine = snow.snowLine;
        snowWidth = snow.getWidth();
        int level = snow.getRandom(20,255);
        color = new JTGLColor(level,level,level);
        reset();        
    }   
    
    public void performTimedAction(long currentTime){
        //if(getY() % 4 == 0)   setDirection();
        int nextX = getX() + getMoveX();
        if(nextX <0 || nextX > snowWidth){
            reset(); 
            return;
        }
        move();
        checkCollision();
    }
    
    public void draw(JTGLGraphics g){
        g.setColor(color);
        g.fillRect(getX(),getY(),1,1);
    }
    
    private void setDirection(){
        int d = getDirection();
        if(d != 2)
            setMove(d,1);
    }
    
    private int getDirection(){
        int r = snow.getRandom(120);
        return (r < 25) ? -1 : (r < 50) ? 1 : (r < 75) ? 0 :2;
    }    
    
    private void reset(){
        setActionDelay(snow.getRandom(50,100));
        setLocation(snow.getRandom(snowWidth),0);
        setDirection();
    }
    
    private void checkCollision(){
        int x = getX();
        int y = getY();        
        int maxLine;        
        maxLine = snowLine[x];
        if(maxLine > 0 && y >= maxLine){
            draw(snow.bg);
            if(y == maxLine)
                snowLine[x] = (short)--maxLine;
            //snow.flakes.remove(this);
            reset();            
        }
    }
    
}
