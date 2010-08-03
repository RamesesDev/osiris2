package test.xtree;
/*
 * Page1.java
 *
 * Created on August 2, 2010, 11:04 AM
 * @author jaycverg
 */


public class Page1 extends javax.swing.JPanel {
    
    public Page1() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTree1 = new com.rameses.rcp.control.XTree();
        jPanel1 = new javax.swing.JPanel();
        xSubFormPanel1 = new com.rameses.rcp.control.XSubFormPanel();
        xButton1 = new com.rameses.rcp.control.XButton();

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setDividerSize(5);
        xTree1.setHandler("treeHandler");
        xTree1.setName("selected");
        jScrollPane1.setViewportView(xTree1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        xSubFormPanel1.setDepends(new String[] {"selected"});
        xSubFormPanel1.setDynamic(true);
        xSubFormPanel1.setHandler("subForm");
        org.jdesktop.layout.GroupLayout xSubFormPanel1Layout = new org.jdesktop.layout.GroupLayout(xSubFormPanel1);
        xSubFormPanel1.setLayout(xSubFormPanel1Layout);
        xSubFormPanel1Layout.setHorizontalGroup(
            xSubFormPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 244, Short.MAX_VALUE)
        );
        xSubFormPanel1Layout.setVerticalGroup(
            xSubFormPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 267, Short.MAX_VALUE)
        );

        xButton1.setText("Add Node");
        xButton1.setName("addNode");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(xSubFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xSubFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
        );
        jSplitPane1.setRightComponent(jPanel1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel1;
    private com.rameses.rcp.control.XTree xTree1;
    // End of variables declaration//GEN-END:variables
    
}
