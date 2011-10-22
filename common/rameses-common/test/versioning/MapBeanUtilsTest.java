/*
 * VersionTest.java
 * JUnit based test
 *
 * Created on September 29, 2011, 8:22 AM
 */

package versioning;


import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class MapBeanUtilsTest extends TestCase {
    
    public MapBeanUtilsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
//    public void testDiff() throws Exception {
//        MyHandler h = new MyHandler();
//        ChangeQueue cq = new ChangeQueue(h);
//        //cq.push( "firstname", "elmo");
//        //cq.push( "lastname", "nazareno");
//        cq.push( "address.province.name", "cebu");
//        cq.push( "address.province.telephone", "116578");
//        cq.applyChanges();
//        while(cq.isUpdating()){;}
//        System.out.println(h.getMap().toString());
//    }
//    
//
//    private class MyHandler implements ChangeQueue.UpdateHandler {
//        private Map map = new HashMap();
//        
//        public void apply(String propertyName, Object changeValue) {
//            MapBeanUtils.setProperty(map, propertyName, changeValue);
//        }
//
//        public Map getMap() {
//            return map;
//        }
//        
//    }
//    
}
