/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.chart;

/**
 * A demo showing a daily and intraday segmented high-low-open-close charts.
 *
 * @author Bill Kelemen
 */
public class SegmentedHighLowChart {

    /**
     * A demonstration application showing a high-low-open-close chart using a
     * segmented or non-segmented axis.
     *
     * @param title  the frame title.
     * @param useSegmentedAxis use a segmented axis for this demo?
     * @param timelineType Type of timeline to use: 1=Monday through Friday, 2=Intraday
     */
    /*
    private JFreeChart chart;
    private List<OHLCItem> items = new ArrayList<OHLCItem>();
    public SegmentedHighLowChart(final String title,
            final boolean useSegmentedAxis,
            final int timelineType, List data) {

        super(title);

        System.out.println("\nMaking SegmentedHighLowChartDemo(" + title + ")");

        // create a Calendar object with today's date at midnight
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // create a timeline for the demo
        SegmentedTimeline timeline = null;
        switch (timelineType) {
            case 1:
                timeline = SegmentedTimeline.newMondayThroughFridayTimeline();
                break;
            case 2:
                timeline = SegmentedTimeline.newFifteenMinuteTimeline();

                final Calendar cal2 = (Calendar) cal.clone();
                cal2.add(Calendar.YEAR, 1);

                // add 1 year of baseTimeline's excluded segments (Saturdays and Sundays) as
                // exceptions of the intraday timeline
                timeline.addBaseTimelineExclusions(cal.getTime().getTime(),
                cal2.getTime().getTime());
                break;

            default:
                System.out.println("Invalid timelineType.");
                System.exit(1);
        }


        // create a data set that has data for trading days (Monday through Friday).
        items.addAll(data);
        
        final DefaultHighLowDataset dataset = createSegmentedHighLowDataset(timeline, cal.getTime(), items);

        if (useSegmentedAxis) {
            chart = ChartFactory.createHighLowChart(
                    title,
                    "Time", "Value",
                    dataset, timeline, true);
        } else {
            chart = ChartFactory.createHighLowChart(
                    title,
                    "Time", "Value",
                    dataset, true);
        }

        final DateAxis axis = (DateAxis) chart.getXYPlot().getDomainAxis();
        axis.setAutoRange(true);
        final TickUnits units = new TickUnits();
        units.add(new DateTickUnit(DateTickUnit.DAY, 1, DateTickUnit.HOUR, 1,
                new SimpleDateFormat("d-MMM")));
        units.add(new DateTickUnit(DateTickUnit.DAY, 2, DateTickUnit.HOUR, 1,
                new SimpleDateFormat("d-MMM")));
        units.add(new DateTickUnit(DateTickUnit.DAY, 7, DateTickUnit.DAY, 1,
                new SimpleDateFormat("d-MMM")));
        units.add(new DateTickUnit(DateTickUnit.DAY, 15, DateTickUnit.DAY, 1,
                new SimpleDateFormat("d-MMM")));
        units.add(new DateTickUnit(DateTickUnit.DAY, 30, DateTickUnit.DAY, 1,
                new SimpleDateFormat("d-MMM")));
        axis.setStandardTickUnits(units);

        final NumberAxis vaxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        vaxis.setAutoRangeIncludesZero(false);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        setContentPane(chartPanel);

    }

    public DefaultHighLowDataset createSegmentedHighLowDataset(
            final SegmentedTimeline timeline,
            final Date start, final List<OHLCItem> data) {

        // some open-high-low-close data


        final int m = data.size();

        final Date[] date = new Date[m];
        final double[] high = new double[m];
        final double[] low = new double[m];
        final double[] open = new double[m];
        final double[] close = new double[m];
        final double[] volume = new double[m];

        final SegmentedTimeline.Segment segment = timeline.getSegment(start);
        int i = 0;
        for (OHLCItem item : data) {
            while (!segment.inIncludeSegments()) {
                segment.inc();
            }
            //date[i] = segment.getDate();
            date[i] = item.getDate();
            open[i] = item.getOpen();
            high[i] = item.getHigh();
            low[i] = item.getLow();
            close[i] = item.getClose();
            segment.inc();
            i++;
        }

        return new DefaultHighLowDataset("Series 1", date, high, low, open, close, volume);

    }

    public JFreeChart getChart() {
        return chart;
    }
     */

// ****************************************************************************
// * COMMERCIAL SUPPORT / JFREECHART DEVELOPER GUIDE                          *
// * Please note that commercial support and documentation is available from: *
// *                                                                          *
// * http://www.object-refinery.com/jfreechart/support.html                   *
// *                                                                          *
// * This is not only a great service for developers, but is a VERY IMPORTANT *
// * source of funding for the JFreeChart project.  Please support us so that *
// * we can continue developing free software.                                *
// ****************************************************************************
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
//    public static void main(final String[] args) {
//
//        final ApplicationFrame[][] frame = new ApplicationFrame[2][2];
//        frame[0][0] = new SegmentedHighLowChart("Segmented Daily High-Low-Open-Close Demo-1",
//                true, 1, null);
//        frame[1][0] = new SegmentedHighLowChart("Normal Daily High-Low-Open-Close Demo-1",
//                false, 1, null);
//        frame[0][1] = new SegmentedHighLowChart("Segmented Intraday High-Low-Open-Close Demo",
//                true, 2, null);
//        frame[1][1] = new SegmentedHighLowChart("Normal Intraday High-Low-Open-Close Demo",
//                false, 2, null);
//        for (int i = 0; i <
//                2; i++) {
//            for (int j = 0; j <
//                    2; j++) {
//                frame[i][j].pack();
//                RefineryUtilities.positionFrameOnScreen(frame[i][j], .15 + .70 * j, .25 + .50 * i);
//                frame[i][j].setVisible(true);
//            }
//        }
//
//    }
    
}
