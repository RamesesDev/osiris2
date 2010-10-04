/*
 * XTabbedPane.java
 *
 * Created on October 1, 2010, 4:50 PM
 * @author jaycverg
 */

package Templates.Classes;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.beans.Beans;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class XTabbedPane extends JTabbedPane implements UIControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    
    
    public XTabbedPane() {
        super();
        initComponents();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initComponents  ">
    private void initComponents() {
        if ( Beans.isDesignTime() ) {
            addTab("Tab 1", new JPanel());
        }
        else {
            
        }
    }
    //</editor-fold>

    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }

    public void refresh() {
    }

    public void load() {
    }

    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
}
