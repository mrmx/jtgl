/*
 * SWMappletContext.java
 *
 * Created on 10 de octubre de 2004, 20:33
 */

package org.jtgl.micro.impl.superwaba;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.impl.superwaba.*;
import org.jtgl.micro.*;

import waba.fx.*;
import waba.ui.*;
import waba.util.Hashtable;
import waba.util.Random;
import waba.sys.Settings;
import waba.sys.Vm;


/**
 *
 * @author  Manuel
 */
public class SWMappletContext extends MainWindow implements MappletContext{
    private SWGraphics gc;    
    private Image dblBuffer;
    private ISurface ssscreen;
    private Graphics g0;
    private Hashtable clientThreadMap;
    private Mapplet currentMapplet;
    private SWImageUtil swImageUtil;
    private boolean ctxDblBuffer;
    private static final String BMP_EXTENSION   =   ".bmp";
    
    /** Creates a new instance of SWMappletContext */
    public SWMappletContext() {
        JTGLContext.registerContext(new SWContext());
        flicker = false;
    }

    public final void runMapplet(Mapplet mapplet) {
        currentMapplet = mapplet;
        try {
            currentMapplet.initialize(this);
        }catch(Exception ex){
            handleException(currentMapplet, ex,"Error initializing");
        }
    }    
    
    public final JTGLImage createJTGLImage(String src) throws JTGLException {
        if(src == null) return null;        
        String locaseSrc = src.toLowerCase();
        if(!locaseSrc.endsWith(BMP_EXTENSION)){            
            int extension_index = locaseSrc.indexOf('.');
            src = src.substring(0,extension_index)+BMP_EXTENSION;            
        }
        JTGLContext.debugMsg("Loading image from "+src);
        Image image = new Image(src);
        if(image.getWidth() == 0 && image.getHeight() == 0)
            throw new JTGLException("Bad image format: "+src);
        JTGLContext.debugMsg("Done loading image!");
        return new SWImage(image);
    }
    
    public final JTGLImage createJTGLImage(JTGLImage image) {
        return image;
    }
    
    public final JTGLImage createJTGLImage(int width, int height) {
        Image image = new Image(width,height);
        return new SWImage(image);
    }
    
    public final JTGLImage createJTGLImage(byte[] imageData, int imageOffset, int imageLength) {
        //TODO: offset 
        Image image = new Image(imageData);
        return new SWImage(image);        
    }
    
    public final void delay(Mapplet mapplet, int ms) {
        Vm.sleep(ms);
    }
    
    public Object get(Mapplet mapplet, String property) {
        //TODO
        return null;
    }
    
    public final ImageUtil getImageUtil() {    
        if(swImageUtil == null)
            swImageUtil = new SWImageUtil(this);
        return swImageUtil;    
    }
    
    public final String[] getAvailableInputControls(Mapplet mapplet) {
        //TODO
        return null;
    }
    
    public final int getDisplayColors(Mapplet mapplet) {        
        return Settings.maxColors;
    }
    
    public final InputControl getInputControl(Mapplet mapplet, String inputControlClassName) {
        //TODO
        return null;
    }
    
    public final JTGLGraphics getJTGLGraphics(Mapplet mapplet) {
        if(gc == null){
            Graphics g = dblBuffer.getGraphics();
            //g.setBackColor(255,255,255);
            //g.clearScreen();           
            gc = new SWGraphics(g,this,true);
            gc.init();
        }
        return gc;
    }
    
    public final void flushGraphics(Mapplet mapplet){
        if(gc != null)
            gc.flush();
    }
    
    public final int getWidth(Mapplet mapplet){
        return getSize().width(); //Whole screen width
    }

    public final int getHeight(Mapplet mapplet){
        return getSize().height(); //Whole screen height
    }
    
    public void handleException(Mapplet mapplet, Throwable ex, String title) {
        //TODO
        JTGLContext.handleException(ex);
    }
    
    public void notifyKill(Mapplet mapplet) {
        try {
            mapplet.kill(true);
        }catch(JTGLException ex){
            handleException(mapplet,ex,null);
            delay(mapplet, 2000);
        }        
        exit(0);
    }    
    
    public final long getCurrentTimeMs(){
        return Vm.getTimeStamp();
    }
    
    //Thread managenent:
    public final void threadSleep(long ms){
        Vm.sleep((int)ms);
    }
    
    public final void threadYield(){        
    }
    
    public final void addThreadClient(JTGLThreadClient client,int priority){
        if(client == null)
            return ;
        if(clientThreadMap == null)
            clientThreadMap = new Hashtable(5);
        WabaClientThread wabaThread = new WabaClientThread();
        wabaThread.client = client;
        clientThreadMap.put(client,wabaThread);
        addThread(wabaThread,priority == JTGLThreadClient.MAX_PRIORITY);
    }
    
    public final void removeThreadClient(JTGLThreadClient client){
        if(client == null || clientThreadMap == null)
            return ;
        WabaClientThread wabaThread = (WabaClientThread)clientThreadMap.get(client);
        if(wabaThread != null)
            removeThread(wabaThread);
    }
    
    final class WabaClientThread implements waba.sys.Thread {
        JTGLThreadClient client;
        public void started(){
            client.started();
        }
        public void run(){
            client.run();            
        }
        public void stopped(){
            client.stopped();
        }
    }
        
    
    
    //
    //MainwWindow methods:
    //
    public final void onStart(){
        JTGLContext.debugMsg("Initializing SWMappletContext");
        JTGLContext.debugMsg("Size:"+getSize());
        setDoubleBuffer(true);
        ctxDblBuffer = false;
        dblBuffer = getOffScreen();
        if(dblBuffer == null)
            try{
                dblBuffer = new Image(getSize().width(),getSize().height());
                ctxDblBuffer = true;
            }catch(Throwable t){
                exit(-1);
            }
        ssscreen = dblBuffer;
        g0 = createGraphics();
        
        if(currentMapplet != null)
            currentMapplet.start();
        //repaintNow();
    }
    
    public final void onExit(){
        if(dblBuffer != null)
            dblBuffer.free();
    }
    
    public final void onPaint(Graphics g){            
        //if(ctxDblBuffer && dblBuffer != null){
        if(dblBuffer != null){
            g.copyScreen(dblBuffer,0,0,height);        
        }        
    }
     
    /*
    public final void repaintNow(){        
        g0.copyScreen(ssscreen,0,0, height);
        super.repaintNow();
    }
     */
    
    final class SWContext extends JTGLContext {
        Random random;
        private SWContext(){
        }
       
        public void _debugMsg(Object obj){
            Vm.debug(""+obj);            
        }
        public void GC(){
            Vm.gc();
        }
        public void copyArray(Object src,int srcPos,Object dest,int destPos,int length){
            Vm.copyArray(src,srcPos,dest,destPos,length);
        }
        public long _getCurrentTimeMs(){
            return Vm.getTimeStamp();
        }
        
        public int getRandom(int from,int to){
            if(random == null)
                random = new Random();
            if(from > to) {
                int save = from;
                from = to;
                to = save;
            }
            int range = to - from + 1;
            // compute a fraction of the range, 0 <= frac < range
            int frac = random.nextInt(range);
            return (frac + from);        
        }
        
        public void _handleException(Throwable ex){
            Sound.beep();
            Vm.debug("Exception:"+ex);
            ex.printStackTrace();
        }
    }    
}
