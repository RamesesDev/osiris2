/*
 * TestLoader.java
 * JUnit based test
 *
 * Created on July 19, 2012, 8:37 AM
 */

package anubis.test;

import com.rameses.anubis.FileDir.FileInfo;
import com.rameses.util.URLDirectory;
import com.rameses.util.URLDirectory.URLFilter;
import java.io.InputStream;
import java.net.URL;
import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class TestLoader extends TestCase {
    
    public TestLoader(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void dtestHello() throws Exception {
        URLDirectory ud = new URLDirectory(new URL("file:///c:/anubis_cms/gazeebu/classroom/modules"));
        URL[] files = ud.list(new URLFilter(){
            public boolean accept(URL u, String filter) {
                InputStream is = null;
                try {
                    System.out.println("filter is ->"+filter);
                    URL uu = new URL(u.toString()+"/module.conf");
                    is = uu.openStream();
                    return true;
                }
                catch(Exception e){
                    return false;
                }
                finally{
                    try { is.close(); } catch(Exception ign){;}
                }
            }
        });
        for(URL u: files) {
            System.out.println(u.toString());
        }
    }

    public void testMe() throws Exception  {
        String t = "the/cat/is/brown/as/a/bear";
        System.out.println(t.substring(t.lastIndexOf("/")));
        FileInfo f = new FileInfo(new URL("file:///c:/anubis_cms/gazeebu/classroom/project.conf"));
        System.out.println(f.getFileName());
        System.out.println(f.getParentPath());
        System.out.println(f.getExt());
    }
    
    
}
