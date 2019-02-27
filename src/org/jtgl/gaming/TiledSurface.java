/*
 * TiledSurface.java
 *
 * Created on 5 de diciembre de 2003, 10:58
 */

package org.jtgl.gaming;

import org.jtgl.core.*;
import org.jtgl.image.*;

/**
 * Represents a surface compound of image tiles (each tile could be empty of filled with a static or animated image)
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class TiledSurface extends Surface{    
    ImageMatrix imageMatrix;
    short [] cells;
    short [] animated;
    int numAnimated;
    int [][] savedCells = {};
    int numSavedCells;    
    int numCells;
    int numTiles;
    int cellRows;
    int cellColumns;
    int cellWidth;
    int cellHeight;
    int viewX;
    int viewY;
    int viewWidth;
    int viewHeight;
    int viewCols;
    int viewRows;
    JTGLRect tmpRect;
    boolean enableClippedViewPort;
    
    public boolean showGrid;
    
    /** Creates a new instance of TiledSurface */
    public TiledSurface(int cellRows, int cellColumns, ImageMatrix imageMatrix) {
        super(cellColumns * imageMatrix.getImageWidth(),cellRows * imageMatrix.getImageHeight());
        if(cellRows < 0 || cellColumns < 0)
            throw new RuntimeException();
        this.imageMatrix = imageMatrix;        
        this.cellWidth = imageMatrix.getImageWidth();
        this.cellHeight = imageMatrix.getImageHeight();        
        this.numTiles = imageMatrix.getNumImages();                
        resetMap(cellRows, cellColumns);
    }
    
    public TiledSurface(int cellRows, int cellColumns, JTGLImage image, int frameWidth, int frameHeight) {    
        this(cellRows,cellColumns, new ImageMatrix(image, frameWidth, frameHeight));
    }
    
    public void resetMap(int cellRows,int cellColumns){
        setWidth(cellColumns * imageMatrix.getImageWidth());
        setHeight(cellRows * imageMatrix.getImageHeight());
        this.cellRows = cellRows;
        this.cellColumns = cellColumns;
        this.numCells = cellRows * cellColumns;
        this.cells = new short[numCells];        
        setViewWindow(0,0, this.width,this.height);
    }
    
    public void setStaticTileSet(JTGLImage image, int cellWidth, int cellHeight) {
        setImageMatrix(new ImageMatrix(image, cellWidth, cellHeight),true);
    }
    
    public void setImageMatrix(ImageMatrix imageMatrix,boolean clearMap){
        if(imageMatrix == null)
            return ;
        setWidth(cellColumns * imageMatrix.getImageWidth());
        setHeight(cellRows * imageMatrix.getImageHeight());
        if(numTiles > imageMatrix.getNumImages()){
            animated = null;
            numAnimated = 0;
            if(clearMap)
                fillCells(0,0, cellColumns-1, cellRows-1, 0);
        }
        this.imageMatrix = imageMatrix;
        this.cellWidth = imageMatrix.getImageWidth();
        this.cellHeight = imageMatrix.getImageHeight();        
        this.numTiles = imageMatrix.getNumImages();        
    }
    
    public final int getNumCells(){
        return numCells;
    }
    
    public final int getNumTiles(){
        return numTiles;
    }
    
    
    public final int getAnimatedCells(){
        return numAnimated;
    }   
    
    public final int getRows(){
        return cellRows;
    }

    public final int getColumns(){
        return cellColumns;
    }
    
    public final int getCellWidth(){
        return cellWidth;
    }
    
    public final int getCellHeight(){
        return cellHeight;
    }
    
    public void setMap(short [] map){                    
        JTGLContext.arraycopy(map,0,cells,0,map.length);
    }
    
    public short [] getMap(){
        return cells;
    }
    
    
    public void setCell(int col,int row,int index){
        if(row < 0 || row >= this.cellRows || col < 0 || col >= this.cellColumns)
            throw new IndexOutOfBoundsException();         
        cells[row * cellColumns + col] = (short)index;
    }
    
    public void setCellValue(int index,int value){    
        cells[index] = (short)value;
    }   
        
    public short getCell(int col,int row){
        if(row < 0 || row >= cellRows || col < 0 || col >= cellColumns)
            throw new IndexOutOfBoundsException();         
        return cells[row * cellColumns  + col];        
    }
    
    public short getCellValue(int index){
        return cells[index];
    }
    
    public int getCellX(int col){
        return col * cellWidth;
    }

    public int getCellY(int row){
        return row * cellHeight;
    }
    
    public int getCol(int x){        
        return x / cellWidth;
    }

    public int getRow(int y){
        return y / cellHeight;
    }
    
    public short getCellAt(int x,int y){
        int col = x / cellWidth;
        int row = y / cellHeight;
        if(row < 0 || row >= cellRows || col < 0 || col >= cellColumns)
            return -1;
        return cells[row * cellColumns  + col];        
    }
    
    /**
     * Modify all cells with values <code>oldIndex</code> to value <code>newIndex</code>
     */
    public final void modCell(int oldIndex,int newIndex){
        int c = numCells;
        short ni = (short)newIndex;
        while(--c >= 0)            
            if(cells[c] == oldIndex)
                cells[c] = ni;
        
    }
    
    /**
     * Faster method than modCell(int oldIndex,int newIndex) that scans whole map and save 
     * all cell locations (indexes within cell-map) containing the passed <code>value</code>     
     */
    public int modSaveCells(int value,int reserveSlots){
        numSavedCells++;        
        if(savedCells.length == 0)
            savedCells = new int[1][];        
        if(numSavedCells > savedCells.length ) {
	    int [][] old = savedCells;	    
	    savedCells = new int[numSavedCells][];
            for(int a = 0 ; a < numSavedCells - 1 ; a++){
                int [] oldSlots = old[a];
                savedCells[a] = new int[oldSlots.length];
                JTGLContext.arraycopy(oldSlots, 0, savedCells[a], 0, oldSlots.length);
            }
	}
        int savedIndex = numSavedCells-1;
        if(savedCells[savedIndex] == null)
            savedCells[savedIndex] = new int[reserveSlots];
        int i = numCells;
        int found = 0;
        while(--i >= 0){
            if(cells[i] == value){                
                if(found >= savedCells[savedIndex].length){
                    int [] old = savedCells[savedIndex];
                    int [] newSlot = new int[found+reserveSlots];   //Reserve more than found
                    JTGLContext.arraycopy(old, 0, newSlot, 0, found);
                    savedCells[savedIndex] = newSlot;
                }
                savedCells[savedIndex][found++] = i;                                
            }
        }
        
        if(found > 0 && found < savedCells[savedIndex].length){
            //Trim array:
            int [] oldFound = savedCells[savedIndex];
            savedCells[savedIndex] = new int[found];
            JTGLContext.arraycopy(oldFound, 0, savedCells[savedIndex], 0, found);
        }
        return savedIndex;
    }

    /**
     * Faster method than modCell(int oldIndex,int newIndex) that scans whole map and save 
     * cell locations containing the passed <code>value</code>     
     */
    public int modSaveCells(int value){
        return modSaveCells(value, 8);
    }
    
    /**
     * Modify all cells with values <code>oldIndex</code> to value <code>newIndex</code>.
     * This method is faster than <code>modCell</code> but requires a previous call to
     * <code>modSaveCells</code> to allocate an index to a saved value.
     * @see modSaveCells
     */
    public void modCells(int savedIndex,int newValue){
        int [] savedIndexes = savedCells[savedIndex];
        int i = savedIndexes.length;
        short nv = (short)newValue;
        while(--i >= 0)
            cells[savedIndexes[i]] = nv;
        
    }
    
    
    public int createAnimatedTile(int staticIndex) {        
        if(staticIndex < 0 || staticIndex >= numTiles)
	    throw new IndexOutOfBoundsException();
        
        if(animated == null){
	    animated = new short[4];
	    numAnimated = 1;
        }else 
            if(numAnimated == animated.length) {
                // grow anim_to_static table if needed 
                short [] tmp_animated = new short[animated.length << 1];
                JTGLContext.arraycopy(animated, 0, tmp_animated, 0, animated.length);
                animated = tmp_animated;
            }
	animated[numAnimated] = (short)staticIndex;
	numAnimated++;
        return -(numAnimated - 1);
    }
    
    /**
     * Associates an animated tile with the specified static tile.  <p>
     *
     * @param animatedIndex the index of the animated tile
     * @param staticIndex the index of the associated tile
     * (must be <code>0</code> or a valid static tile index)
     * @throws IndexOutOfBoundsException if the 
     * <code>staticIndex</code> is invalid
     * @throws IndexOutOfBoundsException if the animated tile index
     * is invalid
     * @see #getAnimatedTile
     *
     */
    public void setAnimatedTile(int animatedIndex, int staticIndex) {     
        if(staticIndex < 0 || staticIndex > numTiles)
	    throw new IndexOutOfBoundsException();	
        // do animated tile index check
	animatedIndex = -animatedIndex;
	if(animated == null || animatedIndex <= 0 || animatedIndex >= numTiles)
	    throw new IndexOutOfBoundsException();        
        animated[animatedIndex] = (short)staticIndex;
    }

    /**
     * Gets the tile referenced by an animated tile.  <p>
     *
     * Returns the tile index currently associated with the
     * animated tile.
     *
     * @param animatedIndex the index of the animated tile
     * @return the index of the tile reference by the animated tile
     * @throws IndexOutOfBoundsException if the animated tile index
     * is invalid
     * @see #setAnimatedTile
     */
    public short getAnimatedTile(int animatedIndex) {
        animatedIndex = -animatedIndex;
        if(animated == null || animatedIndex <= 0 || animatedIndex >= numTiles)
	    throw new IndexOutOfBoundsException();
        return animated[animatedIndex];
    }    
    
        
    public void fillCells(int col, int row, int numCols, int numRows,int cellValue) {
        if( row < 0 || row >= this.cellRows || 
            col < 0 || col >= this.cellColumns ||            
            numRows < 0 || (row + numRows) > this.cellRows ||
            numCols < 0 || ( col + numCols) > this.cellColumns
        )
            throw new IndexOutOfBoundsException();         
	if(cellValue > 0 && cellValue > numTiles) 
            throw new IndexOutOfBoundsException();	    
	else 
            if(cellValue < 0 && (animated == null || (-cellValue) >= numAnimated ))
                throw new IndexOutOfBoundsException();
        int r,c,baseIndex;
        for (r = row; r < row + numRows; r++){
            baseIndex = r * cellColumns;
            for (c = col; c < col + numCols; c++)
                cells[baseIndex + c] = (short)cellValue;        
        }
    
    }

    /*
    public void setClippedViewPort(boolean enable){
        enableClippedViewPort = enable;
    }
     */
    
    /*
     * Compute visible viewWindow from visible viewport (such as SurfaceManager's)
     */
    public void setViewWindowFromViewPort(int vx, int vy, int vWidth, int vHeight){
        if(tmpRect == null)
            tmpRect = new JTGLRect();
        JTGLRect.intersection(x,y,width,height,vx,vy,vWidth,vHeight,tmpRect);
        setViewWindow(tmpRect.x,tmpRect.y,tmpRect.width-tmpRect.x,tmpRect.height-tmpRect.y);        
    }
    
    
    public void setViewWindow(int x, int y, int width, int height) {
        if(width < 0 || height < 0) {
            viewWidth = viewHeight = viewCols = viewRows = 0;
        } else {            
            //View <= mapWidth:
            viewWidth = width > this.width ? this.width : width;
            viewHeight = height > this.height ? this.height : height;            
            viewCols = updateViewCols(viewWidth,cellWidth);
            viewRows = updateViewRows(viewHeight,cellHeight);
            //System.out.println("ViewWindow updated to "+viewCols+"/"+cellColumns+" cols "+viewRows+"/"+cellRows+" rows");            
        }
        setViewLocation(x,y);
    }
    
    public void setViewLocation(int x,int y){
        //View must fit within map:
        viewX = x < 0 ? 0 : (x > width - viewWidth ? width - viewWidth : x );            
        viewY = y < 0 ? 0 : (y > height - viewHeight ? height - viewHeight : y );                    
    }
    
    public int getViewX(){
        return viewX;
    }
    public int getViewY(){
        return viewY;
    }
    public int getViewWidth(){
        return viewWidth;
    }
    public int getViewHeight(){
        return viewHeight;
    }
    
    public void moveView(int dx,int dy){
        setViewLocation(viewX + dx,viewY + dy);
    }    
    
    public void draw(JTGLGraphics g) {
        if(visible){    
            //TODO: if(hasViewWindow && (viewWidth == 0 || viewHeight == 0)) return; 
            int offsetX = viewX % cellWidth;
            int offsetY = viewY % cellHeight;
            int cellX = viewX/cellWidth;
            int cellY = viewY/cellHeight;
            int cellIndex = cellY * cellColumns + cellX;                            
            int vCols = viewCols;
            int vRows = viewRows;            
            if(offsetX >0 && (cellX + vCols < cellColumns)) vCols++;            
            if(offsetY >0 && (cellY + vRows < cellRows)) vRows++;                       
            int x0 = x - offsetX;            
            int xpos = x0;
            int ypos = y - offsetY;
            int r,c;
            short cell;
            //System.out.println("OffsetX = "+offsetX+" OffsetY = "+offsetY);
            //System.out.println("Drawing ("+cellX+","+cellY+","+vCols+","+vRows+") at ("+viewX+","+viewY+")");
            if(enableClippedViewPort){
                g.saveClip();
                g.setClip(x,y,viewWidth,viewHeight);
            }
            for(r = 0; r < vRows; r++){
                for(c = 0; c < vCols; c++){ 
                    cell = cells[cellIndex+c];                
                    if(cell < 0)
                        cell = getAnimatedTile(cell);
                    if(cell > 0) //non empty cell?
                        imageMatrix.draw(g, xpos, ypos, cell-1);                        
                    
                    if(showGrid) g.drawRect(xpos, ypos,cellWidth,cellHeight,JTGLColor.RED);     
                    xpos += cellWidth;
                }
                xpos = x0;
                ypos += cellHeight;
                cellIndex += cellColumns;
            }
            //g.drawRect(x,y,viewWidth-1,viewHeight-1,JTGLColor.BLACK);
            if(enableClippedViewPort)
                g.restoreClip();
        }
    }   
    
    protected int updateViewCols(int viewWidth,int cellWidth){
        int vCols = viewWidth / cellWidth;
        if(viewWidth % cellWidth > 0) vCols++;
        return vCols;
    }
    protected int updateViewRows(int viewHeight,int cellHeight){
        int vRows = viewHeight / cellHeight;
        if(viewHeight % cellHeight > 0) vRows++;
        return vRows;
    }

}
