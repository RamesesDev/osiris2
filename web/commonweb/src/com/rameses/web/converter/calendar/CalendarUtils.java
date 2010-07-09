
package com.rameses.web.converter.calendar;

import java.util.Calendar;

public class CalendarUtils {
    
    public static String[] days1 = {"S", "M", "T", "W", "T", "F", "S"};
    public static String[] days2 = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
    public static String[] days3 = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public static String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    
    public static String getMonthAsString(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "";
    }
    
    public static int getMaximumDay(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(calendar.YEAR);
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 1: {
                if (year % 400 == 0)
                    return 29;
                else if (year % 100 == 0)
                    return 28;
                else if (year % 4 == 0)
                    return 29;
                else
                    return 28;
            }  
        }
        return 0;
    }
    
    public static boolean isValidDate(int year, int month, int day){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        
        if (c.get(Calendar.YEAR) != year)
            return false;
        if (c.get(Calendar.MONTH) != month)
            return false;
        return true;
    }
}
