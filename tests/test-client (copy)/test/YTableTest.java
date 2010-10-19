import com.rameses.rcp.control.XTextField;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import test.control.*;

/**
 *
 * @author compaq
 */
public class YTableTest extends JTable implements UIControl {
    
    private String[] depends;
    private Binding binding;
    private int index;
    private DefaultModel model;
    private Map editors = new HashMap();
    
    public YTableTest() {
        model = new DefaultModel();
        model.addColumn(new DefaultColumn("name", "Name", false));
        model.addColumn(new DefaultColumn("address", "Address", true));
        model.addColumn(new DefaultColumn("gender", "Gender", true));
        model.addColumn(new DefaultColumn("working", "Working", true));
        
        List<Map> data = new ArrayList();
        data.add(createData("Jayrome", "Lahug, CC", true));
        data.add(createData("Windhel", "Labangon, CC", false));
        data.add(createData("Dandee", "Basak, CC, CC", false));
        
        model.setData(data);
        setModel(model);
        //setCellEditor(new EditorManager());
    }
    
    private Map createData(String name, String address, boolean working) {
        Map m = new HashMap();
        m.put("name", name);
        m.put("address", address);
        m.put("working", working);
        return m;
    }
    
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public void refresh() {
    }
    
    public void load() {
    }
    
    public int compareTo(Object o) {
        if ( o == null || !(o instanceof UIControl) ) return 0;
        return this.index = ((UIControl) o).getIndex();
    }
    
    
    
    //<editor-fold defaultstate="collapsed" desc=" DefaultModel ">
    private class DefaultModel extends AbstractTableModel {
        
        private List<Map> rowList = new ArrayList();
        private List<DefaultColumn> columnList = new ArrayList();
        
        public int getRowCount() {
            return rowList.size();
        }
        
        public DefaultColumn getDefaultColumn(int index) {
            if (index >= 0 && index < columnList.size())
                return columnList.get(index);
            
            return null;
        }
        
        public int getColumnCount() {
            return columnList.size();
        }
        
        public void addColumn(DefaultColumn col) {
            columnList.add(col);
        }
        
        public void setData(List<Map> dataList) {
            this.rowList = dataList;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            Map data = rowList.get(rowIndex);
            return data.get(columnList.get(columnIndex).getName());
        }
        
        public String getColumnName(int column) {
            return columnList.get(column).getCaption();
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            DefaultColumn dc = getDefaultColumn(columnIndex);
            if ( dc != null ) return dc.isEditable();
            return false;
        }
        
        public Class getColumnClass(int index) {
            return columnList.get(index).getDataType();
        }
        
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Map data = rowList.get(rowIndex);
            data.put(columnList.get(columnIndex).getName(), aValue);
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" DefaultColumn ">
    public static class DefaultColumn {
        
        private String name;
        private String caption;
        private boolean editable;
        private Class dataType = String.class;
        
        public DefaultColumn() {;}
        
        public DefaultColumn(String name, String caption, boolean editable) {
            this.caption = caption;
            this.name = name;
            this.editable = editable;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getCaption() {
            return caption;
        }
        
        public void setCaption(String caption) {
            this.caption = caption;
        }
        
        public boolean isEditable() {
            return editable;
        }
        
        public void setEditable(boolean editable) {
            this.editable = editable;
        }
        
        public Class getDataType() {
            return dataType;
        }
        
        public void setDataType(Class dataType) {
            this.dataType = dataType;
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" EditorManager ">
    public class EditorManager extends AbstractCellEditor implements TableCellEditor {
        
        private XTextField editor = new XTextField();
        
        public EditorManager() {
            editor.setBorder(BorderFactory.createEmptyBorder(0,1,0,1));
        }
        
        public Object getCellEditorValue() {
            return editor.getValue();
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return editor;
        }
        
    }
    //</editor-fold>
}
