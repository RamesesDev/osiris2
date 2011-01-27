/*
 * XTabbedPane.java
 *
 * Created on October 1, 2010, 4:50 PM
 * @author jaycverg
 */

package Templates.Classes;

import com.rameses.rcp.common.Opener;
import com.rameses.rcp.control.XSubFormPanel;
import com.rameses.rcp.control.tabbedpane.LoadingPanel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.OpenerProvider;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.support.ThemeUI;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.Component;
import java.awt.Font;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class XTabbedPane extends JTabbedPane implements UIControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean dynamic;
    
    private int oldIndex;
    private List<Opener> openers = new ArrayList();
    private boolean nameAutoLookupAsOpener = true;
    
    
    public XTabbedPane() {
        super();
        initComponents();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initComponents  ">
    private void initComponents() {
        if ( Beans.isDesignTime() ) {
            addTab("Tab 1", new JPanel());
        }
        
        Font f = ThemeUI.getFont("XTabbedPane.font");
        if ( f != null ) setFont( f );
        
    }
    //</editor-fold>
    
    public void refresh() {
        if ( dynamic ) {
            loadTabs();
        }
    }
    
    public void load() {
        if ( !dynamic ) {
            loadTabs();
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void loadTabs() {
        loadOpeners();
        removeAll();
        if ( openers.size() > 0 ) {
            for (Opener o: openers) {
                super.addTab(o.getCaption(), getOpenerIcon(o), new LoadingPanel());
            }
            setSelectedIndex(0);
        }
    }
    
    private Icon getOpenerIcon(Opener o) {
        if ( o.getProperties().get("icon") != null ) {
            return ControlSupport.getImageIcon(o.getProperties().get("icon")+"");
        }
        return null;
    }
    
    private void loadOpeners() {
        openers.clear();
        
        if( nameAutoLookupAsOpener ) {
            //--get openers defined from the opener provider
            OpenerProvider openerProvider = ClientContext.getCurrentContext().getOpenerProvider();
            if (openerProvider != null) {
                UIController controller = binding.getController();
                List<Opener> oo = openerProvider.getOpeners(getName(), null);
                if (oo != null) openers.addAll(oo);
            }
        } else {
            //--get openers defined from the code bean
            Object value = null;
            try {
                value = UIControlUtil.getBeanValue(this);
            } catch(Exception e) {;}
            
            if (value == null) {
                //do nothing
            } else if (value.getClass().isArray()) {
                for (Opener o: (Opener[]) value) {
                    openers.add(o);
                }
            } else if (value instanceof Collection) {
                openers.addAll((Collection) value);
            }
        }
        
    }
    //</editor-fold>
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void setSelectedIndex(int index) {
        Component c = getComponentAt(index);
        if ( c instanceof LoadingPanel ) {
            JPanel p = (JPanel) c;
            if ( p.getClientProperty("BINDED") == null ) {
                new Thread(new BindingRunnable(index, openers.get(index))).start();
                p.putClientProperty("BINDED", true);
            }
        }
        
        this.oldIndex = getSelectedIndex();
        super.setSelectedIndex(index);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    
    public boolean isDynamic() { return dynamic; }
    public void setDynamic(boolean dynamic) { this.dynamic = dynamic; }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  BindingRunnable  ">
    private class BindingRunnable implements Runnable {
        
        private int index;
        private Opener opener;
        
        BindingRunnable(int index, Opener opener) {
            this.index = index;
            this.opener = opener;
        }
        
        public void run() {
            try {
                XSubFormPanel xsf = new XSubFormPanel(opener);
                xsf.setBinding(binding);
                xsf.load();
                XTabbedPane.this.setComponentAt(index, xsf);
                
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    //</editor-fold>
    
    public boolean isNameAutoLookupAsOpener() {
        return nameAutoLookupAsOpener;
    }
    
    public void setNameAutoLookupAsOpener(boolean nameAutoLookupAsOpener) {
        this.nameAutoLookupAsOpener = nameAutoLookupAsOpener;
    }
    
}
