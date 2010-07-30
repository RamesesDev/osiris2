/*
 * TestGraph2.java
 * JUnit based test
 *
 * Created on January 15, 2009, 1:17 PM
 */

package test;

import junit.framework.*;

/**
 *
 * @author Administrator
 */
public class TestGraph2 extends TestCase {
    
    public TestGraph2(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    /*
    List<TimeSeriesItem> list = new ArrayList<TimeSeriesItem>();
    JDialog jd = new JDialog();
    TimeSeriesChart chart;
    JButton button = new JButton("Add Chart");
    TimeSeriesItem value;
    Calendar cal = Calendar.getInstance();
    public void testGraph() {
        
        cal.setTime(java.sql.Timestamp.valueOf("2009-01-15 00:00:00"));
        for(int i = 0; i < 3; i++){
            cal.add(Calendar.SECOND, 1);
            value = new TimeSeriesItem();
            value.setDate(cal.getTime());
            value.setSeries("series");
            value.setValue(null);
            list.add(value);
        }
        cal.setTime(java.sql.Timestamp.valueOf("2009-01-15 00:00:00"));
        displayChart();
        jd.setModal( true );
        jd.setLayout(new BorderLayout());
        jd.add(button, BorderLayout.NORTH);
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for(int i = 0; i < 3; i++){
                    cal.add(Calendar.SECOND, 1);
                    value = new TimeSeriesItem();
                    value.setDate(cal.getTime());
                    value.setSeries("series");
                    value.setValue(null);
                    addOrUpdate(value);
                }
                displayChart();
            }
        });
        //jd.setContentPane(chart.);
        jd.setSize(500,300);
        jd.setVisible(true);
    }
    
    private void addOrUpdate(TimeSeriesItem value){
        int idx = list.indexOf(value);
        if (idx != -1){
            list.set(idx, value);
        }else{
            list.add(value);
        }
    }
    
    private void displayChart(){
        chart = new TimeSeriesChart(list, 900, 500, "Chart 1", "xvalue", "yvalue", "Minute");
        chart.getChart().getPlot().setBackgroundPaint(Color.BLACK);
        chart.getChart().getXYPlot().setRangeGridlinePaint(Color.GREEN);
        jd.setContentPane(chart.getContentPane());
        SwingUtilities.updateComponentTreeUI(jd);
    }
    */
    
}
