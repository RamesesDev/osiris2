
package com.rameses.web.converter.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class CalendarDateConverter implements Converter {
    
    public Object getAsObject(FacesContext ctx, UIComponent comp, String date) {
        String pattern = (String) comp.getAttributes().get("pattern");
        if(pattern == null)
            pattern = "MM/dd/yyyy";
        
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public String getAsString(FacesContext ctx, UIComponent comp, Object date) {
        String pattern = (String) comp.getAttributes().get("pattern");
        if(pattern == null)
            pattern = "MM/dd/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        if(date == null)
            return null;
        return formatter.format(date);
    }
}
