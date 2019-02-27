/*
 * SpriteAnimationTest.java
 *
 * Created on 26 de enero de 2004, 12:49
 */

package samples.gaming.animation;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;

/**
 *
 * @author  Manuel
 */
public class AnimationDemo extends GameMapplet{
    JTGLGraphics gc;
    JTGLImage image;
    Sprite sprite;            
    int fpsYPos;
    
    /** Creates a new instance of SpriteAnimationTest */
    public AnimationDemo() {        
    }    
        
    
    public void initGame() throws Exception {                
        gc = getGraphics();
        gc.begin();  
        gc.setFontSize(JTGLFont.SIZE_SMALL);
        fpsYPos = gc.getFont().getHeight();
        enableFPSMeasure(true);
        //setFps(10);                
        image = createImage("/res/Dog.png");
        //image = createImage("/res/SpikyPod.png");
        sprite = new Sprite(image,32,32);        
        //sprite = new Sprite(image,64,64);
        //sprite = new Sprite(image,96,96);
        sprite.setMove(-3, 0);
        sprite.setActionDelay(50);
        sprite.setAnimationDelay(100);
        sprite.setLocation(getWidth(),getHeight() >> 1);        
    }    
    
    protected void processGameLogic() {
        if(sprite.getX() > - sprite.getWidth()){
            long time = getCurrentTimeMs();
            sprite.action(time);
            sprite.animate(time);                  
        }else {            
            sprite.setLocation(getWidth(),getHeight() >> 1); //Loop from the right            
            debugMsg(getWidth()+"x"+getHeight());
        }
        
    }
    
    protected void drawGameScene() {
        gc.clear();
        sprite.draw(gc);
        gc.drawImage(image,0,0);
        
        gc.drawString("FPS:"+ getRealFPS() + " mFPS:"+getRealMFPS(),0,fpsYPos);
        gc.flush();
    }       
    
}
