/*
 * XDaySchedulerComponent.java
 *
 * Created on December 14, 2010, 12:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import com.rameses.calendar.common.DayModel;
import com.rameses.calendar.control.*;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 20101214
 * @author Windhel
 */
public class XDaySchedulerComponent extends JPanel implements UIControl{
    
    private static int FILL_SIZE = 150;
    private static int TIME_GAP = 100;
    private static int START_POS = 10;
    private static int BETWEEN_GAP = 20;
    
    private String[] depends;
    private int index;
    private Binding binding;
    
    private DayModel dsm;
    private String handler;
    private String time_start = "7:00";
    private String time_end = "12:00";
    private int time_interval = 60;
    private Point selectedPoint = new Point(0,0);
    
    //dummy datas
    private List<String> schedules = new ArrayList();
    private List<String> descriptions = new ArrayList();
    private List<String> titles = new ArrayList();
    //end dummy datas
    
    private List<String> items = new ArrayList();
    private List<Color> colors = new ArrayList();
    private List<ScheduleObject> shapes = new ArrayList();
    private ScheduleObject selectedPoly = new ScheduleObject();
    
    public XDaySchedulerComponent() {
        String[] str = time_start.split(":");
        String[] stre = getEndTime().split(":");
        int hrE = Integer.parseInt( stre[0] );
        int minE = Integer.parseInt( stre[1] );
        int hour = Integer.parseInt( str[0] );
        int minute = Integer.parseInt( str[1] );
        StringBuffer sb = new StringBuffer();
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
        
        //DUMMY DATAS
        items.add("7:00-9:30");
        items.add("8:00-10:30");
        items.add("9:30-10:30");
        descriptions.add("<html>listen <br>to <br>mozart</html>");
        descriptions.add("clean the living room");
        descriptions.add("cook for lunch");
        titles.add("task 1");
        titles.add("task 2");
        titles.add("task 3");
        
        setBackground(Color.WHITE);
        addMouseListener( new MouseSupport());
        
        if( schedules.size() > 0 ) {
            for(int i=0 ; i<schedules.size() ; i++){
                colors.add( generateColor() );
                shapes.add( new ScheduleObject() );
                
                add( shapes.get(i) );
            }
        }
        
        //dummy
        for(int i=0 ; i<items.size() ; i++) {
            shapes.get(i).setName( items.get(i) );
            shapes.get(i).setTitle( titles.get(i) );
            shapes.get(i).setDescription( descriptions.get(i));
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x=START_POS;
        int y=START_POS;
        int u=START_POS;
        String[] str;
        int z = 0;
        Point start;
        Dimension size;
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        if( schedules.size() > 0 ) {
            for(int i=0 ; i<schedules.size() ; i++){
                g2.setColor( Color.BLACK );
                g2.drawString( schedules.get(i), x, y);
                g2.setColor( Color.LIGHT_GRAY );
                g2.drawLine( x, y, 250, y);
                y = y + TIME_GAP;
                colors.add( generateColor() );
            }
        }
        
        x = x + TIME_GAP;
        y = START_POS;
        
        if( items.size() > 0 && schedules.size() > 0 ) {
            for(int i=0 ; i<items.size() ; i++) {
                str = items.get(i).split("-");
                z = calcPos( str[0], i );
                y = calcPos( str[1], i );
                start = new Point(x, z);
                size = new Dimension(FILL_SIZE, y - z);
                shapes.get(i).init( start, size, colors.get(i) );
                if( shapes.get(i).containsInSmallBox( selectedPoint ) ) {
                    shapes.get(i).show( start.x + size.width, start.y );
                    shapes.get(i).draw(g2,  true);
                }else {
                    shapes.get(i).hide();
                    shapes.get(i).draw(g2,  false);
                }
                x = x + BETWEEN_GAP;
            }
        }
        
        g.dispose();
        g2.dispose();
    }
    
    public void refresh() {
    }
    
    public void load() {
        Object o = UIControlUtil.getBeanValue(this, handler);
        
        if( o instanceof DayModel )
            dsm = (DayModel) o;
        
        System.out.println(">> " + dsm.getSchedules().size());
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    
    private class MouseSupport extends MouseAdapter {
        
        public void mouseClicked(MouseEvent e) {
            selectedPoint = e.getPoint();
            
            repaint();
            for(int i=items.size()-1 ; i>=0 ; i--) {
                if( shapes.get(i).contains( selectedPoint ) ) {
                    selectedPoly = shapes.get(i);
                    break;
                }
            }
        }
        
    }
    
    
//<editor-fold defaultstate="collapsed" desc="  Helper Methods ">
    public int calcPos(String str, int i) {
        String[] x = str.split(":");
        String[] y = getStartTime().split(":");
        
        double mr = ( Double.parseDouble( x[1] ) / time_interval ) * (TIME_GAP * 2);
        double hr = ( Double.parseDouble( x[0] ) - Double.parseDouble( y[0] )) * (TIME_GAP * 2);
        double result = ( (mr + hr) + (START_POS*2) ) / 2;
        
        return (int) result;
    }
    
    private Color generateColor() {
        float r = Float.parseFloat( Math.random() + "" );
        float g = Float.parseFloat( Math.random() + "" );
        float b = Float.parseFloat( Math.random() + "" );
        return new Color( r ,g ,b, 0.5f);
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="  Setter/Getter ">
    public String getStartTime() {
        return time_start;
    }
    
    public void setStartTime(String startTime) {
        this.time_start = startTime;
    }
    
    public int getTimeInterval() {
        return time_interval;
    }
    
    public void setTimeInterval(int timeInterval) {
        this.time_interval = timeInterval;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public String[] getDepends() {
        return depends;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public String getEndTime() {
        return time_end;
    }
    
    public void setEndTime(String endTime) {
        this.time_end = endTime;
    }
//</editor-fold>
    
    
}
