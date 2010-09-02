/*
 * DocumentMaster.java
 *
 * Created on January 22, 2010, 5:53 PM
 */

package com.rameses.osiris2.client.templates;

/**
 *
 * @author  jaycverg
 */

public class PageTemplate extends javax.swing.JPanel {
    
    /** Creates new form DocumentMaster */
    public PageTemplate() {
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
        xActionBar1 = new com.rameses.rcp.control.XActionBar();
        xLabel1 = new com.rameses.rcp.control.XLabel();

        setLayout(new java.awt.BorderLayout());

        pnlHead.setLayout(new java.awt.BorderLayout());

        xActionBar1.setDynamic(true);
        xActionBar1.setName("formActions");
        xActionBar1.setPreferredSize(new java.awt.Dimension(136, 26));
        pnlHead.add(xActionBar1, java.awt.BorderLayout.CENTER);

        xLabel1.setBackground(new java.awt.Color(10, 36, 106));
        xLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 5));
        xLabel1.setForeground(new java.awt.Color(255, 255, 255));
        xLabel1.setExpression("#{formTitle}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 0, 20));
        xLabel1.setOpaque(true);
        pnlHead.add(xLabel1, java.awt.BorderLayout.SOUTH);

        add(pnlHead, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlHead;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    private com.rameses.rcp.control.XLabel xLabel1;
    // End of variables declaration//GEN-END:variables
    
    
}