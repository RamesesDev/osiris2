/*
 * TaskUtil.java
 *
 * Created on January 4, 2009, 6:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.system.task;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author elmo
 */
public final class TaskUtil {
    
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
}
