/*
 * KeySpecTest.java
 * JUnit based test
 *
 * Created on November 25, 2010, 5:48 PM
 */

package research;

import com.rameses.util.Encoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class KeySpecTest extends TestCase {
    
    public KeySpecTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception{
        String key = "mytest";
        String pwd = "mypass";
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(skey);
        byte[] bytes = mac.doFinal(pwd.getBytes());
        System.out.println( Encoder.MD5.toHexString(bytes) );
        System.out.println( Encoder.MD5.encode( pwd, key ) );
    }
    
     public void testHello2() throws Exception{
        String key = "mytest";
        String pwd = "mypass";
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(skey);
        byte[] bytes = mac.doFinal(pwd.getBytes());
        System.out.println( Encoder.SHA1.toHexString(bytes) );
        System.out.println( Encoder.SHA1.encode( pwd, key ));
    }

}
