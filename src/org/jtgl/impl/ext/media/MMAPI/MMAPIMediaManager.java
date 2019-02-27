/*
 * MediaManager.java
 *
 * Created on 6 de febrero de 2004, 22:39
 */

package org.jtgl.impl.ext.media.MMAPI;

import java.io.*;
import org.jtgl.ext.media.*;

/**
 *
 * @author  Administrator
 */
public class MMAPIMediaManager extends MediaManager {
    private javax.microedition.media.Manager manager;
    private String [] protocols;
    /** Creates a new instance of MediaManager */
    public MMAPIMediaManager() {        
        protocols = getSupportedProtocols(null);
    }
    
    public Player createPlayer(String uri, String mediaType) throws IOException, MediaException {
        try {
            if(uri == null)
                return null;
            boolean internalResource = true;
            if(protocols != null){
                for(int i = 0; i < protocols.length ; i++)
                    if(uri.startsWith(protocols[i])){
                        internalResource = false;
                        break;
                    }
            }
            if(internalResource && uri.indexOf(':') == -1 && !uri.startsWith("/"))
                uri = "/" + uri;            
            if(uri.startsWith("/")){
                System.out.println("Opening internal resource "+uri);
                InputStream is = getClass().getResourceAsStream(uri);
                if(is != null)
                    return createPlayer(is,mediaType);
                else {
                    System.out.println("Can not open resource from "+uri);
                    return null;
                }
            }            
            return new MMAPIPlayer(manager.createPlayer(uri));
        }catch(javax.microedition.media.MediaException ex){
            throw new MediaException(ex);
        }        
    }
    
    public Player createPlayer(InputStream input, String mediaType) throws IOException, MediaException {
        try {
            return new MMAPIPlayer(manager.createPlayer(input,mediaType));
        }catch(javax.microedition.media.MediaException ex){
            throw new MediaException(ex);
        }           
    }
    
    public String[] getSupportedProtocols(String mediaType) {
        return manager.getSupportedProtocols(mediaType);
    }    
    
    public String[] getSupportedContentTypes(String protocol) {
        return manager.getSupportedContentTypes(protocol);
    }        
    
    public String getInfo() {
        return "MMAPI MediaManager";
    }
    
    public String getVendor() {
        return "JTGL";
    }
    
    public String getVersion() {
        return "1.0";
    }
    
    public Object getProperty(String key) {
        return System.getProperty(key);
    }
    
    public void playTone(int note, int duration, int volume) throws MediaException {
        try {
            manager.playTone(note, duration, volume);
        }catch(javax.microedition.media.MediaException ex){
            throw new MediaException(ex);
        }
    }   


}
