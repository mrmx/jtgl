/*
 * DefaultExtensionManager.java
 *
 * Created on 5 de febrero de 2004, 21:10
 */

package org.jtgl.ext;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Default lightweight ExtensionManager implementation.
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class DefaultExtensionManager extends JTGLExtensionManager{
    private Hashtable extensions;
    /** Creates a new instance of DefaultExtensionManager */
    private DefaultExtensionManager() {      
        extensions = new Hashtable(3);
    }
    
    private final class ExtensionEntry {
        Class _class;
        String name,className;
        JTGLExtension instance;
        public ExtensionEntry(String name,String className,Class _class){
            this.name = name;
            this.className = className;
            this._class = _class;
        }
        public int hashCode(){
            return name.hashCode();
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
            Vector entries = (Vector)extensions.get(extensionKey);
            if(entries == null)
                extensions.put(extensionKey,entries = new Vector());
            if(!entries.contains(extensionName)){
                ExtensionEntry entry = new ExtensionEntry(extensionName, extensionClassName,extensionClassInstance);
                entries.addElement(entry);
                return true;
            }
        }
        return false;
    }    
    
    public boolean addExtension(String extensionFamily, String extensionClass, JTGLExtension extension) {
        if(extensionFamily != null && extensionClass != null && extension != null){
            String extensionKey = extensionFamily + "." + extensionClass;
            Vector entries = (Vector)extensions.get(extensionKey);
            if(entries == null)
                extensions.put(extensionKey,entries = new Vector());
            String extensionName = extension.getName();
            if(!entries.contains(extensionName)){
                ExtensionEntry entry = new ExtensionEntry(extensionName, extension.getClass().getName(),extension.getClass());
                entry.instance = extension;
                entries.addElement(entry);
                return true;
            }
        }
        return false;        
    }    
    
    
    public JTGLExtension getExtension(String extensionKey, int index) {
        Vector entries = (Vector)extensions.get(extensionKey);
        if(entries != null)
            try {
                ExtensionEntry entry = (ExtensionEntry)entries.elementAt(index);
                if(entry.instance == null)
                    entry.instance = (JTGLExtension)entry._class.newInstance();
                return entry.instance;
            }catch(Exception ex){
            }        
        return null;
    }
    
    public int getExtensionCount(String extensionKey) {
        Vector entries = (Vector)extensions.get(extensionKey);
        return entries == null ? 0 : entries.size();
    }
    
    public String[] getExtensionsKeys() {
        int numExtensions = extensions.size();
        if(numExtensions > 0){
            String [] keys = new String[numExtensions];
            for(Enumeration e = extensions.keys(); e.hasMoreElements() ;){
                keys[--numExtensions] = (String) e.nextElement();
            }
            return keys;
        }
        return null;
    }
    

    
    static {
        registerManagerInstance(new DefaultExtensionManager());
    }
    
}
