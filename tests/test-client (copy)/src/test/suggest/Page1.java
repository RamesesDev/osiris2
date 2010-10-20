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
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xButton1 = new com.rameses.rcp.control.XButton();
        xSuggest3 = new com.rameses.rcp.control.XSuggest();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xTextField2 = new com.rameses.rcp.control.XTextField();

        setLayout(null);

        setPreferredSize(new java.awt.Dimension(502, 194));

        formPanel1.setCaptionWidth(150);
        xComboBox1.setCaption("Customer Name");
        xComboBox1.setItems("list");
        xComboBox1.setName("entity.customer");
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xComboBox1);

        xSuggest1.setCaption("Country");
        xSuggest1.setName("entity.country");
        xSuggest1.setOnSuggest("queryCountryList");
        xSuggest1.setPreferredSize(new java.awt.Dimension(200, 19));
        formPanel1.add(xSuggest1);

        xSuggest2.setCaption("Currency");
        xSuggest2.setName("entity.currency");
        xSuggest2.setOnSuggest("queryCurrencyList");
        xSuggest2.setPreferredSize(new java.awt.Dimension(200, 19));
        formPanel1.add(xSuggest2);

        xTextField1.setText("xTextField1");
        xTextField1.setPreferredSize(new java.awt.Dimension(200, 19));
        formPanel1.add(xTextField1);

        add(formPanel1);
        formPanel1.setBounds(10, 10, 420, 100);

        xButton1.setText("Display");
        xButton1.setName("display");
        add(xButton1);
        xButton1.setBounds(400, 160, 81, 25);

        xSuggest3.setCaption("Street");
        xSuggest3.setName("entity.street");
        xSuggest3.setOnSuggest("queryStreets");
        xSuggest3.setPreferredSize(new java.awt.Dimension(200, 19));
        add(xSuggest3);
        xSuggest3.setBounds(160, 120, 200, 20);

        xLabel1.setText("xLabel1");
        xLabel1.setFor("entity.street");
        add(xLabel1);
        xLabel1.setBounds(20, 120, 140, 17);

        xTextField2.setText("xTextField2");
        add(xTextField2);
        xTextField2.setBounds(160, 150, 200, 19);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XSuggest xSuggest1;
    private com.rameses.rcp.control.XSuggest xSuggest2;
    private com.rameses.rcp.control.XSuggest xSuggest3;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
