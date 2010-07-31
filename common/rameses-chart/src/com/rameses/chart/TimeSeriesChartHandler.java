/*
 * TimeSeriesChartHandler.java
 *
 * Created on June 2, 2009, 12:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.chart;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author elmo
 */
public abstract class TimeSeriesChartHandler extends AbstractChartHandler {
    
    private TimePeriod timePeriod = TimePeriod.d;
    
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
    
    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }
    
    
    public abstract List<TimeSeriesItem> getData();
    
    public JFreeChart createChart() {
        if( getMap()!=null) {
            super.setAttributes(getMap());
        }
        
        String title = getTitle();
        String xlabel = getXlabel();
        String ylabel = getYlabel();
        boolean legend = isIncludeLegend();
        boolean tooltips = isIncludeTooltips();
        boolean urls = isIncludeUrl();
        
        Class clz = timePeriod.getPeriodClass();
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for(TimeSeriesItem tsi: getData()) {
            TimeSeries series = dataset.getSeries(tsi.getSeries());
            if( series == null ) {
                series = new TimeSeries( tsi.getSeries(), clz );
                dataset.addSeries( series );
            }
            RegularTimePeriod p = RegularTimePeriod.createInstance( clz, tsi.getDate(), TimeZone.getDefault() );
            series.addOrUpdate( p, tsi.getValue() );
        }
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, xlabel, ylabel, dataset, legend, tooltips, urls );
        
        //do some optimizations here...
        
        return chart;
    }

    public void setAttributes(Map map) {
        super.setAttributes(map);
        String t = (String) map.get("timePeriod");    
        if(t!=null && t.trim().length()>0) {
            try {
                timePeriod = TimePeriod.valueOf(t);    
                if(timePeriod == null ) throw new Exception();
            }
            catch(Exception ex) {
                System.out.println("Time period " + t + " not supported.");
                timePeriod = TimePeriod.d;
            }
        }        
    }
    
    
    
}
