/*
 * IOTest.java
 * JUnit based test
 *
 * Created on July 20, 2010, 7:28 AM
 */

package test;

import com.rameses.util.MapEditor;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class MapEditorTest extends TestCase {
    
    
    public MapEditorTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void displayContents(Map data) {
        for(Object o : data.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            System.out.println(me.getKey()+"="+me.getValue());
        }
    }
    
    public void testBasicMapEditor() {
        Map data = new HashMap();
        data.put("firstname", "elmo");
        data.put("lastname", "nazareno");
        data.put("address", null);
        MapEditor dataEditor = new MapEditor(data);
        
        //test original state
        assertEquals(data.size(),3);
        
        //test no updates
        dataEditor.put( "firstname", "elmo" );
        dataEditor.put( "lastname", "nazareno" );
        dataEditor.put( "address", null );
        assertEquals(data.size(),3);

        //test only one change updated.
        //test change lastname
        dataEditor.put( "lastname", "nazareno1" );
        assertEquals(data.size(),4);
        assertTrue( data.containsKey("_lastname"));
        
        //test change address with the same value.
        dataEditor.put( "lastname", "nazareno1" );
        assertEquals(data.size(),4);
        
        //test change address with the original value.
        dataEditor.put( "lastname", "nazareno" );
        assertEquals(data.size(),3);
        assertFalse( data.containsKey("_lastname"));
        
        //update address. original is null
        assertTrue( data.containsKey("address") );
        assertEquals( data.get("address"), null );
        assertFalse( data.containsKey("_address") );
        dataEditor.put( "address", "address1" );
        assertEquals( data.get("address"), "address1" );
        assertTrue( data.containsKey("_address") );
        assertEquals( data.get("_address"), null );
        assertEquals(data.size(),4);
        
        //update address value several times. original msut not change        
        dataEditor.put( "address", "address2" );
        dataEditor.put( "address", "address3" );
        dataEditor.put( "address", "address4" );
        dataEditor.put( "address", "address5" );
        assertEquals(data.size(),4);
        assertTrue( data.containsKey("_address"));
        assertEquals( data.get("_address"), null );

        //update address with original value.
        dataEditor.put( "address", null );
        assertEquals(data.size(),3);
        assertFalse( data.containsKey("_address"));
        assertEquals( data.get("address"), null );
        
        
        dataEditor.put( "firstname", "elmox" );
        dataEditor.put( "lastname", "nazareno" );
        dataEditor.put( "lastname", "nazareno 1" );        
        dataEditor.put( "address", "capitol" );
        dataEditor.put( "address", null );
        dataEditor.put( "lastname", null );
        dataEditor.put( "lastname", "nazareno" );
        
        Map changes = dataEditor.changes();
        displayContents(changes);
    }
    
    public void testEditorItems() {
        Map map = new HashMap();
        
        
        
        
    }
    
}

