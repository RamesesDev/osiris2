package test.lookup;
/*
 * ControlsTestPage.java
 *
 * Created on July 12, 2010, 2:25 PM
 * @author jaycverg
 */


public class TestPage extends javax.swing.JPanel {
    
    public TestPage() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xButton1 = new com.rameses.rcp.control.XButton();
        xTable1 = new com.rameses.rcp.control.XTable();
        jLabel2 = new javax.swing.JLabel();
        xButton2 = new com.rameses.rcp.control.XButton();
        jLabel3 = new javax.swing.JLabel();
        xLabel1 = new com.rameses.rcp.control.XLabel();

        setName("_close");
        jLabel1.setText("Item Code :");

        xLookupField1.setExpression("#{code}");
        xLookupField1.setHandler("handler");
        xLookupField1.setName("item");

        xButton1.setText("Add to Cart");
        xButton1.setName("addToCart");

        xTable1.setHandler("listHandler");

        jLabel2.setText("Shopping Cart :");

        xButton2.setText("Close");
        xButton2.setImmediate(true);
        xButton2.setName("_close");

        jLabel3.setText("Description :");

        xLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        xLabel1.setDepends(new String[] {"item"});
        xLabel1.setName("description");
        xLabel1.setPreferredSize(new java.awt.Dimension(75, 19));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(319, Short.MAX_VALUE)
                .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(xLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .add(xLookupField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2)
                .addContainerGap(292, Short.MAX_VALUE))
            .add(xTable1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xLookupField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(xLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xTable1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTable xTable1;
    // End of variables declaration//GEN-END:variables
    
}
