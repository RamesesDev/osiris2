package com.rameses.calendar.control;

import com.rameses.calendar.common.*;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 20101210
 * @author Windhel
 */
public class XCalendarComponent extends JPanel  implements UIControl{
    
    private static final int MATRIX_SIZE = 42;
    
    private static String[] MONTHS = {
        "January" , "February" , "March" , "April" , "May" ,
        "June", "July" , "August" , "September", "October" , "November" , "December"
    };
    private static String[] DAY_OF_WEEK = {
        "Sun" , "Mon" , "Tue" , "Wed" ,
        "Thu" , "Fri" , "Sat"
    };
    
    private JPanel headerPanel = new JPanel();
    private JPanel contentPanel = new JPanel();
    private JPanel dayNamePanel = new JPanel();
    private JPanel calendarPanel = new JPanel();
    private List<DayPanel> dayPanels = new ArrayList();
    private JLabel[] lblHeaderDays = new JLabel[7];
    private JLabel lblMonth = new JLabel();
    private JLabel lblYear = new JLabel();
    
    private Calendar calendar = Calendar.getInstance();
    private Calendar resultDate = Calendar.getInstance();
    private int currDay;
    private int days;
    private Font normalFont;
    private Font selectedFont;
    private Color cellForeground;
    private Color cellBackground;
    private Color selectedForeground;
    private Color selectedBackground;
    private Font headerFont = getFont();
    private DateModel[] dateModels = new DateModel[1];
    private DateModel selectedDateModel = new DateModel();
    private CalendarModel calendarModel;
    private String[] depends;
    private int index;
    private Binding binding;
    private String handler;
    
    public XCalendarComponent() {
        normalFont = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());
        selectedFont = new Font(normalFont.getName(), Font.BOLD, normalFont.getSize());
        
        //
        //headerFont = getFont();
        
        cellForeground = getForeground();
        cellBackground = getBackground();
        selectedForeground = Color.WHITE;
        selectedBackground = Color.PINK;
        calendarPanel.setLayout( new GridLayout(6, 7) );
        dayNamePanel.setLayout( new GridLayout(1,7));
        currDay = 0;
        
        for(int i = 0 ; i < lblHeaderDays.length  ; i++) {
            lblHeaderDays[i] = new JLabel();
            lblHeaderDays[i].setHorizontalAlignment( JLabel.CENTER );
            lblHeaderDays[i].setFont( getHeaderFont());
            lblHeaderDays[i].setText( DAY_OF_WEEK[i] );
            dayNamePanel.add(lblHeaderDays[i]);
        }
        for(int i = 0 ; i < MATRIX_SIZE ; i++) {
            DayPanel dp = new DayPanel();
            dp.addMouseListener(new AbstractCalendarSupport());
            dayPanels.add( dp );
            
            calendarPanel.add( dp );
        }
        
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(lblMonth, BorderLayout.WEST);
        headerPanel.add(lblYear, BorderLayout.EAST);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(dayNamePanel, BorderLayout.NORTH);
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void refresh() {}
    
    public void load() {
        Object o = UIControlUtil.getBeanValue(this, handler);
        
        if( o instanceof CalendarModel )
            calendarModel = (CalendarModel) o;
        
        calendarModel.init();
        calendarModel.load();
        updateCalendar();
        
        calendarModel.setListener( new CalendarModel.Listener() {
            
            public void nextMonth() { updateCalendar(); }
            public void prevMonth() { updateCalendar(); }
            public void refresh() { updateCalendar(); }
            public void nextYear() { updateCalendar(); }
            public void prevYear() { updateCalendar(); }
            public void refresh(Date date) { updateCalendar( date ); }
            public void refresh(List<Date> dates) { updateCalendar(dates ); }
            
        });
    }
    
    private void updateCalendar(List<Date> dates) {
        for(int i=0 ; i<dates.size() ; i++) {
            if( dates.get(i) instanceof Date) {
                updateCalendar( dates.get(i));
            }
        }
    }
    
    private void updateCalendar(Date date) {
        for(int i=0 ; i<MATRIX_SIZE ; i++) {
            DateModel dm = calendarModel.getModelList().get(i);
            DayPanel dp = dayPanels.get(i);
            if(dm.getDate().equals( date )) {
                dp.setModel( dm );
            }
        }
    }
    
    private void updateCalendar() {
        for(int i=0 ; i<MATRIX_SIZE ; i++) {
            DateModel dm = calendarModel.getModelList().get(i);
            DayPanel dp = dayPanels.get(i);
            dp.setModel( dm );
            
            if( calendarModel.getSelectedItem().equals( dm ) ) {
                dp.setBackground( selectedBackground );
                dp.setForeground( selectedForeground );
                dp.setFont( selectedFont );
            } else {
                dp.setBackground( cellBackground );
                dp.setForeground( cellForeground );
                dp.setFont( normalFont );
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="  AbstractCalendarSupport ">
    private class AbstractCalendarSupport extends MouseAdapter {
        
        List<String> result = new ArrayList();
        
        public void mouseClicked(MouseEvent e) {
            try{
                if(e.getSource() instanceof DayPanel) {
                    DayPanel dp = (DayPanel)e.getSource();
                    calendarModel.setSelectedItem( dp.getModel() );
                    setName( dp.getModel().toString() );
                    updateCalendar();
                } else {
                    e.consume();
                }
            }catch(Exception ex) {}
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  DayPanel ">
    private class DayPanel extends JPanel {
        private JLabel day = new JLabel();
        private JLabel content = new JLabel();
        private DateModel model;
        
        public DayPanel() {
            setLayout( new BorderLayout() );
            setBorder(BorderFactory.createEtchedBorder());
            add( day, BorderLayout.NORTH );
            add( content, BorderLayout.CENTER );
        }
        
        public void setDay( String str ) {
            day.setText( str );
        }
        
        public void setContent( String str ) {
            if(! ValueUtil.isEmpty( str ) )
                content.setText("<html>" + str + "</html>");
            else
                content.setText( "" );
        }
        
        private void setIncluded(boolean isIncluded) {
            if( isIncluded ){
                day.setForeground( Color.BLACK );
                content.setForeground( Color.BLACK );
            } else {
                day.setForeground( Color.LIGHT_GRAY );
                content.setForeground( Color.LIGHT_GRAY );
            }
        }
        
        public DateModel getModel() {
            return model;
        }
        
        public void setModel(DateModel model) {
            this.model = model;
            
            if( model != null) {
                setDay( model.getDay() + "" );
                setContent( model.getText() );
                setIncluded( model.isIncluded() );
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Setter/Getter ">
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public Binding getBinding() { return binding; }
    public void setBinding(Binding binding) { this.binding = binding; }
    public int compareTo(Object o) { return UIControlUtil.compare(this, o); }
    
    public Color getCellForeground() {
        return cellForeground;
    }
    
    public void setCellForeground(Color cellForeground) {
        this.cellForeground = cellForeground;
    }
    
    public Color getCellBackground() {
        return cellBackground;
    }
    
    public void setCellBackground(Color cellBackground) {
        this.cellBackground = cellBackground;
    }
    
    public Color getSelectedForeground() {
        return selectedForeground;
    }
    
    public void setSelectedForeground(Color selectedForeground) {
        this.selectedForeground = selectedForeground;
    }
    
    public Color getSelectedBackground() {
        return selectedBackground;
    }
    
    public void setSelectedBackground(Color selectedBackground) {
        this.selectedBackground = selectedBackground;
    }
    
    public Font getSelectedFont() {
        return selectedFont;
    }
    
    public void setSelectedFont(Font selectedFont) {
        this.selectedFont = selectedFont;
    }
    
    public Font getHeaderFont() {
        return headerFont;
    }
    
    public void setHeaderFont(Font headerFont) {
        this.headerFont = headerFont;
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    //</editor-fold>
}
