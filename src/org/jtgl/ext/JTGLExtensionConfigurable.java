/*
 * JTGLExtensionConfigurable.java
 *
 * Created on 21 de noviembre de 2003, 13:41
 */

package org.jtgl.ext;

import java.util.Hashtable;

/**
 * Defines a generic configurable extension
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public abstract class JTGLExtensionConfigurable extends JTGLAbstractExtension{
    public final static String TYPE_BOOLEAN    =   "B";
    public final static String TYPE_BYTE       =   "b";
    public final static String TYPE_INT        =   "i";
    public final static String TYPE_FINT       =   "I"; //16.16 fixed point int
    public final static String TYPE_LONG       =   "L";
    public final static String TYPE_FLOAT      =   "F";
    public final static String TYPE_STRING     =   "S";
    public final static String TYPE_OBJECT     =   "O";
    private Hashtable params;
    
    
    /** Creates a new instance of JTGLExtensionConfigurable */
    public JTGLExtensionConfigurable() {
        params = new Hashtable();
        String [][] paramInfo = getParamInfo();
        for(int i = 0 ; i < paramInfo.length ; i++)
            setParam(paramInfo[i][1], new Value(paramInfo[i][2],paramInfo[i][0]));        
    }
    
    
    public void setParam(String param,Object newValue){
        if(newValue == null)
            newValue = "null";
        if(newValue instanceof Value){
            params.put(param,newValue);
            newValue = ((Value)newValue).value;
        }
        Value value = (Value)params.get(param);
        if(value != null)
            update(param,value,newValue);
    }
    
    public Object getParam(String param){
        Value value = (Value)params.get(param);
        if(value == null)
            return null;
        return value.value;
    }
    
    
    public abstract String [][] getParamInfo();
    protected void onUpdateBoolean(String param,boolean value){
    }    
    protected void onUpdateByte(String param,byte value){
    }    
    protected void onUpdateInt(String param,byte value){
    }        
    protected void onUpdateString(String param,String value){
    }
    protected void onUpdateObject(String param,Object value){
    }

    private void update(String param,Value value,Object newValue){
        value.update(newValue);
        char type = value.type.charAt(0);
        switch(type){
            case 'S'://TYPE_STRING
            onUpdateString(param,value.valueString);
            case 'O'://TYPE_OBJECT
            default:
                onUpdateObject(param,value.value);
                break;
        }        
    }    
    
    protected final class Value {
        boolean valueBolean;
        byte valueByte;
        int valueInt;
        long valueLong;
        String valueString;
        Object value;      //General value  
        String type;
        /////        
        public Value(Object value,String type){
            this.value = value;
            this.type = type;
        }
        
        void update(Object newValue){            
            char _type = type.charAt(0);
            switch(_type){
                case 'S'://TYPE_STRING
                    if("null".equals(newValue)){
                        valueString = null ;
                        value = null;
                    }else {
                        valueString = newValue.toString();
                        value = valueString;
                    }
                case 'O'://TYPE_OBJECT
                default:
                    value = newValue;
                    break;
            }
            
        }
        
    }
    
}
