package com.rameses.ui.templates;
import com.rameses.rcp.ui.annotations.StyleSheet;

/*
 * OrgMgmtPage.java
 *
 * Created on September 7, 2010, 10:56 AM
 * @author jaycverg
 */

@StyleSheet("com/rameses/client/ui/DataForm.style")
public class DataFormPage extends javax.swing.JPanel {
    
    public DataFormPage() {
        initComponents();
    }
    
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        headerPanel = new javax.swing.JPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        jPanel3 = new javax.swing.JPanel();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();
        statusPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(710, 400));
        headerPanel.setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new java.awt.Dimension(50, 60));
        xLabel2.setBackground(new java.awt.Color(102, 102, 102));
        xLabel2.setForeground(new java.awt.Color(255, 255, 255));
        xLabel2.setExpression("#{formTitle}");
        xLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        xLabel2.setOpaque(true);
        xLabel2.setPadding(new java.awt.Insets(5, 5, 5, 5));
        xLabel2.setPreferredSize(new java.awt.Dimension(101, 30));
        headerPanel.add(xLabel2, java.awt.BorderLayout.NORTH);

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 20));
        xActionBar1.setName("formActions");
        xActionBar1.setOpaque(false);
        xActionBar1.setPadding(new java.awt.Insets(2, 2, 2, 2));
        xActionBar1.setUseToolBar(false);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(xActionBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
                .add(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(xActionBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        headerPanel.add(jPanel3, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        statusPanel.setPreferredSize(new java.awt.Dimension(100, 30));
        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 642, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 30, Short.MAX_VALUE)
        );
        add(statusPanel, java.awt.BorderLayout.SOUTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel statusPanel;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    private com.rameses.rcp.control.XLabel xLabel2;
    // End of variables declaration//GEN-END:variables
    
}
