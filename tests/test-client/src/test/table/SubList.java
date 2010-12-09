/*
 * TestPage.java
 *
 * Created on June 19, 2010, 10:32 AM
 */

package test.table;

import com.rameses.rcp.ui.annotations.StyleSheet;

/**
 *
 * @author  compaq
 */

//@StyleSheet("META-INF/sublist")
public class SubList extends javax.swing.JPanel {
    
    /** Creates new form TestPage */
    public SubList() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xTable1 = new com.rameses.rcp.control.XTable();
        xButton1 = new com.rameses.rcp.control.XButton();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xList1 = new com.rameses.rcp.control.XList();
        jLabel2 = new javax.swing.JLabel();

        xTable1.setHandler("handler");
        xTable1.setName("selectedItem");

        xButton1.setText("Refresh Table");
        xButton1.setName("fireAction");

        xLabel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        xLabel1.setDepends(new String[] {"selectedItem"});
        xLabel1.setName("selectedName");

        jLabel1.setText("Selected :");

        xList1.setExpression("#{caption}");
        xList1.setItems("colList");
        xList1.setMultiselect(true);
        xList1.setName("selectedCols");
        jScrollPane1.setViewportView(xList1);

        jLabel2.setText("Available Columns");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(xButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xTable1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(xTable1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XList xList1;
    private com.rameses.rcp.control.XTable xTable1;
    // End of variables declaration//GEN-END:variables
    
}
