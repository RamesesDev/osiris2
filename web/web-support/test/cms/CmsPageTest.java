/*
 * CmsPageTest.java
 * JUnit based test
 *
 * Created on May 22, 2012, 9:35 AM
 */

package cms;

import com.rameses.web.cms.CmsConf;
import com.rameses.web.cms.CmsPageFactory;

import com.rameses.web.cms.UrlPageResourceProvider;
import com.rameses.web.cms.PageInfo;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class CmsPageTest extends TestCase {
    
    public CmsPageTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testLoadPage() {
        Map info = new HashMap();
        String client = "theme2.jar!";
        info.put("name",client );
        info.put("logo",client+".jpg" );
        
        CmsConf conf = new CmsConf();
        conf.setAppInfo(info);
        //conf.setPageResourceProvider( new UrlPageResourceProvider("file:///c:/gazeebu", client) );
        conf.setPageResourceProvider( new UrlPageResourceProvider("jar:file:/c:/gazeebu/", client) );
        PageInfo p = CmsPageFactory.getPage( "test", new HashMap(), conf );
        System.out.println("==============================================");
        System.out.println(p.getContent());
    }

}
