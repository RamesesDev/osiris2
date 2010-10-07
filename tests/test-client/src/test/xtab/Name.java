package test.xtab;
/*
 * Address.java
 *
 * Created on October 6, 2010, 2:01 PM
 * @author jaycverg
 */


public class Name extends javax.swing.JPanel {
    
    public Name() {
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();

        formPanel1.setCaptionWidth(100);
        xTextField3.setCaption("Lastname");
        xTextField3.setName("entity.lastname");
        xTextField3.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xTextField3);

        xTextField1.setCaption("Firstname");
        xTextField1.setName("entity.firstname");
        xTextField1.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xTextField1);

        xTextField2.setCaption("Middlename");
        xTextField2.setName("entity.middlename");
        xTextField2.setPreferredSize(new java.awt.Dimension(150, 19));
        formPanel1.add(xTextField2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 337, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
