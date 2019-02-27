/*
 * Snow.java
 *
 * Created on 22 de diciembre de 2004, 16:40
 */

package samples.gaming.snow;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;


/**
 * Snow Demo
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Snow extends GameMapplet {  
    final static int    MAX_FLAKES  =   200;
    final static int    MAX_DROP_FLAKES  =   10;
    JTGLGraphics g,bg;
    JTGLImage background;
    SpriteList flakes;
    TimeTrigger trigger;
    String fpsMsg;
    String msg;
    int x,y,fontHeight;
    short [] snowLine;
    int topLine;
    
    public void initGame() {       
        setFps(100);
        enableFPSMeasure(true);
        g = getGraphics();
        g.begin();                                
        background = createImage(getWidth(),getHeight());
        bg = background.getGraphics();
        bg.begin();
        bg.clear(JTGLColor.BLACK);
        bg.setColor(JTGLColor.WHITE);
        snowLine = new short[getWidth()];
        trigger = new TimeTrigger(100);
        for(int i = 0 ; i < getWidth() ; i++)
            snowLine[i] = (short)(getHeight()-1);
        msg = "JTGL SnowDemo";        
        fpsMsg = "";
        x = (getWidth() >> 1) - (g.getFont().stringWidth(msg) >> 1);
        y = getHeight()>>1;   
        fontHeight = g.getFont().getHeight();
        flakes = new SpriteList();        
        addFlakes();
    }
    
 
    
    public void processGameLogic(){
        addFlakes();
        flakes.action(getCurrentTimeMs());
        
        if(y <= getHeight())
            y += 1;
        else
            y = -g.getFont().getHeight();
        if(trigger.checkTime()){ //avoid gc
            fpsMsg = "FPS:"+ getRealFPS() + " mFPS:"+getRealMFPS();        
        }

    }
    
    protected void drawGameScene(){
        g.drawImage(background,0,0);
        flakes.draw(g);        
        g.setColor(JTGLColor.YELLOW);
        g.drawString(msg,x,y);
        g.drawString(fpsMsg,x,y+fontHeight);
        flushGraphics();                       
    }
    
    
    private void addFlakes(){
        for(int i = 0 ; i < MAX_DROP_FLAKES ; i++){
            if(flakes.getSize() == MAX_FLAKES) 
                break;
            flakes.add(new SnowFlake(this));            
        }
    }
    
    
}
