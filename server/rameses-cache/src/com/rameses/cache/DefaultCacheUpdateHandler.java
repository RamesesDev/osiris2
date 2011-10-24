/*
 * DefaultCacheUpdateHandler.java
 * Created on September 30, 2011, 11:04 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class DefaultCacheUpdateHandler implements CacheUpdateHandler {
    
    
    public String getMode() {
        return "default";
    }
    
    /***
     * @param cacheId = the id of the cache info
     * @param cache = cached object
     * @param updateInfo = These can be as follows: <br>
     *      String = represents the closure statement. ex: { o-> o.name = 'jess' }
     *      CacheUpdateParam = request passed as a param: ex: new CacheUpdateParam("{o,x->o.state=x.state}", [state:'hello'] );
     *      Object[] = first argument represents the statement, 2nd argument the data
     *      List = first argument represents the statement, 2nd argument the data
     *      Map = must have two keys : statement and data
     */
    
    public void update(String cacheId, Object cache, Object updateInfo) {
        GroovyShell shell = new GroovyShell();
        try {
            if( updateInfo instanceof String ) {
                String statement = (String)updateInfo;
                Closure closure = (Closure)shell.evaluate( statement );
                closure.call( new Object[]{cache} );
            } else if( updateInfo instanceof CacheUpdateParam) {
                CacheUpdateParam p = (CacheUpdateParam)updateInfo;
                Closure closure = (Closure)shell.evaluate( p.getStatement() );
                closure.call( new Object[]{cache, p.getData()} );
            } else if( updateInfo instanceof Object[] ) {
                Object[] p = (Object[])updateInfo;
                String statement = (String)p[0];
                Object values = p[1];
                Closure closure = (Closure)shell.evaluate( statement );
                closure.call( new Object[]{cache, values} );
            } else if( updateInfo instanceof List) {
                List list = (List)updateInfo;
                String statement = (String)list.get(0);
                Object values = list.get(1);
                Closure closure = (Closure)shell.evaluate( statement );
                closure.call( new Object[]{cache, values} );
            } else if( updateInfo instanceof Map) {
                Map map = (Map)updateInfo;
                String statement = (String)map.get("statement");
                Object data = map.get("data");
                if(statement==null  || data == null )
                    throw new Exception("statement and data fields must be provided");
                Closure closure = (Closure)shell.evaluate( statement );
                closure.call( new Object[]{cache, data} );
            } else {
                throw new Exception("Illegal updateInfo object. Type must be CacheUpdateParam, Object[] or List");
            }
        } catch(Exception e) {
            System.out.println("CACHE UPDATE FAILED."+e.getMessage());
        }
        finally {
            shell = null;
        }
    }
    
}
