/*
 * JTGLExtensionManager.java
 *
 * Created on 20 de noviembre de 2003, 17:45
 */

package org.jtgl.ext;

import java.util.*;


/**
 * Extension Manager
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */

public abstract class JTGLExtensionManager {  
    public static String DEFAULT_CLASSNAME =   "org.jtgl.ext.DefaultExtensionManager";    
    private static JTGLExtensionManager instance;

    /** Creates a new instance of JTGLImagingManager */
    protected JTGLExtensionManager() {
    }
    
    public final static JTGLExtensionManager getInstance(){
        if(instance == null){
            try{
                Class.forName(DEFAULT_CLASSNAME);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return instance;
    }    
    
    /**
     * Query for a family of available extensions.
     * Eg: devices available -> getFamilyExtensions("device") would return "Vibrator"      
     */    
    public String[] getFamilyExtensions(String extensionFamily) {
        if(extensionFamily == null || extensionFamily.length() == 0)
            throw new IllegalArgumentException("extensionFamily");
        String [] extensionsKeys = getExtensionsKeys();
        if(extensionsKeys != null){
            Vector vector = new Vector();            
            for(int i = 0 ; i < extensionsKeys.length ; i++){
                String [] split = getFamilyAndClass(extensionsKeys[i]);
                if(extensionFamily.equals(split[0]))
                    vector.addElement(split[1]);                
            }
            if(vector.size() == 0) 
                return null;
            String [] list = new String[vector.size()];
            vector.copyInto(list);
            return list;            
        }
        return null;
    }    
    
    /**
     * Query for all of extensions keys.
     * Extensions keys are composed of a extension family key plus a extension class key.
     * Eg: getExtensionsKeys() would return an array of strings such as:
     *  {"device.Vibrator" , "media.MediaManager" , "imaging.ImageUtil" }
     */
    public abstract String[] getExtensionsKeys();

    /**
     * Returns the number of implemented extensions available for a given extension key.
     * This method may be used previously to access a <code>JTGLExtension</code> instance
     * , by calling getExtension(extensionKey,instanceIndex).
     */    
    public abstract int getExtensionCount(String extensionKey);
    
    
    public boolean addExtension(String extensionKey,String extensionClassName,String extensionName){
        String [] split = getFamilyAndClass(extensionKey);
        return addExtension(split[0], split[1],extensionClassName,extensionName);
    }
    
    public abstract boolean addExtension(String extensionFamily,String extensionClass,String extensionClassName,String extensionName);
    
    public boolean addExtension(String extensionKey,JTGLExtension extension){    
        String [] split = getFamilyAndClass(extensionKey);
        return addExtension(split[0], split[1],extension);
    }
    
    public abstract boolean addExtension(String extensionFamily,String extensionClass,JTGLExtension extension);
    
    public JTGLExtension getExtension(String extensionKey){
        return getExtension(extensionKey,0);
    }
    
    public abstract JTGLExtension getExtension(String extensionKey,int index);
        
    
    
    protected String [] getFamilyAndClass(String extensionKey){
        if(extensionKey == null)
            return null;
        int dot = extensionKey.indexOf('.');
        if(dot < 1)
            return null;
        String extensionFamily = extensionKey.substring(0,dot);
        String extensionClass  = extensionKey.substring(dot+1);        
        return new String[]{extensionFamily ,extensionClass};
    }
    
    protected static void registerManagerInstance(JTGLExtensionManager _instance){
        instance = _instance;
    }

    
    /*
    public static void main(String [] args){
        JTGLExtensionManager em = JTGLExtensionManager.getInstance();
        printArray(em.getFamilyExtensions("imaging"));
        String [] extensions = em.getExtensionsKeys();
        if(extensions != null)
            for(int i = 0; i < extensions.length ; i++){
                System.out.println("Extension: "+extensions[i]);
                JTGLExtension ext = em.getExtension(extensions[i]);
                if(ext != null){                    
                    System.out.println(" Extension name: "+ext.getName());
                    System.out.println(" Extension version: "+ext.getVersion());
                    System.out.println(" Extension vendor: "+ext.getVendor());
                    System.out.println(" Extension info: "+ext.getInfo());
                    System.out.println("---------------------------------------------");
                }
            }        
    }
    
    private static void printArray(String [] array){
        if(array != null)
            for(int i = 0; i < array.length ; i++)
                System.out.println(array[i]);        
    } 
    */
}
