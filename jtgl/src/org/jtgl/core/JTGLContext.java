/*
 * JTGLContext.java
 *
 * Created on 25 de diciembre de 2004, 12:56
 */

package org.jtgl.core;

/**
 * Generic context for specific implementations (OpenGL,Java2D,J2ME Generic,J2ME Specific (Siemens C55,Nokia Platform...))
 * @author  Manuel
 */
public abstract class JTGLContext {
    private static JTGLContext context; 
    
    /** Creates a new instance of Context */
    protected JTGLContext() {
    }    
    
    public final static JTGLContext getContext(){
        if(context == null)
            throw new RuntimeException("JTGL Context not available");
        return context;
    }
    
    public final static void debugMsg(Object msg){
       getContext()._debugMsg(msg);
    }  
    
    public final static void arraycopy(Object src,int srcPos,Object dest,int destPos,int length){
        getContext().copyArray(src, srcPos, dest, destPos, length);
    }
    
    public final static long getCurrentTimeMs(){
        return getContext()._getCurrentTimeMs();
    }
    
    public final static void gc(){
       getContext().GC();
    }  
    
    
    public final static void handleException(Throwable ex){
        try {
            getContext()._handleException(ex);
        }catch(Exception jex){
            _defaultHandleException(jex);
        }
    }
    
    public static boolean findClassName(String className){
        boolean found = false;
        if(className != null)
            try {
                Class.forName(className);
                found = true;
            }catch(ClassNotFoundException ex){
            }
        return found;
    }
    
    
    public final int getRandom(int to){
        return getRandom(0,to);
    }    
    
    public abstract int getRandom(int from,int to);
    
    public abstract void _debugMsg(Object obj);
    public abstract long _getCurrentTimeMs();
    public abstract void copyArray(Object src,int srcPos,Object dest,int destPos,int length);
    public abstract void GC();
    
    public void _handleException(Throwable ex){
        _defaultHandleException(ex);
    }
    
    protected static void _defaultHandleException(Throwable ex){
        //System.out.println(ex); //SuperWaba has no System & PrintStream classes!!
        ex.printStackTrace();
    }  
    

    public final static void registerContext(JTGLContext ctx){
        if(context == null)
            context = ctx;
    }           
}
