/*
 * TestTokenizer.java
 * JUnit based test
 *
 * Created on May 15, 2012, 2:34 PM
 */

package bak;

import java.util.Stack;
import java.util.StringTokenizer;
import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class TestTokenizer extends TestCase {
    
    public TestTokenizer(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        int start = 2;
        int limit = 3;
        String ids = "x.objid";
        
        int STATE_SELECT = 0;
        int STATE_COLUMNS = 1;
        int STATE_FROM = 2;
        int STATE_WHERE = 3;
        int STATE_ORDER = 4;
        
        String sql = "select x.name ";
        sql += " from (select * from Payer p where p.objid is not null ) as x where x.name  is not null and x.objid not in (select * from data) order by x.name  ";
        
        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder fromBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        StringBuilder orderBuilder = new StringBuilder();
        
        StringBuilder currentBuilder = null;
        Stack stack = new Stack();
        int currentState = STATE_SELECT;
        
        StringTokenizer st = new StringTokenizer(sql.trim());
        while(st.hasMoreElements()) {
            String s = (String)st.nextElement(); 
            if( s.equalsIgnoreCase("select") && currentState <= STATE_SELECT  ) {
                selectBuilder.append( s  );
                currentBuilder = columnBuilder;
                currentState = STATE_COLUMNS;
            }
            else if( s.equalsIgnoreCase("from") && currentState == STATE_COLUMNS && stack.empty()  ) {
                currentBuilder = fromBuilder;
                currentBuilder.append( " " + s );
                currentState = STATE_FROM;
            } 
            else if( s.equalsIgnoreCase("where") && currentState == STATE_FROM && stack.empty()) {
                currentBuilder = whereBuilder;
                currentBuilder.append( " " + s );
                currentState = STATE_WHERE;
            }
            else if( s.equalsIgnoreCase("order") && currentState == STATE_WHERE && stack.empty() ) {
                currentBuilder = orderBuilder;
                currentBuilder.append( " " + s );
                currentState = STATE_ORDER;
            }
            else if(s.equals("(") || s.trim().startsWith("(") || s.trim().endsWith("(")) {
                stack.push(true);
                currentBuilder.append( " " + s );
            }
            else if(s.equals(")") || s.trim().startsWith(")") || s.trim().endsWith(")")) {
                stack.pop();
                currentBuilder.append( " " + s );
            }
            else {
                currentBuilder.append( " " + s );
            }
        }
        
        
        System.out.println(selectBuilder.toString());
        System.out.println( " top " + limit + " ");
        System.out.println(columnBuilder.toString());
        System.out.println(fromBuilder.toString());
        if( whereBuilder.length() == 0 ) {
            System.out.println(" where ");
        }
        else {
            System.out.println(whereBuilder.toString());
            System.out.println(" and ");
        }
        System.out.println( ids + " not in ");
        
        System.out.println("( select top " +  (start*limit) + " " + ids + " ");
        System.out.println( fromBuilder.toString());
        System.out.println(whereBuilder.toString());
        System.out.println(orderBuilder.toString());
        System.out.println(")");
        System.out.println(orderBuilder.toString());
    }

}
