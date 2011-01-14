/*
 * XReportPanel.java
 *
 * Created on November 25, 2009, 3:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.reports.ui;

import com.rameses.osiris2.reports.ReportModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.beans.Beans;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author elmo
 */
public class XReportPanel extends JPanel implements UIControl {
    
    private Binding binding;
    private int index;
    private String[] depends;
    
    
    public XReportPanel() {
        super.setLayout(new BorderLayout());
        if(Beans.isDesignTime()) {
            super.setPreferredSize(new Dimension(40, 40));
            super.setOpaque(true);
            super.setBackground(Color.LIGHT_GRAY);
        }
    }

    public void setLayout(LayoutManager mgr) {;}
    
    private void render() {
        if( ValueUtil.isEmpty(getName()) )
            throw new IllegalStateException("Report Panel name must be provided");
        
        Object rpt = UIControlUtil.getBeanValue(this);
        JasperPrint jasperPrint = null;
        if( rpt instanceof ReportModel ) {
            jasperPrint = ((ReportModel)rpt).getReport();
        } else if(rpt instanceof JasperPrint) {
            jasperPrint = (JasperPrint)rpt;
        }
        if( jasperPrint == null ) {
            throw new IllegalStateException("No report found at " + getName());
        }
        
        JRViewer jrv = new JRViewer(jasperPrint);
        
        removeAll();
        add(jrv);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void setStyle(Map props) {
    }
    
    public void load() {
    }
    
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public void refresh() {
        render();
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
}
