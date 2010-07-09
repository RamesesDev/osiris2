/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on July 24, 2009, 12:55 PM
 */

package test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        String pa = "http://rameses.data/EJBService/stype";
        Pattern p = Pattern.compile("#\\{.*?\\}");
        Map map = new HashMap();
        map.put("rameses.data", "nabunturan");
        map.put( "stype", "local");
        Matcher m = p.matcher(pa);
        StringBuffer sb = new StringBuffer();
        int start = 0;
        while(m.find()) {
            System.out.println( "start " + m.start() + " end " + m.end() );
            String s = m.group().substring(2, m.group().length()-1);
            sb.append( pa.substring(start, m.start()) );
            sb.append( map.get(s) );
            start = m.end();
        }
        sb.append( pa.substring(start));
        System.out.println(sb.toString());
    }

}
