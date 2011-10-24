import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import junit.framework.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * SimpleJson.java
 * JUnit based test
 *
 * Created on September 7, 2011, 5:39 PM
 */

/**
 *
 * @author jzamss
 */
public class SimpleJson extends TestCase {
    
    public SimpleJson(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestHello1() throws Exception {
        URL u = new URL("http://localhost:8080/classroom/json.jsp?id=2");
        StringBuilder b  = new StringBuilder();
        int i = 0;
        InputStream is = u.openStream();
        while( (i=is.read())!=-1) {
            b.append((char)i);
        }
        JSONObject jo = JSONObject.fromObject(b.toString());
        System.out.println( jo.getString("firstname") );
        System.out.println( jo.getString("lastname") );
        JSONArray arr = jo.getJSONArray("items");
        Iterator iter = arr.iterator();
        while(iter.hasNext()) {
            JSONObject item = (JSONObject)iter.next();
            System.out.println(item.getString("phone"));
        }
    }
    
    public void testImage() throws Exception {
        URL u = new URL("http://localhost:8080/classroom/img/biglogo.png");
        HttpURLConnection hurl = (HttpURLConnection)u.openConnection();
        System.out.println(hurl.getContentType());
        InputStream is = hurl.getInputStream();
        int i = 0;
        while( (i=is.read())!=-1) {
           System.out.println(i);
        }
        
        //while( (i=is.read())!=-1) {
        //    b.append((char)i);
        //}
    }
    
}
