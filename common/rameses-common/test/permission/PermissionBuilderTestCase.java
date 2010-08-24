/*
 * PermissionBuilderTestCase.java
 * JUnit based test
 *
 * Created on August 18, 2010, 10:33 AM
 */

package permission;

import com.rameses.util.PermissionBuilder;
import com.rameses.util.PermissionBuilder.Permission;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class PermissionBuilderTestCase extends TestCase {
    
    public PermissionBuilderTestCase(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testBuildPermission() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream("permission/kp");
        assertNotNull(is);
        
        PermissionBuilder b = new PermissionBuilder();
        List<Permission> list = b.createPermissions( "kp", is );
        assertEquals( 4, list.size() );
        
        for(Permission p : list ) System.out.println(p);
        
        //List<String> excludes = new ArrayList();
        //excludes.add
        
    }

    public void testExcludePermission() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream("permission/kp");
        assertNotNull(is);
        
        PermissionBuilder b = new PermissionBuilder();
        List<Permission> list = b.createPermissions( "kp", is );
        assertEquals( 4, list.size() );
        
        List<String> excludes = new ArrayList();
        excludes.add( "kp:payout.open");
        excludes.add( "kp:payout.create");
        
        b.loadPermissions( "BRANCH_OP", list, excludes );
        
        assertEquals( b.getAllPermissions().size(), 2 );
    }
    
    public void testPermissionSets() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream("permission/kp");
        assertNotNull(is);
        InputStream is2 = loader.getResourceAsStream("permission/kyc");
        assertNotNull(is);
        InputStream is3 = loader.getResourceAsStream("permission/hometeller");
        assertNotNull(is);
        
        PermissionBuilder b = new PermissionBuilder();
        b.loadPermissions( "BRANCH_OP", b.createPermissions( "kp", is ), null );
        b.loadPermissions( "COMPLIANCE", b.createPermissions( "kyc", is2 ), null );
        b.loadPermissions( "BRANCH_OP", b.createPermissions( "hometeller", is3 ), null );
        
        assertEquals( b.getPermissionSets().size(),2 );
        assertEquals( b.getPermissionSets().get("BRANCH_OP").size(),8 );
        assertEquals( b.getPermissionSets().get("COMPLIANCE").size(),6 );
        
        List<String> excludes = new ArrayList();
        excludes.add( "kp:payout.open");
        excludes.add( "kp:payout.create");
        
        List allowed = b.getFilteredPermissions( excludes );
        assertEquals( allowed.size(), 12 );
        
    }
    
}
