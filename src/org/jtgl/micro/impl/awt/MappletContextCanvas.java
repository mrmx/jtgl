/*
 * MappletCanvas.java
 *
 * Created on 12 de enero de 2004, 18:02
 */

package org.jtgl.micro.impl.awt;


import java.io.*;
import java.net.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;

import org.jtgl.core.*;
import org.jtgl.image.*;
import org.jtgl.input.*;
import org.jtgl.impl.common.*;
import org.jtgl.impl.awt.*;
import org.jtgl.micro.*;

/**
 * MappetContext for AWT
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class MappletContextCanvas extends Canvas implements MappletContext {
    private Object lock = new Object();
    private Toolkit toolKit;
    private Container parent;
    private Dimension preferredSize;
    private MediaTracker mediaTracker;    
    private MouseInputControl mouseInputControl;
    private KeyInputControl keyInputControl;
    private Image dblBuffer;    
    private AWTGraphics gc;
    private DefaultJavaThreadManager threadManager;
    private Mapplet currentMapplet;
    private int zoomFactor;    
    /** Creates a new instance of MappletCanvas */
    public MappletContextCanvas(Container parent) {
        this.parent = parent;
        mediaTracker = new MediaTracker(this);            
        toolKit = Toolkit.getDefaultToolkit();
        threadManager = new DefaultJavaThreadManager();
        JTGLContext.registerContext(new DefaultJavaJTGLContext());
        zoomFactor = 1;
    }
    
    public void runMapplet(Mapplet mapplet){
        if(mapplet == null)
            return ;
        pause();    //Stops current mapplet activity
        currentMapplet = mapplet;        
        try {
            currentMapplet.initialize(this);
            start();
        }catch(Exception ex){
            handleException(currentMapplet,ex,null);
        }
    }
    
    public void setPreferredSize(Dimension preferredSize){
        this.preferredSize = preferredSize;
    }
    
    public Dimension getPreferredSize() {
        if (dblBuffer != null) {            
            return new Dimension(dblBuffer.getWidth(null) * zoomFactor,dblBuffer.getHeight(null) * zoomFactor);
        } else {
            if(preferredSize != null)
                return preferredSize;            
            return new Dimension(100, 100);
        }
    }
    
    public Dimension getMinimumSize() {
        if (dblBuffer != null) {
            return getPreferredSize();
        } else {
            return new Dimension(0,0);
        }
    } 
    
    public void addNotify(){
        super.addNotify();
        synchronized(lock){
            updateCanvas();
            repaint();        
            lock.notify();
        }
    }
    
    public void setSize(int width,int height){
        if(getSize().width == width && getSize().height == height)
            return ;
        pause();
        super.setSize(width, height);
        System.out.println("MappletCanvas resized to "+width+"x"+height);        
        invalidate();
        updateCanvas();
        validate();
        pause();            
        start();
        repaint();         
    }
    
    public void setZoom(int zoomFactor){
        int prevZoom = this.zoomFactor;
        this.zoomFactor = zoomFactor < 1 ? 1 : zoomFactor;
        if(this.zoomFactor == prevZoom) //Changed?
            return ;        
        /*
        super.setSize(
            dblBuffer.getWidth(null) * zoomFactor, 
            dblBuffer.getHeight(null) * zoomFactor
        );
         */
        invalidate();
        repaint();
    }
    
    public final void delay(Mapplet mapplet,int ms) {
        try{
            Thread.currentThread().sleep(ms);
        }catch(InterruptedException ex){
        }
    }   
    
    //Thread managenent:
    public void threadSleep(long ms){
        try {                         
            Thread.sleep(ms);                    
        }catch(Exception ex){
            //handleException(currentMapplet, ex, null);
        }
    }
    public void threadYield(){
        Thread.yield();
    }
    
    public void addThreadClient(JTGLThreadClient client,int priority){
        threadManager.addThreadClient(client,priority);
    }
    
    public void removeThreadClient(JTGLThreadClient client){
        threadManager.removeThreadClient(client);
    }
    
    public void notifyKill(Mapplet mapplet) {
        try {
            mapplet.kill(true);
        }catch(JTGLException ex){
            handleException(mapplet,ex,null);
            delay(mapplet, 2000);
        }
        //Kill all mapplet threads:        
        threadManager.killAll();
        if(gc != null)
            gc.end();
        gc = null;
        getJTGLGraphics(null).clear();
        repaint();        
    }
    
    public void start(){
        if(currentMapplet != null)
            currentMapplet.start();
    }
    
    public void pause(){
        if(currentMapplet != null)
            currentMapplet.pause();
    }   
    
    
    public JTGLImage createJTGLImage(String src) throws JTGLException {
        if(src == null || src.length() < 2)
            return null;
        if(parent instanceof Applet){
            Applet applet = (Applet)parent;
            URL url = null;
            try {
                 url = new URL(src);            
            }catch(Exception ex){
                if(src.startsWith("/"))
                    src = src.substring(1); //Remove slash
                try {
                    url = new URL(applet.getCodeBase(),src);
                }catch(MalformedURLException uex){
                    return null;
                }
            }      
            System.out.println("Loading image from url: "+url);
            return getJTGLImage(applet.getImage(url));            
        }
        Image image = null;
        //Test URL:
        try {            
            image = getToolKit().getImage(new URL(src));
        }catch(Exception ex){
            //Test file:
            File file = new File(src);
            if(file.exists() && file.isFile() && file.canRead()){
                image = getToolKit().getImage(src);        
            }else {
                //Default use:       
                InputStream input = getClass().getResourceAsStream(src);
                if(input == null)
                    return null;
                try {
                    byte [] imageData = getByteArray(input);
                    return createJTGLImage(imageData,0, imageData.length);        
                }catch(IOException ioex){
                    throw new JTGLException(ioex);
                }
            }
        }
        return image == null? null : getJTGLImage(image);           
    }
    
    public JTGLImage createJTGLImage(byte[] imageData,int imageOffset,int imageLength){
        return getJTGLImage(getToolKit().createImage(imageData, imageOffset, imageLength));
    }    
        
    public JTGLImage createJTGLImage(int width, int height) {       
        Image image = createImage(width, height);                
        /*
        int size = width*height;
        int [] ibuf = new int[size];
        //int i = 0; //while(i < size)ibuf[i++] =  0x1f000000;
        MemoryImageSource mimg = new MemoryImageSource(width,height,ibuf,0,width);
        Image image = component.createImage(mimg);        
         */
        return image == null? null : getJTGLImage(image);              
    }        

    public JTGLImage createJTGLImage(JTGLImage image) {
        return image;
    }
    
    public final void update(Graphics g){        
        paint(g);
    }
    
    public final void paint(Graphics g){
        if(dblBuffer != null){
            if(zoomFactor == 1)
                g.drawImage(dblBuffer,0,0,null);
            else {
                g.drawImage(dblBuffer,0,0,zoomFactor * dblBuffer.getWidth(null),zoomFactor * dblBuffer.getHeight(null),null);
            }
        }
    }      
    
    public JTGLGraphics getJTGLGraphics(Mapplet mapplet){
        if(gc == null){            
            while(!isVisible() || !isValid())
                synchronized(lock){
                    try {                        
                        lock.wait(100);
                    }catch(InterruptedException ex){
                        break;
                    }
                }                  
            Graphics g = dblBuffer.getGraphics();
            g.setClip(0,0, dblBuffer.getWidth(null), dblBuffer.getHeight(null));
            gc = new AWTGraphics(g, this);
            gc.init();
        }
        return gc;        
    }
    
    public void flushGraphics(Mapplet mapplet){
        //TODO synchronous repaint
        repaint();
    }
    
    public int getWidth(Mapplet mapplet){
        try {
            //WARN: Java +1.2 method!
            return getWidth(); 
        }catch(Exception e){
            return getSize().width;
        }
    }

    public int getHeight(Mapplet mapplet){
        try {
            //WARN: Java +1.2 method!
            return getHeight(); 
        }catch(Exception e){
            return getSize().height;
        }
    }
    
    public void handleException(Mapplet mapplet,Throwable ex,String title) {
        if(title != null)
            JTGLContext.debugMsg("Exception found "+title);
        ex.printStackTrace();
    }
    
    public Object get(Mapplet mapplet, String property) {
        return System.getProperty(property);
    }
    
    public ImageUtil getImageUtil() {
        return new DefaultImageUtil(this);
    }
 
    public int getDisplayColors(Mapplet mapplet) {
        //TODO
        return -1;
    }    
    
    
    public final long getCurrentTimeMs(){
        return System.currentTimeMillis();
    }
    
    
    public byte[] getByteArray(InputStream input) throws IOException{
        return getByteArray(input,-1);
    }    
    
    public byte[] getByteArray(InputStream input,long size) throws IOException{
        if(input == null)
            throw new NullPointerException();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int c;        
        while ( ( (c = input.read()) != -1 ) && (size-- != 0) )
           output.write(c);                    
        output.flush();
        output.close();           
        return output.toByteArray();        
    }
    
    
    
    
   
    
    public String[] getAvailableInputControls(Mapplet mapplet) {
        return new String[] {
                    MouseInputControl.CONTROL_CLASSNAME,
                    MouseMotionInputControl.CONTROL_CLASSNAME,                    
                    KeyInputControl.CONTROL_CLASSNAME
        };
    }
    
    public InputControl getInputControl(Mapplet mapplet,String inputControlClassName) {
        if(
            MouseInputControl.CONTROL_CLASSNAME.equals(inputControlClassName) ||
            MouseMotionInputControl.CONTROL_CLASSNAME.equals(inputControlClassName)
        ){
            if(mouseInputControl != null)
                return mouseInputControl;
            mouseInputControl = new MouseInputControlListener();
            addMouseListener((MouseListener)mouseInputControl);
            addMouseMotionListener((MouseMotionListener)mouseInputControl);
            return mouseInputControl;
        }
        if(KeyInputControl.CONTROL_CLASSNAME.equals(inputControlClassName)){
            if(keyInputControl != null)
                return keyInputControl;
            keyInputControl = new KeyInputControlListener();
            addKeyListener((KeyListener)keyInputControl);
            return keyInputControl;            
        }     
        return null;
    }    

    
    private final class KeyInputControlListener extends KeyInputControl implements KeyListener{        
      
        public void keyPressed(KeyEvent e) {
            updateKeyPressed(e.getKeyCode());
        }
        
        public void keyReleased(KeyEvent e) {
            updateKeyReleased(e.getKeyCode());
        }
        
        public void keyTyped(KeyEvent e) {
            char key = e.getKeyChar();
            updateKeyTyped(key == KeyEvent.CHAR_UNDEFINED ? KeyInputControl.CHAR_NONE : key);
        }
        
        /** 
         * Translates from native keycode to KeyInputControl.KEY_XXX constants
         * @param nativeKey
         * @return
         */    
        protected int translateKey(int nativeKey){       
            switch(nativeKey){
                case KeyEvent.VK_UP:
                    return KEY_UP;
                case KeyEvent.VK_DOWN:
                    return KEY_DOWN;
                case KeyEvent.VK_LEFT:
                    return KEY_LEFT;
                case KeyEvent.VK_RIGHT:
                    return KEY_RIGHT;
                case KeyEvent.VK_ENTER:
                    return KEY_ENTER;
                    
                case KeyEvent.VK_0:
                case KeyEvent.VK_NUMPAD0:
                    return KEY_NUM0;                    
                case KeyEvent.VK_1:
                case KeyEvent.VK_NUMPAD1:
                    return KEY_NUM1;                    
                case KeyEvent.VK_2:
                case KeyEvent.VK_NUMPAD2:
                    return KEY_NUM2;                    
                case KeyEvent.VK_3:
                case KeyEvent.VK_NUMPAD3:
                    return KEY_NUM3;                    
                case KeyEvent.VK_4:
                case KeyEvent.VK_NUMPAD4:
                    return KEY_NUM4;                    
                case KeyEvent.VK_5:
                case KeyEvent.VK_NUMPAD5:
                    return KEY_NUM5;                    
                case KeyEvent.VK_6:
                case KeyEvent.VK_NUMPAD6:
                    return KEY_NUM6;                    
                case KeyEvent.VK_7:
                case KeyEvent.VK_NUMPAD7:
                    return KEY_NUM7;                    
                case KeyEvent.VK_8:
                case KeyEvent.VK_NUMPAD8:
                    return KEY_NUM8;                    
                case KeyEvent.VK_9:
                case KeyEvent.VK_NUMPAD9:
                    return KEY_NUM9;                                                            
                default:
                    return KEY_NONE;
            }
        }
        
    }

    private final class MouseInputControlListener extends MouseInputControl implements MouseListener,MouseMotionListener {
        int buttons;
        int x,y;
        public MouseInputControlListener(){
            buttons = x = y = 0;
        }
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }

        public int getButtons() {
            return buttons;
        }
        
        public void mouseDragged(MouseEvent e) {
            saveEvent(e);
        }
        
        public void mouseMoved(MouseEvent e) {
            saveEvent(e);
        }
        
        public void mouseClicked(MouseEvent e) {
            saveEvent(e);
        }        
        
        public void mouseEntered(MouseEvent e) {
            saveEvent(e);
        }
        
        public void mouseExited(MouseEvent e) {
            saveEvent(e);
        }
        
        public void mousePressed(MouseEvent e) {
            saveEvent(e);
        }
        
        public void mouseReleased(MouseEvent e) {
            saveEvent(e);
        }
        
        private void saveEvent(MouseEvent e){            
            x = e.getX() /zoomFactor;
            y = e.getY() /zoomFactor;            
            int modifiers = e.getModifiers();
            //System.out.println("MouseEvent ("+x+","+y+") "+modifiers);
            buttons = MouseInputControl.BUTTON_NONE;
            if((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
                buttons |= MouseInputControl.BUTTON_1;
            if((modifiers & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK)
                buttons |= MouseInputControl.BUTTON_2;
            if((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
                buttons |= MouseInputControl.BUTTON_3;            
        }       
        
        
    }    
    
   
    protected Image waitForImage(Image image){
        JTGLContext.debugMsg("Loading image...");
        try {
            mediaTracker.addImage(image,0);
            mediaTracker.waitForID(0);
            if(mediaTracker.isErrorID(0)){
                JTGLContext.debugMsg("Error loading image");
                return null;
            }
        }catch(InterruptedException ex){            
            ex.printStackTrace();
            return null;
        }
        return image;
    }  
    
    protected JTGLImage getJTGLImage(Image image) {        
        if(image != null)
            return new AWTImage(waitForImage(image));
        return null;
    }        
    
    protected Toolkit getToolKit(){
        return toolKit;
    }
    
    protected void updateCanvas(){        
        Dimension dim = getSize();  
        if(dim.width <= 0 && dim.height <= 0)
            dim = getPreferredSize();
        JTGLContext.debugMsg("MappletCanvas updated to "+dim.width+"x"+dim.height);
        if(dim.width >0 && dim.height > 0)
            createDblBuffer(dim.width, dim.height);                   
    }
    
    private void createDblBuffer(int width,int height){
        if(gc != null) 
            gc.end();
        gc = null;                        
        if(dblBuffer != null){
            Image newBuffer = createImage(width,height);;
            Graphics newGC = newBuffer.getGraphics();
            newGC.drawImage(dblBuffer,0,0,null);
            newGC.dispose();
            dblBuffer.flush();
            dblBuffer = newBuffer;
            return ;
        }
        dblBuffer = createImage(width,height);
    }      
    

    
}
