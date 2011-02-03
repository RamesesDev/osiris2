
package com.rameses.calendar.common;

import java.util.Calendar;
import java.util.Date;

/**
 * 20101210
 * @author Windhel
 */
public class DateModel {
    
    private Calendar calendar;
    private String text;
    private boolean included = true;
    private static String[] DAY_OF_WEEK = {"Sunday" , "Monday" , "Tuesday" , "Wednesday" ,
    "Thursday" , "Friday" , "Saturday" };
    private static String[] MONTHS = {
        "January" , "February" , "March" , "April" , "May" ,
        "June", "July" , "August" , "September", "October" , "November" , "December"
    };
    
    public DateModel() {
        calendar = Calendar.getInstance();
    }
    
    public void init(Date date) {
        calendar.setTime(date);
    }
    
    public Date getDate() {
        return calendar.getTime();
    }
    
    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }
    
    public String getMonthName() {
        return MONTHS[ calendar.get(Calendar.MONTH) ];
    }
    
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }
    
    public String getFirstDayOfMonth() {
        return DAY_OF_WEEK[ calendar.get(Calendar.DAY_OF_WEEK) ];
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }
    
    public boolean equals(Object obj) {
        if(! (obj instanceof DateModel) ) return false;
        
        DateModel dm = (DateModel)obj;
        return this.getDay() == dm.getDay() && this.getMonth() == dm.getMonth() && this.getYear() == dm.getYear();
    }
    
    public void reset() {
        setText("");
    }
}
