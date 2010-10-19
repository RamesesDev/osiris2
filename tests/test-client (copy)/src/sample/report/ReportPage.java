package sample.report;
/*
 * ReportPage.java
 *
 * Created on May 14, 2010, 11:29 AM
 * @author jaycverg
 */


public class ReportPage extends javax.swing.JPanel {
    
    public ReportPage() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xTextField3 = new com.rameses.rcp.control.XTextField();
        jToolBar1 = new javax.swing.JToolBar();
        xButton1 = new com.rameses.rcp.control.XButton();

        setLayout(null);

        setPreferredSize(new java.awt.Dimension(400, 300));
        xTextField3.setCaption("Requested By");
        xTextField3.setName("requestedBy");
        add(xTextField3);
        xTextField3.setBounds(10, 50, 350, 19);

        jToolBar1.setFloatable(false);
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 31));
        xButton1.setText("Preview");
        xButton1.setName("viewReport");
        jToolBar1.add(xButton1);

        add(jToolBar1);
        jToolBar1.setBounds(0, 0, 400, 30);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar jToolBar1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
