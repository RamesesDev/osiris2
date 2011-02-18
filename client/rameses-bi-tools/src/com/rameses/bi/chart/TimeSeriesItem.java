/*
 * TimeValue.java
 *
 * Created on December 15, 2008, 11:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.bi.chart;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author rameses
 */
public class TimeSeriesItem implements Comparable{
    
    /** Creates a new instance of TimeValue */
    private Date date;
    private Number value;
    private String series;
    
    public TimeSeriesItem(String series, Date time,  Number value){
        this.series = series;
        this.value = value;
        this.date = time;
    }
    
    public TimeSeriesItem(Map map){
        this.series = (String)map.get("series");
        this.value = (Number)map.get("value");
        Object d = map.get("date");
        if( d instanceof Date ) {
            this.date = (Date)d;
        }
        else {
            this.date = convertDate( (String)d );
        }
    }

    private Date convertDate( String d ) {
        String timeStamp1 = "\\d{4}-\\d{2}-\\d{2}\\s*?\\d{2}:\\d{2}:\\d{2}";
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        try {
            if( d.matches(timeStamp1) ) {
                return java.sql.Timestamp.valueOf(d);
            }
            else if( d.matches(datePattern) ) {
                return java.sql.Date.valueOf(d);
            }
            else
                return java.sql.Date.valueOf(d);
        }
        catch(Exception ign) {
            System.out.println("TimeSeries. Unparseable date format->" + d);
            return null;
        }
    }    
    
    public TimeSeriesItem(String series, String d,  Number value) {
        this.series = series;
        this.value = value;
        this.date = convertDate( d );
    }
    
    public TimeSeriesItem() {
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Number getValue() {
        return value;
    }
    
    public void setValue(Number value) {
        this.value = value;
    }
    
    public String getSeries() {
        return series;
    }
    
    public void setSeries(String series) {
        this.series = series;
    }

    public boolean equals(Object obj) {
        if( obj == null || !(obj instanceof TimeSeriesItem))
            return false;
        TimeSeriesItem tsv = (TimeSeriesItem) obj;
        if (series == null && tsv.getSeries() != null)
            return false;
        if (!(series.equals(tsv.getSeries())))
            return false;
        if (!(date.equals(tsv.getDate())))
            return false;
        return true;
    }
    
    public int compareTo(Object o) {
        return this.series.compareToIgnoreCase(((TimeSeriesItem)o).getSeries());
    }
    
}
