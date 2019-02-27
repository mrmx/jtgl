/*
 * SiemensMediaManager.java
 *
 * Created on 11 de diciembre de 2003, 19:56
 */

package org.jtgl.impl.ext.media.siemens;

import java.io.*;

import org.jtgl.ext.*;
import org.jtgl.ext.media.*;



/**
 *
 * @author  Manuel
 */
public class SiemensMediaManager extends MediaManager{    
    /** Creates a new instance of MediaManagerImpl */
    public SiemensMediaManager() {      
        /*
        String [] protocols = Manager.getSupportedProtocols(null);
        if(protocols != null)
            for(int i = 0; i < protocols.length ; i++){
                System.out.println("Protocol: "+protocols[i]);
                String [] ctypes =  Manager.getSupportedContentTypes(protocols[i]);
                for(int j = 0; j < ctypes.length ; j++)
                    System.out.println("Mime: "+ctypes[j]);
            }
         */
    }
    
    public String getInfo() {
        return "Siemens MediaManager";
    }
    
    public String getVendor() {
        return "Siemens";
    }
    
    public String getVersion() {
        return "1.0";
    }
    
    public void playTone(int note, int duration, int volume) throws MediaException {
        try {
            com.siemens.mp.media.Manager.playTone(note,duration,volume);
        }catch(com.siemens.mp.media.MediaException ex){
            throw new MediaException(ex);
        }
    }
    
    public Player createPlayer(String uri, String mediaType) throws IOException, MediaException {
        try {
            if(uri == null)
                return null;
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
            return new SiemensMediaPlayer(com.siemens.mp.media.Manager.createPlayer(uri));
        }catch(com.siemens.mp.media.MediaException ex){
            throw new MediaException(ex);
        }        
    }
    
    public Player createPlayer(InputStream input, String mediaType) throws IOException, MediaException {
        try {
            return new SiemensMediaPlayer(com.siemens.mp.media.Manager.createPlayer(input,mediaType));
        }catch(com.siemens.mp.media.MediaException ex){
            throw new MediaException(ex);
        }
    }
    
    public String[] getSupportedProtocols(String mediaType) {
        return com.siemens.mp.media.Manager.getSupportedProtocols(mediaType);
    }
    
    public String[] getSupportedContentTypes(String protocol) {
        return com.siemens.mp.media.Manager.getSupportedContentTypes(protocol);
    }
    
    public Object getProperty(String key) {
        return System.getProperty(key);
    }   
    
}
