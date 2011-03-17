/*
 * MonthCalendarRenderer.java
 *
 * Created on September 20, 2010, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.control.date;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Windhel
 */
public class MonthCalendarRenderer extends JPanel {
    
    private JButton previousMonth = new JButton("<");
    private JButton nextMonth = new JButton(">");
    private JButton previousYear = new JButton("<");
    private JButton nextYear = new JButton(">");
    private JLabel displayMonth = new JLabel();
    private JLabel displayYear = new JLabel();
    private GridBagConstraints gridBagCons = new GridBagConstraints();
    private JPanel panel = new JPanel();
    private JPanel datePanel = new JPanel();
    private Calendar calendar = Calendar.getInstance();
    private Calendar resultDate = Calendar.getInstance();
    private int currDay;
    private int days;
    private String[] months = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    private JLabel[] lblDays = new JLabel[56];
    
    private Color normalFontColor;
    private Color normalBackgroundColor;
    private Font normalFont;
    private Color selectedFontColor;
    private Color selectedBackgroundColor;
    private Font selectedFont;
    private SimpleDateFormat selectedValueFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String selectedValue;
    
    private DatePickerModel parent;
    
    private Listener listener;
    
    
    public MonthCalendarRenderer() {
        init();
    }
    
    public MonthCalendarRenderer(DatePickerModel parent) {
        this();
        this.parent = parent;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  init ">
    public void init() {
        normalFont = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());
        selectedFont = new Font(normalFont.getName(), Font.BOLD, normalFont.getSize());
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i = 0; i < lblDays.length ; i++) {
            lblDays[i] = new JLabel();
            lblDays[i].setOpaque(true);
            lblDays[i].addMouseListener(new AbstractCalendarSupport());
            lblDays[i].setPreferredSize(new Dimension(32,24));
            lblDays[i].setHorizontalAlignment(JLabel.CENTER);
        }
        nextYear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.YEAR, 1);
                displayYear.setText("" + calendar.get(Calendar.YEAR));
                resultDate.set(calendar.get(Calendar.YEAR), resultDate.get(Calendar.MONTH), resultDate.get(Calendar.DAY_OF_MONTH));
                buildCalendar();
                setSelectedValue(selectedValueFormat.format(resultDate.getTime()));
                parent.setSelectedValue(getSelectedValue());
            }
        });
        previousYear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.YEAR, -1);
                displayYear.setText("" + calendar.get(Calendar.YEAR));
                resultDate.set(calendar.get(Calendar.YEAR), resultDate.get(Calendar.MONTH), resultDate.get(Calendar.DAY_OF_MONTH));
                buildCalendar();
                setSelectedValue(selectedValueFormat.format(resultDate.getTime()));
                parent.setSelectedValue(getSelectedValue());
            }
        });
        nextMonth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.MONTH, 1);
                displayMonth.setText("" + months[calendar.get(Calendar.MONTH)]);
                displayYear.setText("" + calendar.get(Calendar.YEAR));
                resultDate.set(resultDate.get(Calendar.YEAR), calendar.get(Calendar.MONTH), resultDate.get(Calendar.DAY_OF_MONTH));
                buildCalendar();
                setSelectedValue(selectedValueFormat.format(resultDate.getTime()));
                parent.setSelectedValue(getSelectedValue());
            }
        });
        previousMonth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.MONTH, -1);
                displayMonth.setText("" + months[calendar.get(Calendar.MONTH)]);
                displayYear.setText("" + calendar.get(Calendar.YEAR));
                resultDate.set(resultDate.get(Calendar.YEAR), calendar.get(Calendar.MONTH), resultDate.get(Calendar.DAY_OF_MONTH));
                buildCalendar();
                setSelectedValue(selectedValueFormat.format(resultDate.getTime()));
                parent.setSelectedValue(getSelectedValue());
            }
        });
        
        setLayout(new BorderLayout());
        panel.add(previousMonth);
        panel.add(displayMonth);
        panel.add(nextMonth);
        panel.add(previousYear);
        panel.add(displayYear);
        panel.add(nextYear);
        add(panel, BorderLayout.NORTH);
        datePanel.setLayout(new GridBagLayout());
        lblDays[1] = new JLabel("Sun");
        lblDays[2] = new JLabel("Mon");
        lblDays[3] = new JLabel("Tue");
        lblDays[4] = new JLabel("Wed");
        lblDays[5] = new JLabel("Thu");
        lblDays[6] = new JLabel("Fri");
        lblDays[7] = new JLabel("Sat");
        currDay = 0;
        for(int y = 1; y < 8; y++) {
            for(int x = 1; x < 8;x++) {
                currDay++;
                gridBagCons.gridx = x;
                gridBagCons.gridy = y;
                if(currDay < lblDays.length)
                    datePanel.add(lblDays[currDay],gridBagCons);
            }
        }
        add(datePanel,  BorderLayout.CENTER);
        displayMonth.setText("" + months[calendar.get(Calendar.MONTH)]);
        displayYear.setText("" + calendar.get(Calendar.YEAR));
        buildCalendar();
        normalFontColor = getForeground();
        normalBackgroundColor = getBackground();
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  buildCalendar ">
    public void buildCalendar() {
        currDay = 0;/*
        calendar.set(Calendar.DAY_OF_MONTH, 01);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));*/
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 01);
        days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        //gets the Day(eg.Sunday = 1, Monday = 2, Tuesday = 3, Wednesday = 4) of MONTH 01, YEAR
        //up until the total Days in a month +(plus) the returned Day to cover up
        //the loss from 0 AND initializes the labels from the said range
        for(int i = calendar.get(Calendar.DAY_OF_WEEK) + 7; i < days + calendar.get(Calendar.DAY_OF_WEEK) + 7; i++) {
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
        }
        //initializes the labels from 0 to the Current Day with " "
        for(int i = 8; i <  calendar.get(Calendar.DAY_OF_WEEK) + 7;i++){
            lblDays[i].setBackground(normalBackgroundColor);
            lblDays[i].setForeground(normalFontColor);
            lblDays[i].setText(" ");
            lblDays[i].setFont(normalFont);
        }
        //initializes the labels from the Current Day +(plus) the total Days in a month
        //until lblDays.length
        for(int i = days + calendar.get(Calendar.DAY_OF_WEEK) + 7; i < lblDays.length;i++) {
            lblDays[i].setBackground(normalBackgroundColor);
            lblDays[i].setForeground(normalFontColor);
            lblDays[i].setText(" ");
            lblDays[i].setFont(normalFont);
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  MonthCalendarRendererSupport ">
    private class AbstractCalendarSupport implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            try{
                if(e.getSource() instanceof JLabel) {
                    String result = ((JLabel)e.getSource()).getText();
                    resultDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Integer.parseInt(result));
                    buildCalendar();
                    setSelectedValue(selectedValueFormat.format(resultDate.getTime()));
                    parent.setSelectedValue(getSelectedValue());
                    if( listener != null ) {
                        listener.onSelect( getSelectedValue() );
                    }
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
    
    
    //<editor-fold defaultstate="collapsed" desc="  setter/getter ">
    public Color getSelectedFontColor() {
        return selectedFontColor;
    }
    
    public void setSelectedFontColor(Color selectedFontColor) {
        this.selectedFontColor = selectedFontColor;
    }
    
    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }
    
    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }
    
    public String getSelectedValue() {
        return selectedValue;
    }
    
    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
    //</editor-fold>
    
    public Listener getListener() {
        return listener;
    }
    
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    
    
    //inner class
    public static interface Listener {
        
        void onSelect(Object value);
        
    }
    
}
