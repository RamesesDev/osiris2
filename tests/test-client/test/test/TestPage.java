package test;

/*
 * TestPage.java
 *
 * Created on July 1, 2010, 1:43 PM
 * @author jaycverg
 */


public class TestPage extends javax.swing.JPanel {
    
    public TestPage() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        formPanel2 = new com.rameses.rcp.util.FormPanel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        formPanel3 = new com.rameses.rcp.util.FormPanel();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xTextField6 = new com.rameses.rcp.control.XTextField();
        xTextField7 = new com.rameses.rcp.control.XTextField();
        xTextField8 = new com.rameses.rcp.control.XTextField();
        xTextField11 = new com.rameses.rcp.control.XTextField();
        formPanel4 = new com.rameses.rcp.util.FormPanel();
        xTextField9 = new com.rameses.rcp.control.XTextField();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        formPanel5 = new com.rameses.rcp.util.FormPanel();
        xTextField12 = new com.rameses.rcp.control.XTextField();
        xTextField13 = new com.rameses.rcp.control.XTextField();

        xTextField3.setText("xTextField3");
        xTextField3.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel2.add(xTextField3);

        xTextField4.setText("xTextField4");
        xTextField4.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel2.add(xTextField4);

        xComboBox1.setPreferredSize(new java.awt.Dimension(100, 24));
        formPanel2.add(xComboBox1);

        formPanel1.setCellpadding(new java.awt.Insets(0, 0, 0, 10));
        formPanel1.setOrientation("horizontal");
        formPanel1.setPadding(new java.awt.Insets(0, 0, 0, 0));
        formPanel1.setShowCaption(false);
        xTextField1.setText("xTextField1");
        xTextField1.setCaption("Address");
        xTextField1.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xTextField1);

        xTextField2.setText("xTextField2");
        xTextField2.setCaption("Name");
        formPanel1.add(xTextField2);

        formPanel2.add(formPanel1);

        formPanel3.setCellpadding(new java.awt.Insets(0, 0, 0, 10));
        formPanel3.setOrientation("horizontal");
        formPanel3.setPadding(new java.awt.Insets(0, 0, 0, 0));
        formPanel3.setShowCaption(false);
        xTextField5.setText("xTextField1");
        xTextField5.setCaption("Address");
        xTextField5.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel3.add(xTextField5);

        xTextField6.setText("xTextField2");
        xTextField6.setCaption("Name");
        formPanel3.add(xTextField6);

        formPanel2.add(formPanel3);

        xTextField7.setText("xTextField7");
        xTextField7.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel2.add(xTextField7);

        xTextField8.setText("xTextField8");
        xTextField8.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel2.add(xTextField8);

        xTextField11.setText("xTextField11");
        formPanel2.add(xTextField11);

        formPanel4.setCaption("");
        formPanel4.setCaptionOrientation("top");
        formPanel4.setCaptionPadding(new java.awt.Insets(0, 0, 5, 5));
        formPanel4.setCellspacing(0);
        formPanel4.setPadding(new java.awt.Insets(0, 0, 10, 0));
        xTextField9.setText("xTextField9");
        xTextField9.setPreferredSize(new java.awt.Dimension(120, 19));
        formPanel4.add(xTextField9);

        xTextField10.setText("xTextField10");
        xTextField10.setPreferredSize(new java.awt.Dimension(120, 19));
        formPanel4.add(xTextField10);

        formPanel5.setOrientation("horizontal");
        formPanel5.setPadding(new java.awt.Insets(3, 0, 0, 0));
        formPanel5.setShowCaption(false);
        xTextField12.setText("xTextField12");
        formPanel5.add(xTextField12);

        xTextField13.setText("xTextField13");
        formPanel5.add(xTextField13);

        formPanel4.add(formPanel5);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(formPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 386, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(formPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 420, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 217, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(formPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.util.FormPanel formPanel2;
    private com.rameses.rcp.util.FormPanel formPanel3;
    private com.rameses.rcp.util.FormPanel formPanel4;
    private com.rameses.rcp.util.FormPanel formPanel5;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField11;
    private com.rameses.rcp.control.XTextField xTextField12;
    private com.rameses.rcp.control.XTextField xTextField13;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    private com.rameses.rcp.control.XTextField xTextField6;
    private com.rameses.rcp.control.XTextField xTextField7;
    private com.rameses.rcp.control.XTextField xTextField8;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
    
}
