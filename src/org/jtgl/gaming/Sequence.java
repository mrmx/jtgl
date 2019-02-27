/*
 * Sequence.java
 *
 * Created on 23 de febrero de 2004, 21:42
 */

package org.jtgl.gaming;

import org.jtgl.core.JTGLContext;

/**
 * Stores a sequence of values (such as keyFrames of a Sprite, or time-frames,etc..) and provides various 
 * "walks" such as ping-pong,reverse or random.
 *
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class Sequence {
    public static final int DEFAULT     =   0;
    public static final int REVERSE     =   1;
    public static final int PING_PONG   =   2;    
    public static final int RANDOM      =   3;    
    private static JTGLContext jtglContext;
    private int mode;    
    private int [] sequence;    
    private int numFrames;
    private int sequenceIndex;
    private int direction;
    /**
     * Used to expand a fixed sequence (without modifiying initial sequence).
     * Eg: an expansion of 2 converts initial sequence {1,2,3} -> {1,1,2,2,3,3}
     */
    private int expansion;    
    private int expansionCounter;        
    
    /** Creates a new instance of Sequence */
    public Sequence() {
        setExpansion(0);
    }    

    /** Creates a new instance of Sequence */
    public Sequence(int [] sequence,int mode) {
        setSequence(sequence);
        setMode(mode);
    }    
    
    /** Creates a new instance of Sequence */
    public Sequence(int a,int b,int mode) {
        setSequence(a,b);
        setMode(mode);
    }    
        
    /** Creates a new instance of Sequence */
    public Sequence(int [] sequence) {
        this(sequence,DEFAULT);
    }

    /** Creates a new instance of Sequence */
    public Sequence(int a,int b) {
        this(a,b,DEFAULT);
    }
    
    public void setSequence(int a,int b){
        if(a == b)
            return;
        int step = a < b ? 1 :-1;
        int size = 1 + Math.abs(b-a);
        int [] seq = new int[size];
        int i = 0;
        for(; a <= b ; a += step)
            seq[i++] = a;
        setSequence(seq);        
    }

    public void setSequence(int [] newSequence){
        if(newSequence == null)
            return ;
        sequence = newSequence;
        numFrames = sequence.length;        
    }    
    
    public int [] getSequenceArray(){
        return sequence;
    }
    
    public int getMode(){
        return mode;
    }
    
    public void setMode(int mode){
        if(sequence == null)
            throw new RuntimeException();
        switch(mode){
            case DEFAULT:
                sequenceIndex = 0;                
                break;
            case REVERSE:
                sequenceIndex = numFrames - 1;                
                break;
            case PING_PONG:        
                sequenceIndex = 0;                
                direction = 1;
                break;
            case RANDOM:
                sequenceIndex = getRandomFrame();
                break;
            default:
                throw new RuntimeException("unknown mode"+mode);
                
        }
        this.mode = mode;        
    }    
    
    public void setExpansion(int expansion){
        this.expansion = expansion > 1 ? expansion : -1;
        expansionCounter = expansion; //Reset expansion counter
    }
    
    public void setFrame(int sequenceIndex) {
        if(sequenceIndex < 0 || sequenceIndex >= numFrames)        
            throw new IndexOutOfBoundsException();  
        this.sequenceIndex = sequenceIndex;                    
    }

    public int getFrame(){
        return sequenceIndex;
    }    
    

    public int getSequenceLength(){
        return numFrames;
    }

    public int nextFrame(){
        int currIndex = sequenceIndex;
        if(--expansionCounter <= 0){
            expansionCounter = expansion; //Resets counter
            switch(mode){
                case DEFAULT:
                    sequenceIndex = ++sequenceIndex % numFrames;
                    break;
                case REVERSE:
                    if(--sequenceIndex < 0)
                        sequenceIndex = numFrames - 1;
                    break;
                case PING_PONG:      
                    int nextIndex = sequenceIndex + direction;
                    if(nextIndex >= numFrames){
                        sequenceIndex = numFrames - 1;
                        direction = -direction;
                    }else 
                    if(nextIndex < 0){
                        sequenceIndex = 0;
                        direction = -direction;
                    }          
                    sequenceIndex += direction;
                    break;
                case RANDOM:
                    sequenceIndex = getRandomFrame();
                    break;
            }        
        }
            
            
        return sequence[currIndex];
    }
    
    private int getRandomFrame(){
        if(jtglContext == null)
            jtglContext = JTGLContext.getContext();
        return jtglContext.getRandom(numFrames);
    }
}
