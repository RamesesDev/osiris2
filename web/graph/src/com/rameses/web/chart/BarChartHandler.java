
package com.rameses.web.chart;

import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public abstract class BarChartHandler extends AbstractChartHandler {

    public abstract List<ChartItem> getData();
    
    public JFreeChart createChart() {
        if( getMap()!=null) {
            super.setAttributes(getMap());
        }
        
        
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        String title = getTitle();
        String xlabel = getXlabel();
        String ylabel = getYlabel();
        boolean legend = isIncludeLegend();
        boolean tooltips = isIncludeTooltips();
        boolean urls = isIncludeUrl();

        //build the data set
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(ChartItem item : getData() ) {
            dataset.addValue(item.getValue(), item.getRowkey(), item.getColkey());
        }
        
        JFreeChart chart = null;        
        if( !getIs3D() ){
            chart  = ChartFactory.createBarChart( title, xlabel, ylabel, dataset, orientation, legend, tooltips, urls ); 
        }
        else{
            chart  = ChartFactory.createBarChart3D(title,xlabel,ylabel,dataset,orientation, legend, tooltips, urls );
        }

        final CategoryPlot plot = chart.getCategoryPlot();
        final CategoryAxis axis = plot.getDomainAxis();
        axis.setCategoryLabelPositions( CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0));
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        return chart;
        
        /*
         * // set the background color for the chart...
        chart.setBackgroundPaint(new Color(0xBBBBDD));

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        
        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.10);
        
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.lightGray
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        return chart;        
         */
        
        
    }
}
