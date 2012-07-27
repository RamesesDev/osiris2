/*
 * JarTest.java
 * JUnit based test
 *
 * Created on June 2, 2012, 8:43 PM
 */

package cms;

import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class JarTest extends TestCase {
    
    public JarTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        /*
        JarURLConnection conn = ( JarURLConnection)(new URL("jar:file:/c:/gazeebu/etracs-theme.jar!/")).openConnection();
        JarFile jarfile = conn.getJarFile();
        Enumeration en = jarfile.entries();
        while(en.hasMoreElements()) {
            System.out.println(en.nextElement());
        }
        
        InputStream is = (new URL("jar:file:/c:/gazeebu/etracs-theme.jar!/masters/default")).openStream();
        System.out.println(StreamUtil.toString(is));
        */
        /*
        URLDirectory u = new URLDirectory(new URL("jar:file:/c:/gazeebu/etracs-theme.jar!/"));
        URL[] ulist = u.list( null  );
        for( URL z : ulist ) {
            System.out.println(z);
        }
         */
        
    }
    
}
