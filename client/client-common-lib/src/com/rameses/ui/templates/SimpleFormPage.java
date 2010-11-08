package com.rameses.ui.templates;
import com.rameses.rcp.ui.annotations.StyleSheet;

/*
 * OrgMgmtPage.java
 *
 * Created on September 7, 2010, 10:56 AM
 * @author jaycverg
 */

@StyleSheet("com/rameses/client/ui/DataForm.style")
public class SimpleFormPage extends javax.swing.JPanel {
    
    public SimpleFormPage() {
        initComponents();
    }
    
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        headerPanel = new javax.swing.JPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(710, 400));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new java.awt.Dimension(30, 30));
        xLabel2.setBackground(new java.awt.Color(102, 102, 102));
        xLabel2.setForeground(new java.awt.Color(255, 255, 255));
        xLabel2.setExpression("#{formTitle}");
        xLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        xLabel2.setOpaque(true);
        xLabel2.setPadding(new java.awt.Insets(5, 5, 5, 5));
        xLabel2.setPreferredSize(new java.awt.Dimension(101, 30));
        headerPanel.add(xLabel2, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel headerPanel;
    private com.rameses.rcp.control.XLabel xLabel2;
    // End of variables declaration//GEN-END:variables
    
}
