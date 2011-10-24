/*
 * SimpleCache.java
 * JUnit based test
 *
 * Created on September 28, 2011, 11:51 AM
 */

package tests;

import com.rameses.cache.CacheServiceMBean;
import com.rameses.service.EJBServiceContext;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceContext;
import com.rameses.service.ServiceProxy;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class SimpleCache extends TestCase {
    
    private Map conf;
    
    public SimpleCache(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.conf = new HashMap();
        this.conf.put("app.host", "localhost:8080");
        this.conf.put("app.context", "gazeebu-classroom");
        this.conf.put(ServiceContext.USE_DEFAULT,true);
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testPut() throws Exception {
        EJBServiceContext ctx = new EJBServiceContext(this.conf);
        CacheServiceMBean mbean = ctx.create("CacheService", CacheServiceMBean.class );
        Map map = new HashMap();
        map.put("name", "elmo");
        Map conf = new HashMap();
        mbean.put("test",map,60000);
    }

    
    public void xtestUpdate() throws Exception {
        System.out.println("test update ->updating");
        Map localConf = new HashMap();
        localConf.put("app.context","gazeebu-classroom");
        localConf.put(ServiceContext.USE_DEFAULT,true);
        EJBServiceContext ctx = new EJBServiceContext(localConf);
        CacheServiceMBean mbean = ctx.create("CacheService", CacheServiceMBean.class );
        mbean.update("test", "{o->o.students=[];}"  );
        mbean.update("test", "{o->o.state='online'; }"  );
        mbean.update("test", "{o->o.students.add( [id:'S1', name:'jay'] ) }"  );
        mbean.update("test", "{o->o.students.add( [id:'S3', name:'jess'] ) }"  );
        
        Map p = new HashMap();
        p.put("state", "onlineX");
        
        mbean.update("test", new Object[]{"{o,v->o.students.find{it.id=='S3'}.state=v.state }",p}  );
        
    }

    public void testGet() throws Exception {
        Map localConf = new HashMap();
        localConf.put("app.context","gazeebu-classroom");
        localConf.put(ServiceContext.USE_DEFAULT,true);
        EJBServiceContext ctx = new EJBServiceContext(localConf);
        CacheServiceMBean mbean = ctx.create("CacheService", CacheServiceMBean.class );
        Object result = mbean.get("test");
        System.out.println("result is " + result);
    }
    
    public void xtestRemove() throws Exception {
        Map localConf = new HashMap();
        localConf.put("app.context","gazeebu-classroom");
        localConf.put(ServiceContext.USE_DEFAULT,true);
        EJBServiceContext ctx = new EJBServiceContext(localConf);
        CacheServiceMBean mbean = ctx.create("CacheService", CacheServiceMBean.class );
        mbean.remove("test");
    }
    
    public void xtestConnect() throws Exception {
        Map map = new HashMap();
        map.put(ServiceContext.USE_DEFAULT, true);
        map.put("app.context", "gazeebu-classroom");
        map.put("app.host", "localhost:8080");
        ScriptServiceContext ctx = new ScriptServiceContext(map);
        ServiceProxy proxy = ctx.create("CacheServiceHandler");
        proxy.invoke( "timeout", new Object[]{"id1", "This is new object"});
    }

}
