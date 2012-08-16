/*
 * NameParserTest.java
 * JUnit based test
 *
 * Created on July 16, 2012, 11:43 AM
 */

package anubis.test;

import com.rameses.anubis.NameParser;
import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class NameParserTest extends TestCase {
    
    public NameParserTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        NameParser np  = new NameParser("[project].gazeebu.com", "file://gazeebu.com/[project]");
        System.out.println(np.getPattern());
        String path = "classroom.gazeebu.com";
        System.out.println("compare ->" + path);
        System.out.println("pattern ->" + np.getPattern());
        System.out.println("matches?" + np.matches( path ));
        //System.out.println( np.getTokens( path) );
        //System.out.println(np.getResolvedTargetPath( path ));
    }
    
    //this should match xxx.gazeebu.org
    public void xtestPattern1() {
        String pattern = "([^\\.]|^www)*?\\.gazeebu\\.org";
        assertTrue( "www.gazeebu.org".matches(pattern) );
        assertFalse( "www.etracs.gazeebu.org".matches(pattern) );
        assertFalse( "gazeebu.org".matches(pattern) );
        assertFalse( "www.gazeebu.org".matches(pattern) );
        assertTrue( "etracs.gazeebu.org".matches(pattern) );
    }
    
    private String[] getData() {
        String s[] = new String[9];
        s[0] = "gazeebu.org";
        s[1] = "www.gazeebu.org";
        s[2] = "en.classroom.gazeebu.org";
        s[3] = "de.classroom.gazeebu.org";
        s[4] = "classroom.gazeebu.org";
        s[5] = "en.gazeebu.org";   
        s[6] = "ww.etracs.org";  
        s[7] = "school-org.gazeebu.org";
        s[8] = "school_org.gazeebu.org";
        return s;
    }
    
    public void ztestPattern2() {
        String[] s = getData();
        //String pattern = "^([\\w|-|_]+)?\\.gazeebu.org$";
        //String pattern = "^www.gazeebu.org$";
        String pattern = "^([\\w|-]+)?\\.([\\w|-]+)?\\.gazeebu.org$";
        for(int i=0; i<s.length;i++ ){
            System.out.println(i+" matches?"+s[i].matches(pattern));
        }
    }
}
