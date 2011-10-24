/*
 * JsonTest.java
 * JUnit based test
 *
 * Created on October 3, 2011, 5:24 PM
 */

package test;

import com.rameses.server.common.JsonUtil;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceContext;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class JsonTest extends TestCase {
    
    public JsonTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    private interface SessionService {
        Map getInfo(String id);
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        Map map = new HashMap();
        map.put("app.host", "localhost:8080");
        map.put("app.context", "gazeebu-classroom");
        map.put(ServiceContext.USE_DEFAULT, true);
        ScriptServiceContext cx = new ScriptServiceContext(map);
        SessionService svc = cx.create("SessionService", SessionService.class);
        Map info = svc.getInfo("T3LCM3:-11ae15e5:132c9124798:-7ffe");
        System.out.println(info);
        System.out.println(JsonUtil.toString(info));
        /*
        Map map = new HashMap();
        map.put("birthdate", new Date());
        //map.put("firstname", "ELMO");
        System.out.println(JsonUtil.toString(map));
         */
    }

}
