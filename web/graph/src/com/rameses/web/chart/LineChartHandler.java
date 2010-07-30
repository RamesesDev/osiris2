package com.rameses.web.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public abstract class LineChartHandler extends AbstractChartHandler {
    
    public abstract List<ChartItem> getData();
    
    public JFreeChart createChart() {
        
        String title = getTitle();
        String xlabel = getXlabel();
        String ylabel = getYlabel();
        boolean legend = isIncludeLegend();
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean tooltips = isIncludeTooltips();
        boolean urls = isIncludeUrl();

        //build the data set
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(ChartItem item : getData() ) {
            dataset.addValue(item.getValue(), item.getRowkey(), item.getColkey());
        }    
        JFreeChart chart = ChartFactory.createLineChart( title, xlabel, ylabel, dataset, orientation, legend, tooltips, urls );
        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.WHITE);
        
        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        
        
        // customise the renderer...
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        
        renderer.setSeriesStroke(
            0, new BasicStroke(
            2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            1.0f, new float[]{10.0f, 6.0f}, 0.0f));
        
        renderer.setSeriesStroke(
            1, new BasicStroke(
            2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            1.0f, new float[]{6.0f, 6.0f}, 0.0f));
        
        renderer.setSeriesStroke(
            2, new BasicStroke(
            2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            1.0f, new float[]{2.0f, 6.0f}, 0.0f));
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
    }
    
}
