package com.rameses.rcp.workspace;
/*
 * WorkspaceDefaultTpl.java
 *
 * Created on February 4, 2011, 10:21 AM
 * @author jaycverg
 */


public class WorkspaceDefaultTpl extends javax.swing.JPanel {
    
    public WorkspaceDefaultTpl() {
        initComponents();
    }
    
    public void setTitle(String title) {
        label.setExpression(title);
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        label = new com.rameses.rcp.control.XLabel();

        setLayout(new java.awt.BorderLayout());

        label.setBackground(new java.awt.Color(0, 0, 102));
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.setForeground(new java.awt.Color(255, 255, 255));
        label.setText("#{title}");
        label.setFont(new java.awt.Font("Arial", 1, 12));
        label.setOpaque(true);
        add(label, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XLabel label;
    // End of variables declaration//GEN-END:variables
    
}
