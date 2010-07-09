
package com.rameses.osiris2.reports;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ValueResolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ReportDataSource implements JRDataSource 
{
    protected Iterator iterator;
    private Object source;
    protected Object currentObject;
    private ValueResolver valueResolver;

    public ReportDataSource(Object source) {
        setSource(source);
        valueResolver = ClientContext.getCurrentContext().getValueResolver();        
    }
    
    public void setSource(Object src) {
        this.source = src;
        if( src == null ) {
            iterator = (new ArrayList()).iterator();
        }
        else if( src instanceof Collection ) {
            iterator = ((Collection)src).iterator();
        }
        else {
            List l = new ArrayList();
            l.add( src );
            iterator = l.iterator();
        }
    }

    public boolean next() throws JRException {
        if( iterator.hasNext() ) {
            currentObject = iterator.next();
            return true;
        }
        else {
            return false;
        }
    }
    
    
    
    public Object getFieldValue(JRField jRField) throws JRException {
        Object field = null;
        String fieldName = null;
        try {
            fieldName = jRField.getName();
            //field = PropertyUtils.getNestedProperty( currentObject, fieldName );
            field = valueResolver.getValue(currentObject, fieldName);
            
            if( jRField.getValueClass().isAssignableFrom( Collection.class) ) {
                return new ReportDataSource( field );
            }
            else {
                return field;
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error on field [" + fieldName  + "] caused by " + ex.getMessage());
            return null;
        }
    }    
}
