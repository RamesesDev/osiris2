
package com.rameses.chart;

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
        if( !isDisplay3d() ){
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
    }
}
