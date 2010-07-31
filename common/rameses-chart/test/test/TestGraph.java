/*
 * TestGraph.java
 * JUnit based test
 *
 * Created on January 15, 2009, 11:44 AM
 */

package test;

import javax.swing.JDialog;
import junit.framework.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author Administrator
 */
public class TestGraph extends TestCase {
    
    public TestGraph(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testTimeSeries() {
        TimeSeries series = new TimeSeries("series1", Minute.class);
        RegularTimePeriod rtp = new Second(01, 01, 01, 13, 01, 2009);
        series.addOrUpdate(rtp, new Double(1.2));
        rtp = new Second(04, 01, 01, 13, 01, 2009);
        series.addOrUpdate(rtp, new Double(1.2));
        rtp = new Second(03, 01, 01, 13, 01, 2009);
        series.addOrUpdate(rtp, new Double(4.1));        
        rtp = new Second(02, 01, 01, 13, 01, 2009);
        series.addOrUpdate(rtp, new Double(3.2));
        rtp = new Second(05, 01, 01, 13, 01, 2009);
        series.addOrUpdate(rtp, new Double(3.1));
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        tsc.addSeries(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart( "Test1", "Time", "Amount", tsc, true, false, false  );
        JDialog jd = new JDialog();
        jd.setModal( true );
        ChartPanel chartPanel = new ChartPanel(chart);
        jd.setContentPane(chartPanel);
        jd.setSize(500,300);
        jd.setVisible(true);
    }
    
    public void testX() {
//        String t = "\\d{4}-\\d{2}-\\d{2}\\s*?\\d{2}:\\d{2}:\\d{2}";
//        System.out.println("2009-01-01".matches(t));
//        System.out.println("2009-01-01     00:00:00".matches(t));
//        
//        
//        String v = "2009 - 01 - 0 1 22: 00 : 00";
//        System.out.println(v.replaceAll("\\s", ""));
    }
    
    
    
}
