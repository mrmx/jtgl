/*
 * MMines.java
 *
 * Created on 20 de enero de 2004, 19:15
 */

package samples.gaming.mmines;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.gaming.*;
import org.jtgl.micro.*;

/**
 * Classic MinesSweeper implemented with JTGL Gaming API
 * Graphics based on JSeepwer Midlet by JDagon (http://jdagon.com)
 *
 * @version 0.8
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class MMines extends GameMapplet {
    public static int MAX_MINES                 =   32;
    public static int CURSOR_ANIM_DELAY         =   500;    
    public static final int MINE_FOUND          =   -2;    
    public static final String  MSG_GAME_OVER   =   "GAME OVER";
    //ImageMatrix indexes:
    public static final int INVERT_COVER_INDEX  =   1;
    public static final int COVER_INDEX         =   2;
    public static final int PLAIN_INDEX         =   3;
    public static final int ONE_INDEX           =   4;
    public static final int FLAG_INDEX          =   12;
    public static final int WALL_INDEX          =   13;
    public static final int MINE_INDEX          =   14;    
    
    JTGLGraphics gc;
    ImageMatrix images;
    TiledSurface minesMap;
    TiledSurface coverMap;
    Sprite cursor;
    int rows  = 10;
    int cols  = 10;
    int minesCols,minesRows;
    int cursorRow,cursorCol;    
    int [] initialCursorSequence = { INVERT_COVER_INDEX -1,COVER_INDEX-1 };
    int [] cursorSequence = {0,0};
    boolean game_over;
    
    /** Creates a new instance of MMines */
    public MMines() {
    }
    
  
    
    
    public void initGame() throws Exception {   
        try {
            images = new ImageMatrix(createImage("/res/mmines.png"),16,16);
        }catch(Exception ex){
            handleException(ex,"Error loading resources");
            notifyKill();
        }
        gc = getGraphics();        
        gc.begin();
        gc.setFontSize(JTGLFont.SIZE_LARGE);
        cols = gc.getWidth() / 16;
        rows = gc.getHeight() / 16;
        MAX_MINES = cols;
        minesCols = cols - 2;
        minesRows = rows - 2;
        cursor = new Sprite(images);        
        cursor.setAnimationDelay(CURSOR_ANIM_DELAY);
        minesMap = new TiledSurface(minesRows,minesCols,images);
        minesMap.setLocation(minesMap.getCellWidth(), minesMap.getCellHeight());                
        coverMap = new TiledSurface(rows,cols,images);        
        coverMap.fillCells(0,0, cols, 1, WALL_INDEX);//Top wall
        coverMap.fillCells(0,0, 1, rows, WALL_INDEX);//Left wall
        coverMap.fillCells(cols-1,0, 1, rows, WALL_INDEX);//Right wall
        coverMap.fillCells(0,rows-1, cols, 1, WALL_INDEX);//Bottom wall
        setFps(5); //Set a low update for such a game
        resetGame();
    }
    
    private void initMinesMap(){        
        int minesDropped = 0;
        int maxIndex = minesMap.getNumCells() - 1;
        int dropIndex;
        minesMap.fillCells(0,0, minesCols, minesRows, PLAIN_INDEX);//Fill all with plain
        //Drop mines:
        while(minesDropped != MAX_MINES){
            dropIndex = getRandom(maxIndex);
            if(minesMap.getCellValue(dropIndex) != MINE_INDEX){
                minesMap.setCellValue(dropIndex, MINE_INDEX);
                minesDropped++;
            }
        }
    }
    
    private void resetGame(){        
        resetCursor();
        coverMap.fillCells(1,1, cols-2, rows-2, COVER_INDEX);//Fill all
        initMinesMap();
        game_over = false;
    }
    
    private void clearCoverMap(){
        coverMap.fillCells(1,1, cols-1, rows-1, 0);//make transparent all tiles
    }
    
    private void resetCursor(){
        cursorRow = cursorCol = 1;
        resetCursorSequence();
        cursor.setSequence(cursorSequence);        
        positionCursor();
        cursor.setVisible(true);
    }
    
    private void positionCursor(){
        cursor.setLocation(cursorCol * cursor.getWidth(), cursorRow * cursor.getHeight());
    }
    
    private void resetCursorSequence(){
        System.arraycopy(initialCursorSequence,0,cursorSequence,0,cursorSequence.length);        
    }    
    
    private void updateTransparentCursorSequence(){
        cursorSequence[0] = COVER_INDEX-1;
        cursorSequence[1] = minesMap.getCell(cursorRow-1, cursorCol-1)-1;
    }
    
    private void updateFlagCursorSequence(){
        cursorSequence[0] = COVER_INDEX-1;
        cursorSequence[1] = FLAG_INDEX-1;
    }
    

    //When moved 
    private void updateCursor(){
        if(coverMap.getCell(cursorRow,cursorCol) == 0){//Transparent? then update cursor sequence
            updateTransparentCursorSequence();
        }
        else
        if(coverMap.getCell(cursorRow,cursorCol) == FLAG_INDEX)
            updateFlagCursorSequence();
        else
            resetCursorSequence();
        positionCursor();        
    }
    
    private boolean show(int row,int col){
        if(coverMap.getCell(row+1,col+1) == 0)  //Showed? (0 == transparent tile)
            return false;
        int minesAround = count(row,col);   
        if(minesAround == MINE_FOUND)
            return true;
        if(minesAround == -1)   //Illegal position
            return false;        
        minesMap.setCell(row,col,ONE_INDEX + minesAround -1);
        coverMap.setCell(row+1,col+1,0);    //Make transparent 
        if(minesAround > 0)   //
            return false;        
        //continue North
        show(row-1,col);
        //continue NW
        show(row-1,col-1);        
        //continue NE
        show(row-1,col+1);                        
        //continue West
        show(row,col-1);        
        //continue East
        show(row,col+1);        
        //continue South
        show(row+1,col);
        //continue SW
        show(row+1,col-1);
        //continue SE
        show(row+1,col+1);                
        return false;
    }
    
    private int count(int row,int col){
        int minesAround = 0;   
        if(isLegal(row,col)){
           if(minesMap.getCell(row,col) == MINE_INDEX)
               return MINE_FOUND;
           //Top
           if(isLegal(row-1,col-1) && minesMap.getCell(row-1,col-1) == MINE_INDEX)  minesAround++;
           if(isLegal(row-1,col) && minesMap.getCell(row-1,col) == MINE_INDEX)  minesAround++;
           if(isLegal(row-1,col+1) && minesMap.getCell(row-1,col+1) == MINE_INDEX)  minesAround++;
           //Middle
           if(isLegal(row,col-1) && minesMap.getCell(row,col-1) == MINE_INDEX)  minesAround++;           
           if(isLegal(row,col+1) && minesMap.getCell(row,col+1) == MINE_INDEX)  minesAround++;
           //Bottom
           if(isLegal(row+1,col-1) && minesMap.getCell(row+1,col-1) == MINE_INDEX)  minesAround++;
           if(isLegal(row+1,col) && minesMap.getCell(row+1,col) == MINE_INDEX)  minesAround++;
           if(isLegal(row+1,col+1) && minesMap.getCell(row+1,col+1) == MINE_INDEX)  minesAround++;           
           return minesAround;
        }
        return -1;
    }
    
    private boolean isLegal(int row,int col){
        return row >= 0 && row < minesRows & col >= 0 && col < minesCols;
    }
    
    private void flushGame(){
        minesMap.draw(gc);
        coverMap.draw(gc);
        cursor.draw(gc);
        gc.flush();
    }
    
    protected void processGameLogic() {
        int key = getKeys();        
        if(game_over && key != KeyInputControl.KEY_NONE){
            resetGame();
            return ;
        }
        cursor.animate(System.currentTimeMillis());
        if(key == KeyInputControl.KEY_NUM9){
            cursor.setAnimationDelay(cursor.getAnimationDelay() + 100);
            //System.out.println(cursor.getAnimationDelay());
        }else
        if(key == KeyInputControl.KEY_NUM3){
            cursor.setAnimationDelay(cursor.getAnimationDelay() - 100);
            //System.out.println(cursor.getAnimationDelay());
        }
        
        if(key == KeyInputControl.KEY_NUM0)            
            //Cheat by revealing coverMap!
            coverMap.setVisible(!coverMap.isVisible());
        else
        if(key == KeyInputControl.KEY_NUM1)
            resetGame();
        else
        if(key == KeyInputControl.KEY_NUM7){ //Flag
            if(coverMap.getCell(cursorRow,cursorCol) == COVER_INDEX){
                coverMap.setCell(cursorRow,cursorCol,FLAG_INDEX);   //Flag only over covers
                //Change cursor sequence
                updateFlagCursorSequence();                 
            }
            else
            if(coverMap.getCell(cursorRow,cursorCol) == FLAG_INDEX){
                coverMap.setCell(cursorRow,cursorCol,COVER_INDEX);  //restore cover
                //Change cursor sequence
                resetCursorSequence();
            }            
        }
        else            
        if(key == KeyInputControl.KEY_LEFT){
            if(cursorCol > 1){
                cursorCol--;
                updateCursor();
            }
        }else
        if(key == KeyInputControl.KEY_RIGHT){
            if(cursorCol < minesCols){
                cursorCol++;      
                resetCursorSequence();
                positionCursor();            
            }
        }else            
        if(key == KeyInputControl.KEY_UP){
            if(cursorRow > 1){
                cursorRow--;      
                resetCursorSequence();
                positionCursor();            
            }
        }else            
        if(key == KeyInputControl.KEY_DOWN){
            if(cursorRow < minesRows){
                cursorRow++;      
                resetCursorSequence();
                positionCursor();            
            }
        }else
        if(key == KeyInputControl.KEY_ENTER){            
            if(show(cursorRow-1, cursorCol-1)){
                cursor.setVisible(false);                
                clearCoverMap();
                flushGame();
                game_over = true;
            }else {
                //Change cursor sequence
                updateTransparentCursorSequence();
            }
            
        }
            
    }

    protected void drawGameScene() {
        if(game_over){
            gc.setColor(new JTGLColor(getRandom(0xff),getRandom(0xff),getRandom(0xff)));
            gc.drawString(MSG_GAME_OVER, (gc.getWidth() >> 1) - (gc.getFont().stringWidth(MSG_GAME_OVER) >> 1) ,gc.getHeight() >> 1);
            gc.flush();        
        }else
            flushGame();
    }
    
   
    
}
