package test.client;

import com.rameses.rcp.constant.TextCase;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import test.templates.Template1;
/*
 * ControlsTestPage.java
 *
 * Created on July 12, 2010, 2:25 PM
 * @author jaycverg
 */

//@StyleSheet("META-INF/style1")
//@Template(Template1.class)
public class ControlsTestPage extends javax.swing.JPanel {
    
    public ControlsTestPage() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xButton2 = new com.rameses.rcp.control.XButton();
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();

        xButton2.setText("Show Input");
        xButton2.setAccelerator("F1");
        xButton2.setName("btnShowInput");

        xDateField1.setCaption("Date");
        xDateField1.setName("entity.date");
        xDateField1.setPreferredSize(new java.awt.Dimension(150, 19));
        xDateField1.setRequired(true);
        xDateField1.setUseDatePickerModel(true);
        formPanel1.add(xDateField1);

        xTextField1.setName("name");

        xTextField2.setText("xTextField2");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 326, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(153, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(374, Short.MAX_VALUE)
                .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, xTextField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, xTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 57, Short.MAX_VALUE)
                .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
