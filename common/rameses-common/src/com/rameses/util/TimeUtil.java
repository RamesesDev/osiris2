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

public final class TimeUtil {
    
    
    /***
     * this procedure checks if the date is within the time specified.
     * sample timePattern: 08:00-12:00 MWF
     */
    private static SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    public static boolean checkSchedule(Date d, String timePattern) {
        try {
            ScheduleBean p = new ScheduleBean( timePattern );
            Calendar cal = Calendar.getInstance();
            cal.setTime( d );
            int dow = cal.get( Calendar.DAY_OF_WEEK );
            if(dow == Calendar.MONDAY  && !p.isMon()) return false;
            else if(dow == Calendar.TUESDAY  && !p.isTue()) return false;
            else if(dow == Calendar.WEDNESDAY  && !p.isWed()) return false;
            else if(dow == Calendar.THURSDAY  && !p.isThu()) return false;
            else if(dow == Calendar.FRIDAY  && !p.isFri()) return false;
            else if(dow == Calendar.SATURDAY  && !p.isSat()) return false;
            else if(dow == Calendar.SUNDAY  && !p.isSun()) return false;
            
            String strDate = formatDay.format( d );
            
            //check time if within.
            Date dfrom = dformat.parse( strDate + " " + p.getFromTime() );
            Date dto = dformat.parse( strDate + " " + p.getToTime() );
            if( d.before(dfrom)) return false;
            if( d.after(dto )) return false;
            return true;
            
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public static class ScheduleBean {
        private String fromTime;
        private String toTime;
        private boolean mon;
        private boolean tue;
        private boolean wed;
        private boolean thu;
        private boolean fri;
        private boolean sat;
        private boolean sun;
        
        public ScheduleBean() {}
        
        public ScheduleBean(String pattern) {
            parse(pattern);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append( fromTime );
            sb.append( "-");
            sb.append(toTime);
            sb.append( " ");
            if(mon) sb.append("M");
            if(tue) sb.append("T");
            if(wed) sb.append("W");
            if(thu) sb.append("Th");
            if(fri) sb.append("F");
            if(sat) sb.append("S");
            if(sun) sb.append("Su");
            return sb.toString();
        }
        
        //pattern passed is ##:##-##:## MWF
        private void parse(String pattern) {
            fromTime = pattern.substring(0, pattern.indexOf("-"));
            toTime = pattern.substring(pattern.indexOf("-")+1, pattern.indexOf(" "));
            String days = pattern.substring( pattern.indexOf(" ")+1 );
            if(days.contains("M")) mon = true;
            //uses a negative lookahead to negate Th
            if(days.matches(".*T(?!h).*")) tue = true;
            if(days.contains("W")) wed = true;
            if(days.contains("Th")) thu = true;
            if(days.contains("F")) fri = true;
            if(days.matches(".*S(?!u).*")) sat = true;
            if(days.contains("Su")) sun = true;
        }
        
        public String getFromTime() {
            return fromTime;
        }
        
        public String getToTime() {
            return toTime;
        }
        
        public boolean isMon() {
            return mon;
        }
        
        public boolean isTue() {
            return tue;
        }
        
        public boolean isWed() {
            return wed;
        }
        
        public boolean isThu() {
            return thu;
        }
        
        public boolean isFri() {
            return fri;
        }
        
        public boolean isSat() {
            return sat;
        }
        
        public boolean isSun() {
            return sun;
        }

        public void setFromTime(String fromTime) {
            this.fromTime = fromTime;
        }

        public void setToTime(String toTime) {
            this.toTime = toTime;
        }

        public void setMon(boolean mon) {
            this.mon = mon;
        }

        public void setTue(boolean tue) {
            this.tue = tue;
        }

        public void setWed(boolean wed) {
            this.wed = wed;
        }

        public void setThu(boolean thu) {
            this.thu = thu;
        }

        public void setFri(boolean fri) {
            this.fri = fri;
        }

        public void setSat(boolean sat) {
            this.sat = sat;
        }

        public void setSun(boolean sun) {
            this.sun = sun;
        }
        
    }
    
}
