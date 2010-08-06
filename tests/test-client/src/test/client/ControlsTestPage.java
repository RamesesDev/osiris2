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

@StyleSheet("META-INF/style1, META-INF/style4")
@Template(Template1.class)
public class ControlsTestPage extends javax.swing.JPanel {
    
    public ControlsTestPage() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xNumberField2 = new com.rameses.rcp.control.XNumberField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xNumberField3 = new com.rameses.rcp.control.XNumberField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox2 = new com.rameses.rcp.control.XCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xFileBrowser1 = new com.rameses.rcp.control.XFileBrowser();
        xRadio1 = new com.rameses.rcp.control.XRadio();
        xRadio2 = new com.rameses.rcp.control.XRadio();
        xRadio3 = new com.rameses.rcp.control.XRadio();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xButton3 = new com.rameses.rcp.control.XButton();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xButton4 = new com.rameses.rcp.control.XButton();
        xButton5 = new com.rameses.rcp.control.XButton();

        xButton1.setText("Close");
        xButton1.setImmediate(true);
        xButton1.setName("_close");

        xButton2.setText("Show Input");
        xButton2.setName("btnShowInput");

        formPanel1.setCaptionWidth(100);
        xNumberField1.setCaption("Integer Num");
        xNumberField1.setName("intNum");
        xNumberField1.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xNumberField1);

        xNumberField2.setCaption("Double Num");
        xNumberField2.setName("doubleNum");
        xNumberField2.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xNumberField2);

        xDateField1.setCaption("Date");
        xDateField1.setName("entity.date");
        xDateField1.setPreferredSize(new java.awt.Dimension(150, 19));
        xDateField1.setRequired(true);
        formPanel1.add(xDateField1);

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
        xComboBox2.setName("entity.item2");
        xComboBox2.setPreferredSize(new java.awt.Dimension(150, 24));
        formPanel1.add(xComboBox2);

        xComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        xComboBox3.setAllowNull(false);
        xComboBox3.setCaption("Disallow Null");
        xComboBox3.setExpression("#{name}");
        xComboBox3.setItems("itemList");
        xComboBox3.setName("entity.item3");
        xComboBox3.setPreferredSize(new java.awt.Dimension(150, 24));
        formPanel1.add(xComboBox3);

        xCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox1.setText("Default Behavior");
        xCheckBox1.setCaption("");
        xCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox1.setName("checkBox1");
        formPanel1.add(xCheckBox1);

        xCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox2.setText("Using Check/Uncheck value");
        xCheckBox2.setCaption("");
        xCheckBox2.setCheckValue("YES");
        xCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox2.setName("checkBox2");
        xCheckBox2.setUncheckValue("NO");
        formPanel1.add(xCheckBox2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(225, 80));
        xTextArea1.setColumns(20);
        xTextArea1.setRows(5);
        xTextArea1.setCaption("Memo");
        xTextArea1.setDepends(new String[] {"checkBox2"});
        xTextArea1.setName("entity.memo");
        xTextArea1.setRequired(true);
        xTextArea1.setTextCase(TextCase.UPPER);
        jScrollPane1.setViewportView(xTextArea1);

        formPanel1.add(jScrollPane1);

        xFileBrowser1.setCaption("File");
        xFileBrowser1.setName("file");
        xFileBrowser1.setPreferredSize(new java.awt.Dimension(250, 19));
        xFileBrowser1.setRequired(true);
        formPanel1.add(xFileBrowser1);

        xRadio1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xRadio1.setText("Single");
        xRadio1.setCaption("Civil Status");
        xRadio1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xRadio1.setName("entity.civilStat");
        xRadio1.setOptionValue("SINGLE");
        formPanel1.add(xRadio1);

        xRadio2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xRadio2.setText("Married");
        xRadio2.setCaption("");
        xRadio2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xRadio2.setName("entity.civilStat");
        xRadio2.setOptionValue("MARRIED");
        formPanel1.add(xRadio2);

        xRadio3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xRadio3.setText("Widow");
        xRadio3.setCaption("");
        xRadio3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xRadio3.setName("entity.civilStat");
        xRadio3.setOptionValue("WIDOW");
        formPanel1.add(xRadio3);

        xLookupField1.setCaption("Test Lookup");
        xLookupField1.setHandler("lookupHandler");
        xLookupField1.setName("entity.product");
        xLookupField1.setPreferredSize(new java.awt.Dimension(200, 19));
        formPanel1.add(xLookupField1);

        xButton3.setText("Toggle Readonly");
        xButton3.setImmediate(true);
        xButton3.setName("btnToggle");

        xTextField1.setName("entity.testPost");

        xButton4.setText("Post");
        xButton4.setDefaultCommand(true);
        xButton4.setName("testPost");

        xButton5.setText("Save");
        xButton5.setImmediate(true);
        xButton5.setName("save");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, formPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                            .add(xButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(xButton5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(layout.createSequentialGroup()
                            .add(xTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 181, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(xButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 441, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(xTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XButton xButton4;
    private com.rameses.rcp.control.XButton xButton5;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XCheckBox xCheckBox2;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFileBrowser xFileBrowser1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XNumberField xNumberField2;
    private com.rameses.rcp.control.XNumberField xNumberField3;
    private com.rameses.rcp.control.XRadio xRadio1;
    private com.rameses.rcp.control.XRadio xRadio2;
    private com.rameses.rcp.control.XRadio xRadio3;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
    
}
