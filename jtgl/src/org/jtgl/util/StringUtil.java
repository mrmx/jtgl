/*
 * StringUtil.java
 *
 * Created on 24 de enero de 2004, 21:27
 */

package org.jtgl.util;

/**
 *
 * @author  Administrator
 */
public final class StringUtil {
    private final static char [] digits = { '0','1','2','3','4','5','6','7','8','9'};
    
    public static final int parseInt(String str) throws Exception {
        if(str == null)
            throw new Exception("Illegal argument : "+str);
        char [] chrs = str.toCharArray();
        int pos = 1;
        int c = chrs.length;
        int i,parsed;
        parsed = 0;
        while(c > 0){
            char ch = chrs[--c];
            for(i = 0 ; i < digits.length ;i++)
                if(ch == digits[i]) 
                    break;
            if(i == digits.length)
                throw new Exception("Illegal argument : "+str);
            parsed += i * pos;
            pos *= 10;            
        }
        return parsed;
    }

    
    public static void main(String [] args) throws Exception {
        System.out.println(parseInt("1234567890"));
    }
    
}
