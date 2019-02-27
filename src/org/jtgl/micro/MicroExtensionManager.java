/*
 * MicroExtensionManager.java
 *
 * Created on 7 de febrero de 2004, 18:17
 */

package org.jtgl.micro;

import java.util.Hashtable;
import java.util.Enumeration;
import org.jtgl.ext.*;

/**
 * A minimun <code>JTGLExtensionManager</code> implementation,
 * suitable for constrained environments where only a query by extensionKey
 * is enought to access a concrete <code>JTGLExtension</code> instance.
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public final class MicroExtensionManager extends JTGLExtensionManager{
    private static  MicroExtensionManager instance;
    private Hashtable extensionsClasses,extensionsInstances;
    
    /** Creates a new instance of MicroExtensionManager */
    protected MicroExtensionManager() {
        extensionsClasses = new Hashtable(2);
        extensionsInstances = new Hashtable(2);
    }
    
    public static void registerInstance(){
        if(instance == null){
            registerManagerInstance(instance = new MicroExtensionManager());
        }        
    }
    
    
    public boolean addExtension(String extensionFamily, String extensionClass, String extensionClassName, String extensionName) {
        Class extensionClassInstance = null;
        try {
            extensionClassInstance = getClass().forName(extensionClassName);
        }catch(Exception ex){
            //Debug(extensionClassName + " not available");
            return false;
        }
        if(extensionFamily != null && extensionClass != null && extensionName != null){
            String extensionKey = extensionFamily + "." + extensionClass;
            extensionsClasses.put(extensionKey, extensionClassInstance);
            return true;
        }
        return false;
    }
    
    public boolean addExtension(String extensionFamily, String extensionClass, JTGLExtension extension) {
        if(extensionFamily != null && extensionClass != null && extension != null){
            String extensionKey = extensionFamily + "." + extensionClass;
            extensionsClasses.put(extensionKey, extension.getClass());
            extensionsInstances.put(extensionKey,extension);
            return true;
        }
        return false;        
    }    
    
    public JTGLExtension getExtension(String extensionKey, int index) {
        JTGLExtension extension = (JTGLExtension) extensionsInstances.get(extensionKey);
        if(extension == null){
            Class extensionClassInstance = (Class)extensionsClasses.get(extensionKey);
            try {
                extension = (JTGLExtension)extensionClassInstance.newInstance();
                extensionsInstances.put(extensionKey, extension);
            }catch(Exception ex){
            }
        }
        return extension;
    }
    
    public int getExtensionCount(String extensionKey) {
        return extensionsClasses.get(extensionKey) == null ? 0 : 1;
    }
    
    
    public String[] getExtensionsKeys() {
        int numExtensions = extensionsClasses.size();
        if(numExtensions > 0){
            String [] keys = new String[numExtensions];
            for(Enumeration e = extensionsClasses.keys(); e.hasMoreElements() ;){
                keys[--numExtensions] = (String) e.nextElement();
            }
            return keys;
        }
        return null;
    }            
    

    
}
