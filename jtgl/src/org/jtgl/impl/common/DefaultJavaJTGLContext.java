/*
 * DefaultJavaJTGLContext.java
 *
 * Created on 25 de diciembre de 2004, 14:17
 */
package org.jtgl.impl.common;

import java.util.Random;
import org.jtgl.core.JTGLContext;

/**
 * TODO: Make final?
 * @author Manuel
 */
public class DefaultJavaJTGLContext extends JTGLContext{
    Random random;
    /** Creates a new instance of DefaultJavaJTGLContext */
    public DefaultJavaJTGLContext() {
    }
        
    public void _debugMsg(Object obj){
        System.out.println(""+obj);
    }
    public final void GC(){
        System.gc();
    }
    
    public final long _getCurrentTimeMs(){
        return System.currentTimeMillis();
    }
    
    public final void copyArray(Object src,int srcPos,Object dest,int destPos,int length){
        System.arraycopy(src,srcPos,dest,destPos,length);
    }
    
    public final int getRandom(int from,int to){
        if(random == null)
            random = new Random();
        if(from > to) {
            int save = from;
            from = to;
            to = save;
        }
        int range = to - from + 1;
        // compute a fraction of the range, 0 <= frac < range
        int frac = (random.nextInt() >>> 1) % range;
        return (frac + from);
    }
    
    public void _handleException(Throwable ex){
        System.out.println("Exception:"+ex);
        ex.printStackTrace();
    }
    
    
}
