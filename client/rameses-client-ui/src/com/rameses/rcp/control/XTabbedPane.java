/*
 * XTabbedPane.java
 *
 * Created on October 1, 2010, 4:50 PM
 * @author jaycverg
 */

package Templates.Classes;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.ActionProvider;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class XTabbedPane extends JTabbedPane implements UIControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean dynamic;
    
    private int oldIndex;
    
    public XTabbedPane() {
        super();
        initComponents();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initComponents  ">
    private void initComponents() {
        if ( Beans.isDesignTime() ) {
            addTab("Tab 1", new JPanel());
        }
        
    }
    //</editor-fold>
    
    public void refresh() {
    }
    
    public void load() {
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void loadActions() {
        List<Action> actions = new ArrayList();
        
        //--get actions defined from the code bean
        Object value = null;
        try {
            value = UIControlUtil.getBeanValue(this);
        } catch(Exception e) {;}
        
        if (value == null) {
            //do nothing
        } else if (value.getClass().isArray()) {
            for (Action aa: (Action[]) value) {
                actions.add(aa);
            }
        } else if (value instanceof Collection) {
            actions.addAll((Collection) value);
        }
        
        //--get actions defined from the action provider
        ActionProvider actionProvider = ClientContext.getCurrentContext().getActionProvider();
        if (actionProvider != null) {
            UIController controller = binding.getController();
            List<Action> aa = actionProvider.getActionsByType(getName(), null);
            if (aa != null) actions.addAll(aa);
        }
        
        if (actions.size() == 0) return;
        
        Collections.sort(actions);
    }
    //</editor-fold>
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void setSelectedIndex(int index) {
        this.oldIndex = getSelectedIndex();
        super.setSelectedIndex(index);
    }
    
    
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    
    public boolean isDynamic() { return dynamic; }
    public void setDynamic(boolean dynamic) { this.dynamic = dynamic; }
    
    
}
