/*
 * UrlTest.java
 * JUnit based test
 *
 * Created on July 22, 2012, 8:44 PM
 */

package anubis.test;

import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class UrlTest extends TestCase {
    
    public UrlTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        /*
        NameParser np = new NameParser("/products/[category]/[product]", "product/page");
        System.out.println("tokens are->"+np.getTokens());
        MatchResult mr = np.buildResult("/products/HomeProducts/Product1");
        System.out.println(mr.getTokens());
        System.out.println(mr.getResolvedPath());
         */
        String searchMe = "Green Eggs and Ham";
        String findMe = "Eggs but ham";
        int len = findMe.length();
        boolean foundIt = false;
        int i = 0;
        while (!searchMe.regionMatches(i, findMe, 0, len)) {
            i++;
            System.out.println(i);
            foundIt = true;
        }
        if (foundIt) {
            System.out.println(searchMe.substring(i, i+len));
        }
    }
    
}
