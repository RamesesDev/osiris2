
package com.rameses.calendar.control;

import com.rameses.calendar.common.DayModel;
import com.rameses.calendar.common.ScheduleModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Windhel
 */
public class XDaySchedulerComponent extends JScrollPane implements UIControl {
    
    private static int START_POS = 10;
    private static int BETWEEN_GAP = 30;
    private static int BETWEEN_GAP_SEPARATE = 50;
    private static int COLUMN_ITEM_GAP = 50;
    private int time_fill_size = 100;
    
    private int time_gap = 100;
    private boolean separate = false;
    private List<String> schedules = new ArrayList();
    
    private List<Color> colors = new ArrayList();
    private Plot panel = new Plot();
    
    private String time_start = "7:00";
    private String time_end = "12:00";
    private int time_interval = 60;
    private Point selectedPoint = new Point(0,0);
    
    private DayModel model;
    private List<ScheduleModel> shapes = new ArrayList();
    private String handler;
    
    private String[] depends;
    private int index;
    private Binding binding;
    
    public XDaySchedulerComponent() {
        setBackground(Color.WHITE);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  calculateTimeFrame ">
    public void calculateTimeFrame() {
        String[] str = time_start.split(":");
        String[] stre = time_end.split(":");
        int hrE = Integer.parseInt( stre[0] ) + 1;
        int minE = Integer.parseInt( stre[1] );
        int hour = Integer.parseInt( str[0] );
        int minute = Integer.parseInt( str[1] );
        StringBuffer sb = new StringBuffer();
        schedules.add("00:00");
        while(hour != hrE) {
            int min = Integer.toString( minute ).length();
            int hr = Integer.toString( hour ).length();
            if( hr == 1 && min == 1) {
                sb.append( "0" + hour + ":0" + minute );
            } else if( hr == 1) {
                sb.append( "0" + hour + ":" + minute );
            }else if( min == 1) {
                sb.append( hour + ":0" + minute );
            }else{
                sb.append( hour + ":" + minute );
            }
            
            schedules.add( sb.toString() );
            sb.delete(0, sb.length() );
            minute += time_interval;
            if(minute >= 60) {
                minute -= 60;
                hour++;
            }
        }
        //schedules.add("00:00");
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  Helper Methods ">
    public int calcPos(String str) {
        String[] x = str.split(":");
        String[] y = time_start.split(":");
        
        double mr = ( Double.parseDouble( x[1] ) / time_interval ) * (time_gap * 2);
        double hr = ( Double.parseDouble( x[0] ) - Double.parseDouble( y[0] )) * (time_gap * 2);
        double result = ( (mr + hr) + (START_POS*2) ) / 2;
        
        return (int) result;
    }
    
    private Color generateColor() {
        float r = Float.parseFloat( Math.random() + "" );
        float g = Float.parseFloat( Math.random() + "" );
        float b = Float.parseFloat( Math.random() + "" );
        
        if( separate )
            return new Color( r ,g ,b );
        else
            return new Color( r ,g ,b, 0.8f);
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  Setter/Getters Methods ">
    public boolean isSeparate() { return separate; }
    
    public void setSeparate(boolean separate) { this.separate = separate; }
    
    public String getTime_start() { return time_start; }
    
    public void setTime_start(String time_start) { this.time_start = time_start; }
    
    public String getTime_end() { return time_end; }
    
    public void setTime_end(String time_end) { this.time_end = time_end; }
    
    public int getTime_interval() { return time_interval; }
    
    public void setTime_interval(int time_interval) { this.time_interval = time_interval; }
    
    public String getHandler() { return handler; }
    
    public void setHandler(String handler) { this.handler = handler; }
    
    public int getTime_fill_size() { return time_fill_size; }

    public void setTime_fill_size(int time_fill_size) { this.time_fill_size = time_fill_size; }
    
    public void setDepends(String[] depends) { this.depends = depends;}
    
    public String[] getDepends() { return depends; }
    
    public void setIndex(int index) { this.index = index; }
    
    public int getIndex() { return index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    
    public Binding getBinding() { return binding; }
    
    public int compareTo(Object o) { return UIControlUtil.compare(this, o); }
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  Plot ( JPanel ) ">
    private class Plot extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x=START_POS;
            int y=START_POS;
            int u=START_POS;
            String[] str;
            int z;
            Point start;
            Dimension size;
            Graphics2D g2 = (Graphics2D) g;
            time_gap = ((int) (getHeight() / schedules.size()));
            
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            if( schedules.size() > 0 ) {
                for(int i=0 ; i<schedules.size() ; i++){
                    g2.setColor( Color.BLACK );
                    g2.drawString( schedules.get(i), x, y);
                    g2.setColor( Color.LIGHT_GRAY );
                    g2.drawLine( x, y, getWidth(), y);
                    y = y + time_gap;
                }
            }
            
            x = COLUMN_ITEM_GAP;
            y = START_POS;
            if( shapes.size() > 0 && schedules.size() > 0 ) {
                for(int i=0 ; i<shapes.size() ; i++) {
                    ScheduleModel sm = (ScheduleModel) shapes.get(i);
                    z = calcPos( (sm.getFromHr()+1)+":"+sm.getFromMin() );
                    
                    if( sm.getToDay() != sm.getFromDay() )
                        y = calcPos( "25:00" );
                    else
                        y = calcPos( (sm.getToHr()+1)+":"+sm.getToMin() );
                    
                    start = new Point( x, z );
                    size = new Dimension( getTime_fill_size(), y-z );
                    sm.init( start, size, colors.get(i) );
                    sm.supplyTimeData( schedules.size(), time_gap, time_start, time_end, start, size);
                    sm.setToLocation(g2);
                    
                    if( separate )
                        x = x + BETWEEN_GAP_SEPARATE;
                    else
                        x = x + BETWEEN_GAP;
                }
            }
            
            g2.dispose();
            g.dispose();
        }
    }
    //</editor-fold>
    
    public void refresh() {
    }
    
    public void load() {
        Object o = UIControlUtil.getBeanValue(this, handler);
        
        if( o instanceof DayModel )
            model = (DayModel) o;
        //populate model and shapes (List)
        model.init();
        shapes = model.getModelList();
        //add shapes (List) to panel ( Plot JPanel )
        for(int i=shapes.size()-1 ; i>=0 ; i--) {
            ScheduleModel sm = (ScheduleModel)shapes.get(i);
            panel.add( sm );
            sm.setParent( panel );
        }
        calculateTimeFrame(); //calculate and populate the time frame
        //generate the color(s) depending on the no. of shapes
        if( schedules.size() > 0 ) {
            for(int i=0 ; i<shapes.size() ; i++){
                colors.add( generateColor() );
            }
        }
        //set the dimension of the Plot ( JPanel ) depending the calculated time frame.
        panel.setPreferredSize( new Dimension( 300,time_gap*schedules.size() ));
        
        
        setViewportView( panel );
    }
}
