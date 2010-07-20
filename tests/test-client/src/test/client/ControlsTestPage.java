package test.client;

import com.rameses.rcp.constant.TextCase;
/*
 * ControlsTestPage.java
 *
 * Created on July 12, 2010, 2:25 PM
 * @author jaycverg
 */


public class ControlsTestPage extends javax.swing.JPanel {
    
    public ControlsTestPage() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xNumberField2 = new com.rameses.rcp.control.XNumberField();
        xNumberField3 = new com.rameses.rcp.control.XNumberField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox2 = new com.rameses.rcp.control.XCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();

        xButton1.setText("Close");
        xButton1.setDefaultCommand(true);
        xButton1.setName("_close");

        xButton2.setText("Show Input");
        xButton2.setName("showInput");

        formPanel1.setCaptionWidth(100);
        xDateField1.setCaption("Date");
        xDateField1.setName("date");
        xDateField1.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xDateField1);

        xNumberField1.setCaption("Integer Num");
        xNumberField1.setName("intNum");
        xNumberField1.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xNumberField1);

        xNumberField2.setCaption("Double Num");
        xNumberField2.setName("doubleNum");
        xNumberField2.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xNumberField2);

        xNumberField3.setCaption("Decimal Num");
        xNumberField3.setName("decimalNum");
        xNumberField3.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xNumberField3);

        xComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        xComboBox1.setCaption("Normal");
        xComboBox1.setExpression("#{name}");
        xComboBox1.setItems("itemList");
        xComboBox1.setName("item");
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 24));
        formPanel1.add(xComboBox1);

        xComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        xComboBox2.setCaption("Allow Null");
        xComboBox2.setExpression("#{name}");
        xComboBox2.setItems("itemList");
        xComboBox2.setName("item2");
        xComboBox2.setPreferredSize(new java.awt.Dimension(150, 24));
        formPanel1.add(xComboBox2);

        xComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        xComboBox3.setAllowNull(false);
        xComboBox3.setCaption("Disallow Null");
        xComboBox3.setExpression("#{name}");
        xComboBox3.setItems("itemList");
        xComboBox3.setName("item3");
        xComboBox3.setPreferredSize(new java.awt.Dimension(150, 24));
        formPanel1.add(xComboBox3);

        xCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox1.setText("Default Behavior");
        xCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox1.setName("checkBox1");
        formPanel1.add(xCheckBox1);

        xCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox2.setText("Using Check/Uncheck value");
        xCheckBox2.setCheckValue("YES");
        xCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox2.setName("checkBox2");
        xCheckBox2.setUncheckValue("NO");
        formPanel1.add(xCheckBox2);

        xTextArea1.setColumns(20);
        xTextArea1.setRows(5);
        xTextArea1.setCaption("Memo");
        xTextArea1.setName("memo");
        xTextArea1.setTextCase(TextCase.UPPER);
        jScrollPane1.setViewportView(xTextArea1);

        formPanel1.add(jScrollPane1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, formPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XCheckBox xCheckBox2;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XNumberField xNumberField2;
    private com.rameses.rcp.control.XNumberField xNumberField3;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    // End of variables declaration//GEN-END:variables
    
}
