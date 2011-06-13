import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.*;
/*
 * Test1.java
 * JUnit based test
 *
 * Created on January 29, 2011, 9:39 AM
 */

/**
 *
 * @author ms
 */
public class Test1 extends TestCase {
    
    public Test1(String testName) {
        super(testName);
    }
    
    public void testHello() throws Exception {
        String response = getProcessResponse("ipconfig /all");
        StringTokenizer tokenizer = new StringTokenizer(response, "\n");
        
        int counter = 1;
        boolean skipMac = false;
        Pattern macPattern = Pattern.compile("(?:\\w{2}-){5}\\w{2}");
        while(tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();
            
            if ( line.toLowerCase().contains("disconnected") ) skipMac = true;

            Matcher m = macPattern.matcher(line);
            if( m.find() ) {
                if( !skipMac ) {
                    System.out.println( m.group() );
                    //break;
                }
                skipMac = false;
            }
        }
    }
    
    private static String getProcessResponse(String command) throws Exception
    {
        Process p = Runtime.getRuntime().exec(command);
        InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
        StringBuffer buffer= new StringBuffer();
        for (;;) {
            int c = stdoutStream.read();
            if (c == -1) break;
            buffer.append((char)c);
        }
        
        String outputText = buffer.toString();
        stdoutStream.close();
        return outputText;
    }
    
}
