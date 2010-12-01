/*
 * ObjectBuilder.java
 *
 * Created on August 15, 2010, 2:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import com.rameses.util.BreakException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author elmo
 */
public class MapBuilderHandler implements SchemaHandler {
    
    private Map map;
    private Stack<Map> stack = new Stack();
    private SchemaHandlerStatus status;
    
    public MapBuilderHandler() {
    }
    
    public void startElement(SchemaElement element, Object data) {
        //System.out.println("start element " + element.getName());
        if(status.getFieldContext()==null) {
            Map currentMap = new HashMap();
            stack.push( currentMap );
        }
    }
    
    public void setStatus(SchemaHandlerStatus status) {
        this.status = status;
    }
    
    //do not include fields that are excluded. We need to intelligently
    //check also that a null default value cannot displace a non-empty value
    public void processField(SimpleField f, String refname,  Object value) {
        if( status.isExcludeField( f )) return;
        
        if(!stack.isEmpty()) {
            Map map = stack.peek();
            Object defaultValue = f.getProperties().get("default");
            if( defaultValue==null && map.containsKey(refname)) return;
            try {
                defaultValue = SchemaUtil.formatType(f, defaultValue );
                map.put( refname, defaultValue );
            } catch(Exception ign){
                System.out.println("cannot assign " + defaultValue + " as " + f.getType());
            }
        }
    }
    
    public void endElement(SchemaElement element) {
        if(status.getFieldContext()==null) {
            Map omap = null;
            if(! stack.isEmpty() ) omap = stack.pop();
            if(stack.isEmpty()) this.map = omap;
        }
    }
    
    public Map getMap() {
        return map;
    }
    
    public void startSchema(Schema schema) {;}
    public void startLinkField(LinkField f, String refname,SchemaElement element) {;}
    public void endLinkField(LinkField f) {;}
    public void endSchema(Schema schema) {;}
    
    public void startComplexField(ComplexField cf, String refname, SchemaElement element,Object data) {
        if(element==null) throw new BreakException();
        
        Map map = stack.peek();
        if(cf.getType()==null || cf.getType().trim().length()==0) {
            if(cf.isRequired()) {
                Schema s = element.getSchema();
                Map subData = element.getSchema().getSchemaManager().createMap(s, element);
                map.put(refname, subData);
            }
        } else {
            if(cf.isRequired()) {
                map.put(refname, new ArrayList());
            }
        }
    }
    
    public void endComplexField(ComplexField cf) {
    }
    
    
    
}
