/*
 * DocumentMaster.java
 *
 * Created on January 22, 2010, 5:53 PM
 */

package com.rameses.osiris2.client.templates;

import com.rameses.rcp.ui.annotations.StyleSheet;

/**
 *
 * @author  jaycverg
 */

@StyleSheet("crud/styles/FormTemplate.srss")
public class FormTemplate extends javax.swing.JPanel {
    
    /** Creates new form DocumentMaster */
    public FormTemplate() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlHead = new javax.swing.JPanel();
        lblTitle = new com.rameses.rcp.control.XLabel();
        pnlToolbar = new javax.swing.JPanel();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();

        setLayout(new java.awt.BorderLayout());

        pnlHead.setLayout(new java.awt.BorderLayout());

        pnlHead.setPreferredSize(new java.awt.Dimension(60, 52));
        lblTitle.setBackground(new java.awt.Color(0, 51, 153));
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setExpression("#{formTitle}");
        lblTitle.setFont(new java.awt.Font("Dialog", 1, 14));
        lblTitle.setOpaque(true);
        lblTitle.setPreferredSize(new java.awt.Dimension(200, 26));
        pnlHead.add(lblTitle, java.awt.BorderLayout.SOUTH);

        pnlToolbar.setLayout(new java.awt.BorderLayout());

        pnlToolbar.setPreferredSize(new java.awt.Dimension(20, 26));
        xActionBar1.setDynamic(true);
        xActionBar1.setName("formActions");
        pnlToolbar.add(xActionBar1, java.awt.BorderLayout.CENTER);

        pnlHead.add(pnlToolbar, java.awt.BorderLayout.CENTER);

        add(pnlHead, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XLabel lblTitle;
    private javax.swing.JPanel pnlHead;
    private javax.swing.JPanel pnlToolbar;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    // End of variables declaration//GEN-END:variables

    
}