import com.rameses.cms.PageInstance;
import com.rameses.cms.Project;
import com.rameses.cms.impl.DefaultProjectFactory;
import junit.framework.*;
/*
 * File2Test.java
 * JUnit based test
 *
 * Created on June 19, 2012, 2:34 PM
 */

/**
 *
 * @author Elmo
 */
public class TestGetPage extends TestCase {
    
    public TestGetPage(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        DefaultProjectFactory dfactory = new DefaultProjectFactory();
        Project project = dfactory.create( "etracs", "file:///c:/cms_demo/projects/etracs" );
        PageInstance pageInstance = project.getFileManager().getPageManager().getPage( "/zzblogs" );
        System.out.println(pageInstance.getContent());
        
        /*
       
        HttpServiceHandler.MyTest o = (HttpServiceHandler.MyTest)project.getServiceManager().create("http/MyService");
        System.out.println(o.getList());
        */
        
         //Object o = project.getServiceManager().findInterface("IContentService");
         //System.out.println("object is " + o);
    }
    
  
    
}
