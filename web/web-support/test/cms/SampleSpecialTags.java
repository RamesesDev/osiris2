/*
 * SampleSpecialTags.java
 * JUnit based test
 *
 * Created on June 4, 2012, 7:45 AM
 */

package cms;

import com.rameses.util.TemplateSource;
import com.rameses.web.cms.CmsCache;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class SampleSpecialTags extends TestCase {
    
    public SampleSpecialTags(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        final String s = "the cat went running ${[field:'cat']} away";
        Map m = new ExtMap();
        
        m.put("data~myfield", "elmo shucks");
        m.put("myfield", "sampler");
        TemplateSource ts = new TemplateSource() {
            public InputStream getSource(String name) {
                return new ByteArrayInputStream(s.getBytes());
            }
        };
        String s1 = CmsCache.getInstance().getResult("uc", "index", m,ts );
        System.out.println(s1);
    }

    private class ExtMap extends HashMap {
        public Object get(Object key) {
            System.out.println(key);
            if(!super.containsKey(key)) return "";
            return super.get(key);
        }
    }
    
}
