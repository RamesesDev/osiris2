import com.rameses.cms.AbstractFile;
import com.rameses.cms.Folder;
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
public class File2Test extends TestCase {
    
    public File2Test(String testName) {
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
        
        String testPath = "/blogs/2012/01/01/my-first-article";
        String testPath1 = "/about/history";
        PageInstance pageInstance = project.getFileManager().getPageManager().getPage( testPath );
        System.out.println("*******************************************");
        System.out.println( pageInstance.getContent() );
        Folder f = (Folder)project.getFileManager().getPageManager().getFolder("/about1");
        System.out.println(f.getFilepath());
        System.out.println("after loading ---->"+pageInstance.getPage());
        for(Object o : f.getItems()) {
            System.out.println( ((AbstractFile) o).getFilepath());
        }
        /*
       
        HttpServiceHandler.MyTest o = (HttpServiceHandler.MyTest)project.getServiceManager().create("http/MyService");
        System.out.println(o.getList());
        */
        
         //Object o = project.getServiceManager().findInterface("IContentService");
         //System.out.println("object is " + o);
    }
    
}
