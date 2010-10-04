/*
 * DatePickerModel.java
 *
 * Created on September 21, 2010, 1:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.control.date;

import com.rameses.rcp.control.XDateField;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 * 
 * 
 * @author Windhel
 * 
 * - MonthCalendarRenderer has a SimpleDateFormat
 * must!! MonthCalendarRenderer.selectedValueFormat = XDateField.valueFormatter
 */

public class DatePickerModel extends AbstractCalendarModel {
    
    private MonthCalendarRenderer dpUI = new MonthCalendarRenderer(this);
    private JPopupMenu popup = new JPopupMenu();
    private SimpleDateFormat outputFormatter;
    private SimpleDateFormat inputFormatter;
    private XDateField parent;
    
    public DatePickerModel(XDateField parent) {
        this();
        this.parent = parent;
        outputFormatter = new SimpleDateFormat(parent.getOutputFormat());
        inputFormatter = new SimpleDateFormat(parent.getInputFormat());
    }
    
    public DatePickerModel() {
        dpUI.setSelectedBackgroundColor(Color.DARK_GRAY);
        dpUI.setSelectedFontColor(Color.WHITE);
        popup.add(dpUI);
    }
        
    public String getSelectedValue() {
        return dpUI.getSelectedValue();
    }
    
    public void setSelectedValue(String value) {
        try {
            Date d = inputFormatter.parse(value);
            parent.setText(inputFormatter.format(d));
        }catch(Exception ex) { ex.printStackTrace(); }
    }
    
    public void hideCalendar() {
        popup.setVisible(false);
    }
    
    public void showCalendar() {
        popup.show(parent, 0, parent.getHeight() - 1);
        popup.setVisible(true);
    }
    
    public JComponent getCalendar() {
        return dpUI;
    }
}
