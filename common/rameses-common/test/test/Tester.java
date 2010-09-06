/*
 * Tester.java
 * JUnit based test
 *
 * Created on June 2, 2010, 2:26 PM
 */

package test;

import com.rameses.util.KeyGen;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class Tester extends TestCase {
    
    public Tester(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testSubstitute() throws Exception {
        //List list = new ArrayList();
        //list.add(0, "num1");
        //list.add(0, "num2");
        //System.out.println("array size " + list.size());
        DecimalFormat dn = new DecimalFormat("000000");
        System.out.println(dn.format(250));
        System.out.println("key->"+KeyGen.generateAlphanumKey("",3));
        //String tmp = "/a/b/c";
        //System.out.println( tmp.substring( tmp.indexOf("/", 1)+1) );
        
//        Map map = new HashMap();
//        map.put("firstname","elmo");
//        map.put("lastname","nazareno");
//
//        Map map2 = new HashMap();
//        map2.put("firstname","elmox");
//        map2.put("address","cebu city");
//
//        map2.putAll(map);
//        for(Object o: map2.entrySet()) {
//            Map.Entry me = (Map.Entry)o;
//            System.out.println(me.getKey()+"="+me.getValue());
//        }
//        assertEquals(map.get("firstname"), map2.get("firstname"));
        
        /*
        String c = "where cond = $P{cond}";
        Map map = new HashMap();
        map.put("condition", c);
        String sql = "select from o ${condition}";
        System.out.println( SqlUtil.substituteValues( sql, map ));
         */
    }
    
    public void xtestHashCode() {
        
        String s1 = "abcdefghijklmnopqrstuvwxyz";
        String s2 = "a12d34g56j78m9!p@#s$%v^&y*A";
        
        // compare hash codes
        System.out.println( "String 1: " + s1 + " s1.hashCode(): " + s1.hashCode() );
        System.out.println( "String 2: " + s2 + " s2.hashCode(): " + s2.hashCode() );
        System.out.println( "Equals: " + ( s1.equals( s2 ) ) + " HashCode equals: " + ( s1.hashCode() == s2.hashCode() ) );
    }
    
    
    public void xtestCode() {
        String f1 = "ABC_MT817777_000123X";
        String f2 = "ABC_MT817777_000124X";
        System.out.println("code1->" + f1.hashCode());
        System.out.println("code2->" + f2.hashCode());
    }
    
    public void testTimezone( ) {
        final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date d = new Date();
        String time = sdf.format(d);
        System.out.println("DEFAULT->" + time);

        for(String s: TimeZone.getAvailableIDs()) {
            TimeZone tz =  TimeZone.getTimeZone(s);
            sdf.setTimeZone(tz );
            System.out.println(s + "->" + sdf.format(d) + " off:" +tz.getRawOffset());
        }

    }
    
    public void testMatch() {
        String s = "#{cat}";
        String dref = s.replaceAll("#|\\{|\\}", "");
        assertEquals(dref,"cat");
    }
    
    
    public void testAddNumber() {
        String c = "cat";
        Date d = new Date();
        BigDecimal a = new BigDecimal("100");
        assertTrue( a instanceof Number );
    }
    
}
