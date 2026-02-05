package com.example.my_automation.utils;

import java.util.HashMap;
import java.util.Map;

public class HtmlEscapingUtils {
    
    public static final String NEW_LINE  = "\n";
    public static final String EMPTY  = "";
    private static HashMap<String, String> charMap;         

    static {
        charMap = new HashMap<>();
        charMap.put(NEW_LINE, EMPTY);        
    }

    public static String escapingCharacters(String htmlDoc) {
        if(htmlDoc==null) {
            return htmlDoc;
        }        
        for(Map.Entry<String, String> escapingEntry : charMap.entrySet()) {            
           htmlDoc=htmlDoc.replace(escapingEntry.getKey(), escapingEntry.getValue());          
        }                
        return htmlDoc;
    } 
}
