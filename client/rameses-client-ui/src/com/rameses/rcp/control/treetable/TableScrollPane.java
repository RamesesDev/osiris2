/*
 * TableScrollPane.java
 *
 * Created on December 26, 2011, 12:28 AM
 * @author jaycverg
 */

package com.rameses.rcp.control.treetable;

import com.rameses.rcp.common.AbstractListModel;
import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import javax.swing.JScrollPane;


public class TableScrollPane extends JScrollPane 
{
    private AbstractListModel listModel;
    
    public TableScrollPane(Component view) {
        this(view, null);
    }
    
    public TableScrollPane(Component view, AbstractListModel listModel) {
        super(view);
        this.listModel = listModel;
    }

    protected void processMouseWheelEvent(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        if ( rotation == 0 ) return;
        
        if( listModel != null ) 
        {
            if ( rotation < 0 ) {
                listModel.moveBackRecord();
            } else {
                listModel.moveNextRecord();
            }
        }
    }

    public AbstractListModel getListModel() {
        return listModel;
    }

    public void setListModel(AbstractListModel listModel) {
        this.listModel = listModel;
    }
    
}
