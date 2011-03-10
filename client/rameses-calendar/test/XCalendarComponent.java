import com.rameses.calendar.common.*;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.Beans;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 20101210
 * @author Windhel
 */
public class XCalendarComponent extends JPanel  implements UIControl{
    
    private static String[] MONTHS = { "January" , "February" , "March" , "April" , "May" ,
    "June", "July" , "August" , "September", "October" , "November" , "December" };
    private static String[] DAY_OF_WEEK = {"Sun" , "Mon" , "Tue" , "Wed" ,
    "Thu" , "Fri" , "Sat" };
    
    private Calendar calendar = Calendar.getInstance();
    private Calendar resultDate = Calendar.getInstance();
    
    private JPanel headerPanel = new JPanel();
    private JPanel contentPanel = new JPanel();
    private JPanel dayNamePanel = new JPanel();
    private JPanel dayPanel = new JPanel();
    private JPanel[] panelDays = new JPanel[42];
    private JLabel[] lblHeaderDays = new JLabel[7];
    private JLabel[] lblDays = new JLabel[42];
    private JLabel[] lblContentDays = new JLabel[42];
    private JLabel lblMonth = new JLabel();
    private JLabel lblYear = new JLabel();
    
    private int currDay;
    private int days;
    private Font normalFont;
    private Font selectedFont;
    private Color normalFontColor;
    private Color normalBackgroundColor;
    private Color selectedFontColor;
    private Color selectedBackgroundColor;
    
    private DateModel[] dateModels = new DateModel[1];
    private DateModel selectedDateModel = new DateModel();
    private CalendarModel calendarModel;
    
    private String[] depends;
    private int index;
    private Binding binding;
    
    public XCalendarComponent() {
        Calendar c = Calendar.getInstance();
        c.set(2010, 11, 11);
        dateModels[0] = new DateModel(c.getTime());
        dateModels[0].setText( "Birthday - Alvin" );
        
        normalFont = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());
        selectedFont = new Font(normalFont.getName(), Font.BOLD, normalFont.getSize());
        normalFontColor = getForeground();
        normalBackgroundColor = getBackground();
        selectedFontColor = Color.WHITE;
        selectedBackgroundColor = Color.BLACK;
        dayPanel.setLayout( new GridLayout(6, 7) );
        dayNamePanel.setLayout( new GridLayout(1,7));
        currDay = 0;
        
        for(int i = 0 ; i < lblHeaderDays.length  ; i++) {
            lblHeaderDays[i] = new JLabel( DAY_OF_WEEK[i]);
            lblHeaderDays[i].setHorizontalAlignment( JLabel.CENTER );
            
            dayNamePanel.add(lblHeaderDays[i]);
        }
        for(int i = 0 ; i < lblDays.length ; i++) {
            lblDays[i] = new JLabel();
            lblDays[i].setOpaque(true);
            lblDays[i].setHorizontalAlignment(JLabel.RIGHT);
            lblContentDays[i] = new JLabel();
            lblContentDays[i].setOpaque(true);
            
            panelDays[i] = new JPanel();
            panelDays[i].setLayout(new BorderLayout());
            panelDays[i].setBorder(BorderFactory.createEtchedBorder());
            panelDays[i].addMouseListener(new AbstractCalendarSupport());
            
            
            panelDays[i].add(lblDays[i], BorderLayout.NORTH);
            panelDays[i].add(lblContentDays[i], BorderLayout.CENTER);
            dayPanel.add(panelDays[i]);
        }
        
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(lblMonth, BorderLayout.WEST);
        headerPanel.add(lblYear, BorderLayout.EAST);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(dayNamePanel, BorderLayout.NORTH);
        contentPanel.add(dayPanel, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        buildCalendar();
    }
    
    public void buildCalendar() {
        currDay = 0;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 01);
        days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        lblMonth.setText( MONTHS[calendar.get(Calendar.MONTH)] );
        lblYear.setText( ""  + calendar.get(Calendar.YEAR) );
        
        for(int i = 0; i < lblDays.length; i++) {
            if( (i >= calendar.get(Calendar.DAY_OF_WEEK) - 1) && (i < days + calendar.get(Calendar.DAY_OF_WEEK) - 1)) {
                currDay++;
                if(resultDate.get(Calendar.DAY_OF_MONTH) == currDay) {
                    lblDays[i].setFont(selectedFont);
                    lblDays[i].setBackground(selectedBackgroundColor);
                    lblDays[i].setForeground(selectedFontColor);
                } else {
                    lblDays[i].setFont(normalFont);
                    lblDays[i].setBackground(normalBackgroundColor);
                    lblDays[i].setForeground(normalFontColor);
                }
                lblDays[i].setText("" + currDay);
                panelDays[i].setToolTipText("" + currDay);
            } else {
                lblDays[i].setBackground(normalBackgroundColor);
                lblDays[i].setForeground(normalFontColor);
                lblDays[i].setText(" ");
                lblDays[i].setFont(normalFont);
                panelDays[i].setToolTipText(" ");
            }
            
            
        }
        if(! Beans.isDesignTime() )
            renderDateModels( calendar.get(Calendar.DAY_OF_WEEK) - 2 );
    }
    
    private void renderDateModels(int inc) {
        for(DateModel dateModel : dateModels) {
            if(lblYear.getText().equals( "" + dateModel.getYear() ) &&
                    lblMonth.getText().equals( "" + MONTHS[dateModel.getMonth()]) &&
                    lblDays[dateModel.getDay() + inc].getText().equals("" + dateModel.getDay()) ) {
                lblContentDays[dateModel.getDay() + inc].setText( dateModel.getText() );
            }
        }
    }
    
    public String[] getDepends() { return depends; }   
    public void setDepends(String[] depends) { this.depends = depends; }

    public int getIndex() { return index; }    
    public void setIndex(int index) { this.index = index; }
        
    public Binding getBinding() { return binding; }
    public void setBinding(Binding binding) { this.binding = binding; }

    public void refresh() {
    }

    public void load() {
        Object o = UIControlUtil.getBeanValue(this);
        
        if( o instanceof CalendarModel )
            calendarModel = (CalendarModel) o;
        
        System.out.println(">> " + calendarModel);
    }

    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
//<editor-fold defaultstate="collapsed" desc="  MonthCalendarRendererSupport ">
    private class AbstractCalendarSupport implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            try{
                if(e.getSource() instanceof JPanel) {
                    String result = ((JPanel)e.getSource()).getToolTipText().trim();
                    resultDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Integer.parseInt(result));
                    buildCalendar();
                }else {
                    e.consume();
                }
            }catch(Exception ex) {}
        }
        
        public void mousePressed(MouseEvent e) {}
        
        public void mouseReleased(MouseEvent e) {}
        
        public void mouseEntered(MouseEvent e) {}
        
        public void mouseExited(MouseEvent e) {}
    }
//</editor-fold>
    
}
