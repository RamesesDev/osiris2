/*
 * FileDirTest.java
 * JUnit based test
 *
 * Created on July 19, 2012, 10:25 AM
 */

package anubis.test;

import com.rameses.anubis.FileDir;
import com.rameses.anubis.FileDir.FileFilter;
import com.rameses.io.StreamUtil;
import java.net.URL;
import java.net.URLConnection;
import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class FileDirTest extends TestCase {
    
    public FileDirTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestHello() {
        //String path = "file:///c:/anubis_cms/gazeebu/classroom/files/";
        String path = "http://10.0.0.102:8080/jetty-demo/";
        FileDir.scan(path, new FileFilter(){
            public void handle(FileDir.FileInfo f) {
                URL u = f.getSubfile( "module.conf" );
                if(u!=null) {
                    System.out.println( u.toString() );
                }
            }
        });
    }
    
    public void testPath() throws Exception {
        //String path = "http://10.0.0.102:8080/jetty-demo/files";
        String path = "http://10.0.0.152/tests/etracs2-web-portal/files";
        //String path = "http://10.0.0.152:50019/resources";
        URL url = new URL(path);
        URLConnection conn = url.openConnection();
        System.out.println(conn);
        String s = StreamUtil.toString(conn.getInputStream());
        System.out.println(s);
        /*
        Pattern pattern = Pattern.compile("<a href=\".*?\".*?>.*?</a>");
        Matcher matcher = pattern.matcher(s);
        while(matcher.find()){
            System.out.println(s.substring(matcher.start(),matcher.end()));
        }
        
        System.out.println("filename map->"+StreamUtil.toString(conn.getInputStream()));
         */
    }
    
}
