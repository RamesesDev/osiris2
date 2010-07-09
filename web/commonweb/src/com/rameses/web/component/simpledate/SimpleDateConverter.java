//version 1
package com.rameses.web.component.simpledate;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class SimpleDateConverter implements Converter {
    /*
     *  value of date
     *  2009-02-01 00:00:00;;true
     */
    public Object getAsObject(FacesContext ctx, UIComponent arg, String date) {
        
        UISimpleDate comp = (UISimpleDate) arg;
        String[] dateValues = date.split(";;");
        if(Boolean.parseBoolean(dateValues[1]))
            return new Date();
        String[] dates = dateValues[0].split("-");
        String strtime = dates[2].substring(dates[2].indexOf(" ")+1);
        String[] time = strtime.split(":");
        if(dates[0].equals("null") || dates[1].equals("null") || dates[2].equals("null")
        || time[0].equals("null") || time[1].equals("null") || time[2].equals("null")){
            return null;
        }
        Date dateVal;        
        try{         
            String d = dateValues[0];
            dateVal = java.sql.Timestamp.valueOf(d);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(!(formatter.format(dateVal).equals(d))){
                throw new Exception();
            }
            return dateVal;
        }catch(Exception e){
            FacesMessage msg = new FacesMessage();         
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Invalid Date");
            msg.setDetail("Invalid Date");
            throw new ConverterException(msg);
        }        
    }
    
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        return null;
    }
}
