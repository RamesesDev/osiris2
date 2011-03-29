/*
 * DateUtil.java
 *
 * Created on June 2, 2010, 1:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        //returns integer position in list
        private int findDayInList(String m, String[] days ) {
            for(int i=0; i<days.length;i++) {
                if( days[i].equals(m)) return i;
            }
            return -1;
        }
        
        
        //pattern passed is ##:##-##:## MWF
        private void parse(String pattern) {
            if(!pattern.matches("\\d\\d:\\d\\d-\\d\\d:\\d\\d (M|T|W|Th|F|S|Su|-).*"))
                throw new RuntimeException("Pattern is incorrect. Must follow for example  ##:##-##:## MWF");
            fromTime = pattern.substring(0, pattern.indexOf("-"));
            toTime = pattern.substring(pattern.indexOf("-")+1, pattern.indexOf(" "));
            String days = pattern.substring( pattern.indexOf(" ")+1 );
            
            //handle also a days range for example M-F
            if( days.contains("-")) {
                String[] arr = days.split("-");
                String max = arr[1];
                String[] list = new String[]{"M","T","W","Th","F","S","Su"};
                int imin = findDayInList(arr[0], list);
                int imax = findDayInList(arr[1], list);
                StringBuffer sb = new StringBuffer();
                for(int i=imin; i<=imax; i++) {
                    sb.append( list[i]);
                }
                days = sb.toString();
            }
                    
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

    public static interface ParseHandler {
        void handle( String dow, Timestamp from, Timestamp to );
    } 
    
    public static class MapListParseHandler implements ParseHandler {
        private List<Map> list = new ArrayList();
        public void handle( String dow, Timestamp from, Timestamp to ){
            Map m = new HashMap();
            m.put("day", dow);
            m.put( "timefrom", from);
            m.put("timeto", to);
            list.add(m);
        }
        public List<Map> getList() {
            return list;
        }
    } 
    
    /**
     * User passes a ParseHandler interface  
     * where day is any of ff: M, T, W, Th, F, S, Su
     * timein and timeout is a timestampvalue. 
     * If sample day is null, it assumes 1970-01-01
     */
    public static void parseToDay( String pattern, ParseHandler handler ) {
        TimeUtil.ScheduleBean bean = new TimeUtil.ScheduleBean(pattern);
        String dummyDate = "0000-00-00";
        Timestamp start = Timestamp.valueOf(dummyDate + " " + bean.getFromTime() + ":00");
        Timestamp end = Timestamp.valueOf(dummyDate + " " +  bean.getToTime() + ":00");
        if( bean.isMon() ) handler.handle("M",start,end);
        if( bean.isTue() ) handler.handle("T",start,end);
        if( bean.isWed() ) handler.handle("W",start,end);
        if( bean.isThu() ) handler.handle("Th",start,end);
        if( bean.isFri() ) handler.handle("F",start,end);
        if( bean.isSat() ) handler.handle("S",start,end);
        if( bean.isSun() ) handler.handle("Su",start,end);
    }
    
    public static List<Map> parseToDaylistMap(String pattern) {
        MapListParseHandler h = new MapListParseHandler();
        parseToDay(pattern, h);
        return h.getList();
    }
    
    
}
