/*
 * ReportPanel.java
 *
 * Created on November 25, 2009, 3:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.reports.ui;

import com.rameses.osiris2.reports.ReportModel;
import com.rameses.rcp.framework.AbstractUIControl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.Beans;
import java.util.Map;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author elmo
 */
public class ReportPanel extends AbstractUIControl {
    
    /** Creates a new instance of ReportPanel */
    public ReportPanel() {
        super.setLayout(new BorderLayout());
        if(Beans.isDesignTime()) {
            super.setOpaque(true);
            super.setBackground(Color.GREEN);
        }
    }

    public void refresh(String callerFieldName) {
        render();
    }

    private void render() {
        if( isEmpty(getName())) 
            throw new IllegalStateException("Report Panel name must be provided");
            
        Object rpt = super.getBeanValue(getName());
        JasperPrint jasperPrint = null;
        if( rpt instanceof ReportModel ) {
            jasperPrint = ((ReportModel)rpt).getReport();
        }
        else if(rpt instanceof JasperPrint) {
            jasperPrint = (JasperPrint)rpt;
        }
        if( jasperPrint == null ) {
            throw new IllegalStateException("No report found at " + getName());
        }
        
        removeAll();
        
        JRViewer jrv = new JRViewer(jasperPrint);
        //JComponent jc = (JComponent) jrv.getComponent(0); 
        add(jrv);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void setStyle(Map props) {
    }

    public void load() {
    }
    
}
