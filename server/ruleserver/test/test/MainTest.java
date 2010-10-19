/*
 * MainTest.java
 * JUnit based test
 *
 * Created on September 14, 2010, 8:17 PM
 */

package test;

import com.rameses.rules.common.RuleAction;
import com.rameses.rules.common.RuleActionHandler;
import com.rameses.rules.common.RuleUtils;
import com.rameses.ruleserver.RuleMgmtBak;
import com.rameses.ruleserver.RuleService;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import junit.framework.*;
import org.apache.commons.beanutils.BeanUtils;
import org.drools.base.DefaultKnowledgeHelper;

/**
 *
 * @author elmo
 */
public class MainTest extends TestCase {
    
    public MainTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    
    public class EmailAction implements RuleActionHandler {
        public void execute(Object context, Object params) {
            System.out.println("context found is " + context);
            System.out.println("params is " + params);
        }
    }
    
    public class PrintAction implements RuleActionHandler {
        public void execute(Object context, Object params) {
            System.out.println("context found is " + context);
            DefaultKnowledgeHelper kh = (DefaultKnowledgeHelper)context;
            try {
                Object person = RuleUtils.createFact(kh , "org.Person");
                System.out.println("created new person "+ person);
            }
            catch(Exception ex) {
                System.out.println("ERROR LOADING " + ex.getMessage());
            }
            System.out.println("printing " + params);
        }
    }
    
    private void fire(RuleService ruleService) throws Exception {
        RuleAction p = new RuleAction();
        p.addCommand( "email", new EmailAction() );
        p.addCommand( "print", new PrintAction() );
        
        Object elmo = ruleService.createFact("test", "org.Person");
        BeanUtils.setProperty(elmo,"name", "elmo");
        Object alex = ruleService.createFact("test", "org.Person");
        BeanUtils.setProperty(alex,"name", "alex");
        
        Object addr = ruleService.createFact("test", "org.Address");
        BeanUtils.setProperty(addr,"street", "capitol");
        BeanUtils.setProperty(alex,"address", addr);
        
        
        
        List list = new ArrayList();
        list.add(elmo);
        list.add(alex);
        ruleService.execute("test", list,p,null);
    }
    
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception  {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration e = classLoader.getResources("META-INF/rules");
        assertTrue(e.hasMoreElements());
        
        RuleMgmtBak mgmt = new RuleMgmtBak(true);
        mgmt.start();
        
        System.out.println("firing originally rules");
        System.out.println("*********************************");
        RuleService svc = new RuleService(mgmt);
        fire(svc);

        /*
        System.out.println("FIRING UPDATED RULE SECTION");
        System.out.println("*********************************");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        mgmt.addPackage("test", loader.getResourceAsStream("META-INF/rule2.drl"));
        fire(svc);
        */
        
        /*
        System.out.println("adding new rules");
        URL u = new URL("file:///Users/elmo/rules/rules/rule2.drl");
        mgmt.addPackage(  "default", u.openStream() );
        System.out.println("firing updated rules");
        fire(mgmt);
        */
        
        
    }

    
    
}
