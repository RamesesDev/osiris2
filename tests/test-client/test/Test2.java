import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.classutils.ClassDef;
import com.rameses.rcp.annotations.Service;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import junit.framework.*;

/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on June 24, 2010, 1:13 PM
 */

/**
 *
 * @author compaq
 */
public class Test2 extends TestCase {
    
    public Test2(String testName) {
        super(testName);
    }
    
    public void testHello() throws Exception {
        SampleClass s = new SampleClass();
        ClassDef d = new ClassDef(s.getClass());
        d.setHandler(new FieldHandler());
        d.injectFields(s);
        
        System.out.println("service is " + s.getService());
    }
    
    private class FieldHandler implements AnnotationFieldHandler {
        
        public Object getResource(Field f, Annotation a) throws Exception {
            if ( a.annotationType() == Service.class ) {
                System.out.println("service class is annotated..");
                return "hello";
            }
            return null;
        }
    
    }
    
    private class SampleClass {
    
        @Service("Service")
        private Object service;
        
        public void run() {
        
        }
        
        public Object getService() {
            return service;
        }
        
    }
    
}
