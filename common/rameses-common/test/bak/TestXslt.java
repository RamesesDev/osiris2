/*
 * TestXslt.java
 * JUnit based test
 *
 * Created on April 21, 2012, 9:00 AM
 */

package bak;

import com.rameses.io.StreamUtil;
import com.rameses.util.TemplateProvider;
import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class TestXslt extends TestCase {
    
    public TestXslt(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        String s = StreamUtil.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/templates/source.xml") );
        TemplateProvider.getInstance().transform( "META-INF/templates/sample.xsl",s,System.out  );
    }
    
}
