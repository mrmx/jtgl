/*
 * MediaManager.java
 *
 * Created on 1 de noviembre de 2003, 3:52
 */

package org.jtgl.ext.media;

import java.io.InputStream;
import java.io.IOException;
import org.jtgl.ext.*;

/**
 * MediaManager
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class MediaManager extends JTGLAbstractExtension {    
    /** Creates a new instance of JTGLMediaManager */
    public MediaManager() {
    }    
    public Player createPlayer(String uri) throws IOException,MediaException{
        return createPlayer(uri,null);
    }
    public abstract Player createPlayer(String uri,String mediaType) throws IOException,MediaException;
    public abstract Player createPlayer(InputStream input,String mediaType) throws IOException,MediaException;
    public abstract void playTone(int note,int duration,int volume) throws MediaException;    
    
    public abstract String [] getSupportedProtocols(String mediaType);
    public abstract String [] getSupportedContentTypes(String protocol);
    
    public int getMinVolume(){
        return 0;
    }
    
    public int getMaxVolume(){
        return 100;
    }
}
