package uk.org.ogsadai.activity.astro;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VOTableDataTypeConverter
{
    
    public static final Map<String, String> TAP_TYPE_TO_DSA = 
        new HashMap<String, String>();
    
    public static final Set<String> DSA_TYPES =
        new HashSet<String>( Arrays.asList(
            "float", "int", "string", "dateTime", "boolean", 
            "bit", "unsignedByte", 
            "short", "long", "char", "unicodeChar", "double", 
            "floatComplex", "doubleComplex"));
    
    static
    {
        TAP_TYPE_TO_DSA.put("BOOLEAN", "boolean");
        TAP_TYPE_TO_DSA.put("SMALLINT", "short");
        TAP_TYPE_TO_DSA.put("INTEGER", "int");
        TAP_TYPE_TO_DSA.put("BIGINT", "long");
        TAP_TYPE_TO_DSA.put("FLOAT", "float");
        TAP_TYPE_TO_DSA.put("REAL", "double");
        TAP_TYPE_TO_DSA.put("DOUBLE", "double");
        TAP_TYPE_TO_DSA.put("TIMESTAMP", "dateTime");
        TAP_TYPE_TO_DSA.put("CHAR", "char");
        TAP_TYPE_TO_DSA.put("VARCHAR", "string");
//        TAP_TYPE_TO_DSA.put("BINARY", "");
//        TAP_TYPE_TO_DSA.put("VARBINARY", "");
//        TAP_TYPE_TO_DSA.put("POINT", "");
//        TAP_TYPE_TO_DSA.put("REGION", "");
        TAP_TYPE_TO_DSA.put("CLOB", "string");
//        TAP_TYPE_TO_DSA.put("BLOB", "");
        
    }

}
