/*
 * TimePeriod.java
 *
 * Created on June 2, 2009, 12:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.chart;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;

/**
 *
 * @author elmo
 */
public enum TimePeriod {

    ms(Millisecond.class),
    s(Second.class),
    m(Minute.class),
    h(Hour.class),
    d(Day.class),
    w(Week.class),
    M(Month.class),
    y(Year.class);    
    
    private Class periodClass;
    
    TimePeriod( Class t ) {
        periodClass = t;
    }
    
    public Class getPeriodClass() {
        return periodClass;
    }
    
}
