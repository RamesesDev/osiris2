/*
 * BeanResolver.java
 *
 * Created on August 12, 2010, 1:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.schema;

import com.rameses.common.PropertyResolver;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author elmo
 */
public class BeanResolver implements PropertyResolver {
    
    /** Creates a new instance of BeanResolver */
    public BeanResolver() {
    }
    
    public void setProperty(Object bean, String propertyName, Object value) {
        try {
            PropertyUtils.setNestedProperty( bean, propertyName, value );
        } catch(Exception e){;}
    }
    
    public Class getPropertyType(Object bean, String propertyName) {
        try {
            Object o = PropertyUtils.getNestedProperty( bean, propertyName );
            if(o!=null) return o.getClass();
            return PropertyUtils.getPropertyType(bean, propertyName);
        } catch(Exception e) {
            //System.out.println("error " + e.getMessage());
            return null;
        }
    }
    
    public Object getProperty(Object bean, String propertyName) {
        try {
            return PropertyUtils.getNestedProperty( bean, propertyName );
        } catch(Exception e) {
            return null;
        }
    }
    
}
