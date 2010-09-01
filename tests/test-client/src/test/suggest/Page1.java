package test.suggest;

import com.rameses.rcp.constant.TextCase;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import test.templates.Template1;
/*
 * Page1.java
 *
 * Created on July 12, 2010, 2:25 PM
 * @author jaycverg
 */

public class Page1 extends javax.swing.JPanel {
    
    public Page1() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xSuggest1 = new com.rameses.rcp.control.XSuggest();
        xSuggest2 = new com.rameses.rcp.control.XSuggest();
        xSuggest3 = new com.rameses.rcp.control.XSuggest();
        xButton1 = new com.rameses.rcp.control.XButton();

        formPanel1.setCaptionWidth(150);
        xComboBox1.setCaption("Customer Name");
        xComboBox1.setItems("list");
        xComboBox1.setName("entity.customer");
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 24));
        formPanel1.add(xComboBox1);

        xSuggest1.setAllowNew(false);
        xSuggest1.setCaption("Country");
        xSuggest1.setExpression("#{name}");
        xSuggest1.setName("entity.country");
        xSuggest1.setOnSuggest("queryCountryList");
        xSuggest1.setPreferredSize(new java.awt.Dimension(200, 24));
        formPanel1.add(xSuggest1);

        xSuggest2.setAllowNew(false);
        xSuggest2.setCaption("Currency");
        xSuggest2.setExpression("#{description}");
        xSuggest2.setName("entity.currency");
        xSuggest2.setOnSuggest("queryCurrencyList");
        xSuggest2.setPreferredSize(new java.awt.Dimension(200, 24));
        formPanel1.add(xSuggest2);

        xSuggest3.setCaption("Street");
        xSuggest3.setName("entity.street");
        xSuggest3.setOnSuggest("queryStreets");
        xSuggest3.setPreferredSize(new java.awt.Dimension(200, 24));
        formPanel1.add(xSuggest3);

        xButton1.setText("Display");
        xButton1.setName("display");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, formPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XSuggest xSuggest1;
    private com.rameses.rcp.control.XSuggest xSuggest2;
    private com.rameses.rcp.control.XSuggest xSuggest3;
    // End of variables declaration//GEN-END:variables
    
}
