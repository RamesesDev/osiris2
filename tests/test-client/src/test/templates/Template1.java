package test.templates;

import com.rameses.rcp.ui.annotations.StyleSheet;
/*
 * Template1.java
 *
 * Created on July 23, 2010, 5:04 PM
 * @author jaycverg
 */

@StyleSheet("META-INF/style2")
public class Template1 extends javax.swing.JPanel {
    
    public Template1() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(51, 0, 102));
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Template 1");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        xActionBar1.setName("actions");
        jPanel1.add(xActionBar1, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    // End of variables declaration//GEN-END:variables
    
}
