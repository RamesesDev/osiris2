package com.rameses.bi.chart;

import java.awt.Font;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

public abstract class PieChartHandler extends AbstractChartHandler {
    
    public abstract List<PieChartItem> getData();
    
    
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
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        for(PieChartItem item : getData()) {
            dataset.setValue(item.getCategory(), item.getValue());
        }
        
        JFreeChart chart;
        if(!isDisplay3d()){
            chart = ChartFactory.createPieChart(title, dataset,legend, tooltips, urls );
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setNoDataMessage("No data available");
            plot.setCircular(false);
            plot.setLabelGap(0.02);
        }else{
            chart = ChartFactory.createPieChart3D(title, dataset,legend, tooltips, urls );
            PiePlot3D plot = (PiePlot3D) chart.getPlot();
            plot.setStartAngle(290);
            plot.setDirection(Rotation.CLOCKWISE);
            plot.setForegroundAlpha(0.5f);
            plot.setNoDataMessage("No data to display");
        }
        return chart;

    }
    
}
