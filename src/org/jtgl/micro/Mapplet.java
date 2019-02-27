/*
 * Mapplet.java
 *
 * Created on 19 de diciembre de 2003, 17:03
 */

package org.jtgl.micro;

import org.jtgl.core.*;
import org.jtgl.input.*;
import org.jtgl.image.JTGLImage;



/**
 * Defines a Micro Application (aka Mapplet) focused on micro environments (such as MIDP,ExEn,DoJa).
 * @author Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class Mapplet {
    private MappletContext context;
    private boolean initialized;
    private boolean debugMessages = true;
    
    public Mapplet(){            
    } 
    
    public Mapplet(MappletContext context){        
        setContext(context);
    }     
    
    /** Initialize Mapplet */    
    public final void initialize(MappletContext context) throws JTGLException{
        if(!initialized){
            setContext(context);
            debugMsg("JTGL (Java Tiny Gfx Library) v0.77 http://www.jtgl.org");
            initialize();
            initialized = true;
        }
    }
    
    /** Initialize Mapplet 
     * Signals that mapplet has a context
     */    
    public void initialize() throws JTGLException{
    }
    
    /** Start Mapplet */    
    public void start(){
    }
    /** Pause Mapplet */    
    public void pause() {
    }
    
    /** Kill Mapplet */    
    public void kill(boolean force) throws JTGLException{
    }       
    
    /** Gets global graphics context
     * @return JTGLGraphics instance
     */    
    public JTGLGraphics getGraphics(){
        return context != null ? context.getJTGLGraphics(this) : null;
    }
    

    public final void flushGraphics(){
        context.flushGraphics(this);
    }
    
    public final int getWidth(){
        return context.getWidth(this);
    }

    public final int getHeight(){
        return context.getHeight(this);
    }    
    
    
    public final int getDisplayColors(){
        return context.getDisplayColors(this);
    }
    
    public JTGLImage createImage(String src) throws JTGLException {
        return context.createJTGLImage(src);
    }
    
    public JTGLImage createImage(int width,int height){
        return context.createJTGLImage(width, height);
    }
    
    public JTGLImage createImage(JTGLImage image){
        return context.createJTGLImage(image);
    }
    
    public Object get(String property){
        return context.get(this,property);
    }
    
    public ImageUtil getImageUtil(){
        return context.getImageUtil();
    }
    
    
    public final long getCurrentTimeMs(){
        return context.getCurrentTimeMs();        
    }     
    
    public final void delay(int ms){
        context.delay(this,ms);
    }
    
    //Thread management:    
    public final void threadSleep(long ms){
        context.threadSleep(ms);        
    }
    public final void threadYield(){
        context.threadYield();        
    }    
    public final void addThreadClient(JTGLThreadClient client,int priority){
        context.addThreadClient(client,priority);
    }    
    public final void removeThreadClient(JTGLThreadClient client){
        context.removeThreadClient(client);
    }
    
    
    public void notifyKill(){
        context.notifyKill(this);
    }
    
    public void handleException(Throwable ex){
        handleException(ex,null);
    }
    
    public void handleException(Throwable ex,String title){
        context.handleException(this,ex,title);
    }    
    
    // Input handling:
    public String [] getAvailableInputControls(){
        return context.getAvailableInputControls(this);
    }
    public InputControl getInputControl(String inputControlClassName){
        return context.getInputControl(this, inputControlClassName);
    }
    
    
    
    //Debugging:
    public final void setDebug(boolean debugMessages){
        this.debugMessages = debugMessages;
    }    
    /** 
     * Print to console (if implemented), useful for debugging, etc
     * @param obj Object to print to console
     */    
    public final void debugMsg(Object obj){
        if(debugMessages && context != null)
            JTGLContext.debugMsg(obj);
    }
    
    void setContext(MappletContext context){
        this.context = context;
    }
    
}
