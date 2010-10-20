package formpanel;

import com.rameses.rcp.ui.annotations.StyleSheet;
/*
 * Page.java
 *
 * Created on October 18, 2010, 4:49 PM
 * @author jaycverg
 */

@StyleSheet("formpanel/style")
public class Page extends javax.swing.JPanel {
    
    public Page() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton3 = new com.rameses.rcp.control.XButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();

        xButton1.setText("Show Data");
        xButton1.setDefaultCommand(true);
        xButton1.setImmediate(true);
        xButton1.setName("showData");

        xButton3.setText("Save");
        xButton3.setName("save");

        com.rameses.rcp.control.border.XLineBorder xLineBorder1 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder1.setArc(10);
        xLineBorder1.setLineColor(new java.awt.Color(153, 153, 153));
        jScrollPane1.setBorder(xLineBorder1);

        formPanel1.setCaptionWidth(90);
        formPanel1.setDynamic(true);
        formPanel1.setName("formControls");
        xTextField1.setCaption("Firstname");
        xTextField1.setName("entity.firstname");
        xTextField1.setPreferredSize(new java.awt.Dimension(200, 19));
        xTextField1.setRequired(true);
        formPanel1.add(xTextField1);

        xTextField2.setCaption("Lastname");
        xTextField2.setName("entity.lastname");
        xTextField2.setPreferredSize(new java.awt.Dimension(200, 19));
        xTextField2.setRequired(true);
        formPanel1.add(xTextField2);

        jScrollPane1.setViewportView(formPanel1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 213, Short.MAX_VALUE)
                        .add(xButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(xButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
