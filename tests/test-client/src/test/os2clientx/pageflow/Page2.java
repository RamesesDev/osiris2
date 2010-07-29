package test.os2clientx.pageflow;

import com.rameses.osiris2.client.templates.PageFlowTemplate;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
/*
 * Page2.java
 *
 * Created on July 26, 2010, 3:48 PM
 * @author jaycverg
 */

@Template(PageFlowTemplate.class)
@StyleSheet("META-INF/style3")
public class Page2 extends javax.swing.JPanel {
    
    public Page2() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();

        formPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Entry Form"));
        formPanel1.setCaptionWidth(80);
        xCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox1.setText("Is remote?");
        xCheckBox1.setCaption("");
        xCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox1.setName("entity.remote");
        formPanel1.add(xCheckBox1);

        xTextField1.setCaption("Name");
        xTextField1.setDepends(new String[] {"entity.remote"});
        xTextField1.setName("entity.name");
        xTextField1.setPreferredSize(new java.awt.Dimension(150, 19));
        xTextField1.setRequired(true);
        formPanel1.add(xTextField1);

        xTextField2.setCaption("Address");
        xTextField2.setDepends(new String[] {"entity.remote"});
        xTextField2.setName("entity.address");
        xTextField2.setPreferredSize(new java.awt.Dimension(250, 19));
        xTextField2.setRequired(true);
        formPanel1.add(xTextField2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 366, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
