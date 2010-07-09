package com.rameses.reports.jasper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.apache.commons.beanutils.PropertyUtils;


public class ReportDataSource implements JRDataSource {
    
    protected Iterator iterator;
    private Object source;
    protected Object currentObject;
    

    public ReportDataSource(Object source) {
        setSource(source);
    }
    
    public void setSource(Object src) {
        this.source = src;
        if( src instanceof Collection ) {
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
            field = PropertyUtils.getNestedProperty( currentObject, fieldName );
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
