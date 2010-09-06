/*
 * BeanResolver.java
 *
 * Created on August 12, 2010, 1:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package persistence.tests;

import com.rameses.schema.SchemaSerializer;
import com.rameses.util.MapSerializer;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class TestSerializer implements SchemaSerializer {
    private MapSerializer serializer = new MapSerializer();
    
    public Object read(String s) {
        return null;
    }
    
    public String write(Object o) {
        if(!(o instanceof Map))
            throw new RuntimeException("SchemaMgmt error. write serialization must accept a Map object");
        return serializer.toString( (Map)o );
    }
    
    
    
    
}
