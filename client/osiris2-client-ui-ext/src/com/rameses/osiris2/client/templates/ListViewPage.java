/*
 * ListViewPage.java
 *
 * Created on January 23, 2010, 8:16 AM
 */

package com.rameses.osiris2.client.templates;

/**
 *
 * @author  elmo
 */
public class ListViewPage extends javax.swing.JPanel {
    
    /** Creates new form ListViewPage */
    public ListViewPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        pnlHead = new javax.swing.JPanel();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();
        xActionBar2 = new com.rameses.rcp.control.XActionBar();
        lblTitle = new com.rameses.rcp.control.XLabel();
        pnlFooter = new javax.swing.JPanel();
        actionFooter = new com.rameses.rcp.control.XActionBar();
        pnlCenter = new javax.swing.JPanel();
        xSubFormPanel1 = new com.rameses.rcp.control.XSubFormPanel();
        xGrid1 = new com.rameses.rcp.control.XGrid();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(530, 452));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        pnlHeader.setPreferredSize(new java.awt.Dimension(80, 52));
        pnlHead.setLayout(new java.awt.BorderLayout());

        pnlHead.setPreferredSize(new java.awt.Dimension(40, 26));

        xActionBar1.setDynamic(true);
        xActionBar1.setName("listHandler.listActions");
        pnlHead.add(xActionBar1, java.awt.BorderLayout.WEST);

        xActionBar2.setDynamic(true);
        xActionBar2.setName("listHandler.viewActions");
        pnlHead.add(xActionBar2, java.awt.BorderLayout.EAST);

        pnlHeader.add(pnlHead, java.awt.BorderLayout.CENTER);

        lblTitle.setBackground(new java.awt.Color(0, 51, 153));
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setExpression("#{listHandler.title}");
        lblTitle.setFont(new java.awt.Font("Dialog", 1, 14));
        lblTitle.setOpaque(true);
        lblTitle.setPreferredSize(new java.awt.Dimension(40, 26));
        pnlHeader.add(lblTitle, java.awt.BorderLayout.SOUTH);

        add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlFooter.setLayout(new java.awt.BorderLayout());

        pnlFooter.setPreferredSize(new java.awt.Dimension(30, 30));

        actionFooter.setName("listHandler.pageActions");
        pnlFooter.add(actionFooter, java.awt.BorderLayout.WEST);

        add(pnlFooter, java.awt.BorderLayout.SOUTH);

        pnlCenter.setLayout(new java.awt.BorderLayout());

        xSubFormPanel1.setHandler("listHandler.queryHandler");
        pnlCenter.add(xSubFormPanel1, java.awt.BorderLayout.NORTH);

        xGrid1.setHandler("listHandler");
        pnlCenter.add(xGrid1, java.awt.BorderLayout.CENTER);

        add(pnlCenter, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XActionBar actionFooter;
    private javax.swing.JPanel jPanel2;
    private com.rameses.rcp.control.XLabel lblTitle;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHead;
    private javax.swing.JPanel pnlHeader;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    private com.rameses.rcp.control.XActionBar xActionBar2;
    private com.rameses.rcp.control.XGrid xGrid1;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel1;
    // End of variables declaration//GEN-END:variables
    
}
