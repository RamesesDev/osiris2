import com.rameses.rcp.ui.ControlProperty;
import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.Field;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Node;

/**
 *
 * @author compaq
 */
public class ControlPropertySheet extends JPanel {
    
    private PropertySheet sheet = new PropertySheet();
    private ControlProperty property = new ControlProperty();
    
    public ControlPropertySheet() {
        setLayout(new BorderLayout());
        
        try {
            Node[] nodes = new Node[]{
                new BeanNode(property)
            };
            
            sheet.setNodes(nodes);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        add(sheet, BorderLayout.CENTER);
        
        try {
            UIManager.put("Tree.altbackground", Color.LIGHT_GRAY);
            
            Field field = sheet.getClass().getDeclaredField("table");
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            
            JTable table = (JTable) field.get(sheet);
            table.setShowGrid(true);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);
            table.setGridColor(Color.LIGHT_GRAY);
            
            field.setAccessible(accessible);
        } catch(Exception ign){
            ign.printStackTrace();
        }
    }
    
    public void showProperty() 
    {
        StringBuffer sb = new StringBuffer();
        sb.append("new " + ControlProperty.class.getName() + "() {{\n "); 
        sb.append("   setCaption(\"" + property.getCaption() + "\");\n "); 
        sb.append("   setIndex("+ property.getIndex() +");\n "); 
        sb.append("   setRequired("+ property.isRequired() +");\n "); 
        sb.append("   setCaptionWidth("+ property.getCaptionWidth() +");\n "); 
        sb.append("}}");
        System.out.println(sb);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                try {
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                } catch (Exception e) {;}
                
                ControlPropertySheet sheet = new ControlPropertySheet();
                
                JDialog d = new JDialog();
                d.setModal(true);
                d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                d.setContentPane(sheet);
                d.pack();
                d.setVisible(true);
                
                sheet.showProperty();
            }
        });
    }
    
}
