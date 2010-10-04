/*
 * BasicAggregatorHandler.java
 *
 * Created on September 3, 2010, 8:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * this is basic aggerator opearations of sun,count,min and max
 */
public class BasicAggregatorHandler {
    
    /**
     * count always returns an incrementing field.
     */
    private final static String AGGTYPE_COUNT = "count";
    private final static String AGGTYPE_MAX = "max";
    private final static String AGGTYPE_MIN = "min";
    private final static String AGGTYPE_SUM = "sum";
    private final static String AGGTYPE_CONCAT = "concat";
    
    
    /** Creates a new instance of BasicAggregatorHandler */
    public BasicAggregatorHandler() {
    }
    
    public boolean proceed(Object oldModel, Object newModel) {
        return true;
    }
    
    public Object compare(String aggtype, Class type, Object oldValue, Object newValue, Map properties) {
        Object retValue = oldValue;
        if(aggtype != null && !aggtype.equalsIgnoreCase(AGGTYPE_CONCAT)) {
            BigDecimal _old = new BigDecimal("0");
            BigDecimal _new = new BigDecimal("0");
            if(oldValue!=null) _old = new BigDecimal( oldValue + "");
            if(newValue!=null) _new = new BigDecimal( newValue + "");
            
            if(aggtype.equalsIgnoreCase(AGGTYPE_SUM)) {
                _old = _old.add( _new );
            } else if(aggtype.equalsIgnoreCase(AGGTYPE_MAX)) {
                if( _old.compareTo( _new) < 0 ) {
                    _old = _new;
                }
            } else if(aggtype.equalsIgnoreCase(AGGTYPE_MIN)) {
                if( _old.compareTo( _new) > 0 ) {
                    _old = _new;
                }
            } else if(aggtype.equalsIgnoreCase(AGGTYPE_COUNT)) {
                if( _old.equals(new BigDecimal("0"))) {
                    _old = new BigDecimal("1");
                }
                _old = _old.add(new BigDecimal("1"));
            }
            
            if( type == Integer.class ) {
                retValue = new Integer( _old.intValue() );
            } else if( type == int.class ) {
                retValue = _old.intValue();
            } else if( type == Double.class ) {
                retValue = new Double( _old.doubleValue() );
            } else if( type == double.class ) {
                retValue = _old.doubleValue();
            } else if( type == Long.class ) {
                retValue = new Long(_old.longValue());
            } else if( type == long.class ) {
                retValue = _old.longValue();
            } else if( type == Float.class ) {
                retValue = new Float(_old.floatValue());
            } else if( type == float.class ) {
                retValue = _old.floatValue();
            } else {
                retValue = _old;
            }
            return retValue;
        } else if(aggtype != null && aggtype.equalsIgnoreCase(AGGTYPE_CONCAT)) {
            String delimiter = (String)properties.get("delimiter");
            
            StringBuffer sb = new StringBuffer();
            if(oldValue!=null) sb.append( (String)oldValue);
            if(delimiter!=null) sb.append(delimiter);
            if( newValue!=null )sb.append((String)newValue );
            retValue = sb.toString();
            return retValue;
        } else {
            return newValue;
        }
    }
    
    
    
}
