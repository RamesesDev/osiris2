package com.rameses.rcp.control.table;

import com.rameses.rcp.common.AbstractListModel;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollBar;


public class ListScrollBar extends JScrollBar implements AdjustmentListener {
    
    private AbstractListModel listModel;
    
    public ListScrollBar() {
        super.setVisibleAmount(0);
        super.setMinimum(0);
        super.setMaximum(0);
        super.setVisible(false);
        
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                if ( rotation == 0 ) return;
                
                if ( rotation < 0 )
                    listModel.moveBackRecord();
                else
                    listModel.moveNextRecord();
            }
        });
    }
    
    public void setListModel(AbstractListModel model) {
        this.listModel = model;
    }
    
    public void adjustValues() {
        int rowCount = listModel.getRowCount();
        //int max = rowCount-listModel.getRows()+1;
        
        super.removeAdjustmentListener(this);
        super.setValue(listModel.getTopRow());
        super.setMaximum(listModel.getMaxRows());
        super.setMinimum(0);
        super.setVisibleAmount(0);
        if(rowCount>listModel.getRows()-1) {
            super.setVisible(true);
            super.firePropertyChange("visible", false, true);
        } else {
            super.setVisible(false);
            super.firePropertyChange("visible", true, false);
        }
        
        super.addAdjustmentListener(this);
    }
    
    public void adjustmentValueChanged(AdjustmentEvent e) {
        listModel.setTopRow(e.getValue());
    }
    
}
