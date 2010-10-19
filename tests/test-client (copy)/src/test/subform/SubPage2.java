package test.subform;
/*
 * SubPage.java
 *
 * Created on July 31, 2010, 11:53 AM
 * @author jaycverg
 */


public class SubPage2 extends javax.swing.JPanel {
    
    public SubPage2() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xButton1 = new com.rameses.rcp.control.XButton();

        xLabel1.setName("entity.caption");
        xLabel1.setPreferredSize(new java.awt.Dimension(407, 19));

        xButton1.setText("Next");
        xButton1.setName("next");
        xButton1.setTarget("root");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(xLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(265, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(xLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XLabel xLabel1;
    // End of variables declaration//GEN-END:variables
    
}
