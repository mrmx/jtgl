package samples.gaming.ballz;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;


/**
 * Ballz is a bouncing balls demo, showing collision detection.
 * @author   Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Ballz extends GameMapplet {
    JTGLGraphics gc;    
    MouseInputControl mouseInput;    
    ImageUtil imageUtil;
    
    //Images
    JTGLImage imgSplash;
    JTGLImage imgBall;
    //Sprites
    static int MAX_BALLS = 10;
    Sprite sprBall[];
    RectSurface sfRect;
    
    int moveBallX,moveBallY;
    int incSpeed,speed;
    int screen;
    //screen indexes:
    static final int SCREEN_SPLASH          =   0;
    //Resources:
    static final String strJTGL_Logo        =   "/res/JTGL.png";
    static final String strSprite_Ball      =   "/res/ball.png";
    
    /** Creates a new instance of Ballz */
    public Ballz() {
    }   
    
      
    
    public void initGame() throws JTGLException {                
        gc = getGraphics();
        gc.begin();
        imageUtil = getImageUtil();
        mouseInput = (MouseInputControl)getInputControl(MouseInputControl.CONTROL_CLASSNAME);        
        imgSplash = imageUtil.createScaledImage(
                    createImage(strJTGL_Logo),
                    gc.getWidth(),gc.getHeight()
        );    
        debugMsg("imgSplash = "+imgSplash);
        sprBall = new Sprite[MAX_BALLS];
        imgBall = createImage(strSprite_Ball);
        for(int i = 0; i < MAX_BALLS ; i++){
            sprBall[i] = new Sprite(imgBall);
            sprBall[i].setSpeed(getRandom(10,30));
            sprBall[i].setLocation(
                getRandom(gc.getWidth()), 
                getRandom(gc.getHeight())
            );
        }
        moveBallX = gc.getWidth() >> 1;
        moveBallY = gc.getHeight() >> 1;
        sfRect = new RectSurface(imgBall.getWidth() << 2 , imgBall.getHeight() << 2 );
        sfRect.setAbsRefLocation(moveBallX,moveBallY);
        screen = SCREEN_SPLASH;
        speed = 2;
        incSpeed = 0;
    }
    
    protected void processGameLogic(){
        int key = getKeys();

        if(key == KeyInputControl.KEY_LEFT){
            updateSpeed();
            moveBallX -= speed;
            moveBallX = moveBallX < 0 ? gc.getWidth(): moveBallX;                    
        }else
        if(key == KeyInputControl.KEY_RIGHT){
            updateSpeed();
            moveBallX += speed;
            moveBallX = moveBallX > gc.getWidth() ? 0: moveBallX;
        }else            
        if(key == KeyInputControl.KEY_UP){
            updateSpeed();
            moveBallY -= speed;
            moveBallY = moveBallY < 0 ? gc.getHeight() : moveBallY;
        }else            
        if(key == KeyInputControl.KEY_DOWN){
            updateSpeed();
            moveBallY += speed;
            moveBallY = moveBallY > gc.getHeight() ? 0: moveBallY;
        }else {
            /*
            if(--incSpeed <= 0){
                speed = --speed == 0? 1 : speed;
                incSpeed = 0;
            }
             **/
            speed = 2;
        }
            
        if(key == KeyInputControl.KEY_NUM0)
            notifyKill();
        
        if(key == KeyInputControl.KEY_NUM1){
            Sprite [] old = sprBall;
            sprBall = new Sprite[old.length << 1];
            System.arraycopy(old,0,sprBall, 0,old.length);
            for(int i = old.length; i < sprBall.length ; i++){            
                sprBall[i] = new Sprite(imgBall);
                sprBall[i].setSpeed(getRandom(10,30));
                sprBall[i].setLocation(
                    getRandom(gc.getWidth()), 
                    getRandom(gc.getHeight())
                );
            }            
            debugMsg("Balls = "+ sprBall.length);
            moveBallX = gc.getClipWidth() >> 1;
            moveBallY = gc.getClipHeight() >> 1;            

        }        
        if(key == KeyInputControl.KEY_ENTER){
            for(int i = 0; i < sprBall.length ; i++){            
                sprBall[i].setSpeed(getRandom(10,30));
                sprBall[i].setLocation(
                    getRandom(gc.getWidth()), 
                    getRandom(gc.getHeight())
                );
            }            
            //moveBallX = gc.getClipWidth() >> 1;moveBallY = gc.getClipHeight() >> 1;                        
        }
            
        if(mouseInput != null ){//&& mouseInput.getButtons() == mouseInput.BUTTON_1){            
            //debugMsg("Mouse Buttons: "+mouseInput.getButtons());
            moveBallX = mouseInput.getX();
            moveBallY = mouseInput.getY();
            //debugMsg("Move to "+moveBallX+","+moveBallY);
            //sprBall.incSpeed(1);       
        }
       
    }
    
    protected void drawGameScene(){
        gc.clear(JTGLColor.CYAN);
        switch(screen){
            case SCREEN_SPLASH:
                gc.drawImage(imgSplash,0,0);
                gc.flush();
                delay(2000);
                screen++;
                break;
            default:
                sfRect.draw(gc);
                int i = sprBall.length;
                long drawTime = System.currentTimeMillis();
                while(--i >= 0) {
                    sprBall[i].moveRefTo(moveBallX, moveBallY, false);                   
                    sprBall[i].draw(gc);                
                }
                gc.drawString(""+(System.currentTimeMillis()-drawTime),0,0);
                gc.flush();
                break;
        }        
    }
    
    private final void updateSpeed(){
        if(++incSpeed == 100){
            speed <<= 1;
            speed = speed > 30 ? 30 : speed;
            incSpeed = 0;
        }        
    }
}


