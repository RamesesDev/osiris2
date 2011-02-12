/*
 * DateUtil.java
 *
 * Created on June 2, 2010, 1:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {
    
    private static long MILLISECS_PER_SEC = 1000 ;
    private static long MILLISECS_PER_MIN =(60 * 1000);
    private static long MILLISECS_PER_HOUR = (60 * 60 * 1000);
    private static long MILLISECS_PER_DAY = (24 * 60 * 60 * 1000);
    
    private static String getType(String interval) {
        String type = "day";
        if( interval.contains("M")) type = "month";
        else if( interval.contains("h")) type = "hour";
        else if( interval.contains("m")) type = "minute";
        else if( interval.contains("s")) type = "second";
        else if( interval.contains("y")) type = "year";
        return type;
    }
    
    public static Date add( Date fromDate, String interval ) {
        if( interval == null ) return fromDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        String type = getType(interval);
        boolean neg = false;
        if(interval.trim().startsWith("-")) neg = true;
        
        int num = Integer.parseInt(interval.replaceAll("\\D", ""));
        if(neg) num = num * -1;
        
        if(type.equals("hour")) {
            cal.add(Calendar.HOUR, num );
        } else if(type.equals("minute")) {
            cal.add(Calendar.MINUTE, num );
        } else if(type.equals("second")) {
            cal.add(Calendar.SECOND, num );
        } else if( type.equals("month")) {
            cal.add(Calendar.MONTH, num);
        } else if( type.equals("year")) {
            cal.add(Calendar.YEAR, num);
        } else {
            cal.add(Calendar.DATE, num );
        }
        return cal.getTime();
    }
    
    public static Date calculateNextDate(Date d, String mode) {
        if( mode == null || mode.length()==0 )
            return null;
        
        int itype = Calendar.DATE;
        if( mode.contains("d") ) itype = Calendar.DATE;
        if( mode.contains("y") ) itype = Calendar.YEAR;
        if( mode.contains("w") ) itype = Calendar.WEEK_OF_YEAR;
        if( mode.contains("M") ) itype = Calendar.MONTH;
        if( mode.contains("m") ) itype = Calendar.MINUTE;
        if( mode.contains("h") ) itype = Calendar.HOUR;
        if( mode.contains("s") ) itype = Calendar.SECOND;
        String s = mode.replaceAll( "[^\\d]", "").trim();
        int i = Integer.parseInt(s);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime( d );
        cal.add( itype, i );
        return cal.getTime();
    }
    
    
    //Calendar.get(Calendar.ZONE_OFFSET) + Calendar.get(Calendar.DST_OFFSET)) / (60 * 1000)
    public static String getFormattedTime( Date d, String timezone ) {
        final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        TimeZone tz = TimeZone.getTimeZone(timezone);
        if(tz==null)
            throw new RuntimeException("Timezone " +timezone + " is not available");
        sdf.setTimeZone(tz);
        return sdf.format(d);
    }
    
    public static String getFormattedTime( Date d, String dtformat, String timezone ) {
        if(dtformat==null) dtformat = "yyyy-MM-dd HH:mm:ss";
        final String DATE_TIME_FORMAT = dtformat;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        TimeZone tz = TimeZone.getTimeZone(timezone);
        if(tz==null)
            throw new RuntimeException("Timezone " +timezone + " is not available");
        sdf.setTimeZone(tz);
        return sdf.format(d);
    }
    
    public static String convertTime(String time, String sourceTZ, String destTZ) {
        final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        if (sourceTZ != null)
            sdf.setTimeZone(TimeZone.getTimeZone(sourceTZ));
        else
            sdf.setTimeZone(TimeZone.getDefault()); // default to server's timezone
        Date specifiedTime;
        try {
            specifiedTime = sdf.parse(time);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        // switch timezone
        if (destTZ != null)
            sdf.setTimeZone(TimeZone.getTimeZone(destTZ));
        else
            sdf.setTimeZone(TimeZone.getDefault()); // default to server's timezone
        return sdf.format(specifiedTime);
    }
    
    public static long diff(Date d1, Date d2) {
        return diff(d1,d2,Calendar.DATE);
    }
    
    public static  long diff(Date dfrom, Date dto, int type) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(dfrom);
        calendar2.setTime(dto);
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;
        if( type == Calendar.DATE ) {
            return diff / (24 * 60 * 60 * 1000);
        }
        else if( type == Calendar.HOUR) {
            return diff / (60 * 60 * 1000);
        }
        else if( type == Calendar.MINUTE) {
            return diff / (60 * 1000);
        }
        else if( type == Calendar.SECOND ) {
            return diff / 1000;
        }
        else {
            return diff;
        }
    }
    
}
