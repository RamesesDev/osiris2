/*
 * JSProxyWriter.java
 *
 * Created on July 2, 2012, 4:29 PM
 * @author jaycverg
 */

package com.rameses.web.support;

import com.rameses.common.AsyncHandler;
import java.io.Writer;
import java.util.List;
import java.util.Map;


public class JSProxyWriter {
    
    private Map classInfo;
    private boolean prettyJS = true;
    
    public JSProxyWriter(Map classInfo) {
        this.classInfo = classInfo;
    }
    
    public JSProxyWriter(Map classInfo, boolean prettyJS) {
        this(classInfo);
        this.prettyJS = prettyJS;
    }
    
    public void write(Writer w) throws Exception {
        String name = (String) classInfo.get("name");
        w.write( "new function() {"  );
        if( prettyJS ) w.write("\n");
        w.write( "this._p =  new DynamicProxy().create(\""+ name + "\");"  );
        if( prettyJS ) w.write("\n");
        
        List<Map> methods = (List)classInfo.get("methods");
        for( Map mth : methods ) {
            //check first if the method contains an AsyncHandler
            StringBuffer args = new StringBuffer();
            StringBuffer parms = new StringBuffer();

            String methodName = (String)mth.get("name");
            List params = (List)mth.get("params");
            
            boolean includeMethod = true;
            int i=0;
            
            //search each parameter if it has AsyncHandler. do not continue if there is.
            for(i=0;i<params.size();i++) {
                String clz = (String)params.get(i);
                if(clz.equals(AsyncHandler.class.getName())) {
                    includeMethod = false;
                    break;
                }
                //arguments
                args.append( "a" + i + ",");
                
                //parameters
                if( i > 0 ) parms.append(", ");
                parms.append( "a" + i);
            }
            if(!includeMethod) continue;
            
            w.write("this." + escapeMethodName(methodName) + "= function(");
            w.write(args.toString());
            w.write("handler ) {");
            if(  prettyJS ) w.write("\n");
            //if( !mth.get("returnType").equals("void") ) w.write("return ");
            w.write( "return this._p.invoke(\"" + methodName + "\"" );
            w.write( ",");
            w.write("["+parms.toString()+"]");
            w.write(", handler );");
            if( prettyJS ) w.write("\n");
            
            w.write("};");
            if( prettyJS ) w.write("\n");
        }
        
        w.write( "}" );
    }
    
    private String escapeMethodName(String name) {
        if("delete".equals(name)) {
            return "_" + name;
        }
        else if("export".equals(name)) {
            return "_" + name;
        }
        else if("function".equals(name)) {
            return "_" + name;
        }
        else if("var".equals(name)) {
            return "_" + name;
        }
        else if("yield".equals(name)) {
            return "_" + name;
        }
        return name;
    }
    
    
}
