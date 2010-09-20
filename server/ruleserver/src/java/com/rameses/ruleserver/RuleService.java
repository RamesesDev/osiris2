package com.rameses.ruleserver;

import com.rameses.rules.common.ActionCommand;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import org.drools.KnowledgeBase;
import org.drools.definition.type.FactType;
import org.drools.runtime.StatefulKnowledgeSession;


@Stateless
@Local(RuleServiceLocal.class)
public class RuleService implements RuleServiceLocal {
    
    @Resource(mappedName="RuleMgmt")
    private RuleMgmtMBean ruleMgmt;
    
    public RuleService() {
        
    }

    //for testing only
    public RuleService(RuleMgmt mgmt) {
        this.ruleMgmt = mgmt;
    }
    
    
    public Object createFact( String ruleset, String name ) throws Exception {
        KnowledgeBase kb = ruleMgmt.getKnowledgeBase(ruleset);
        String pkg = name.substring(0, name.lastIndexOf("."));
        String cls = name.substring(name.lastIndexOf(".")+1);
        FactType ftype = kb.getFactType( pkg, cls );
        return ftype.newInstance();
    }
    
    public Object execute(String ruleset, List facts, Object globals, String agenda) throws Exception {
        KnowledgeBase kb = ruleMgmt.getKnowledgeBase(ruleset);
        if(kb==null) 
            throw new RuntimeException("Knowledgebase " + ruleset + " is not found!");
        StatefulKnowledgeSession session = null;
        try {
            session = kb.newStatefulKnowledgeSession();
            if(facts!=null) {
                for(Object o: facts) {
                    session.insert(o);
                }
            }
            if( globals !=null ) {
                if( globals instanceof ActionCommand ) {
                    ActionCommand ac = (ActionCommand)globals;
                    try {
                        session.setGlobal(ac.GLOBAL_NAME, ac);
                    }
                    catch(Exception ign){;}
                }
                else if( globals instanceof Map ) {
                    Map g = (Map)globals;
                    for(Object o: g.entrySet()) {
                        Map.Entry me = (Map.Entry)o;
                        try {
                            session.setGlobal( me.getKey()+"", me.getValue() );
                        }
                        catch(Exception ign){;}
                    }
                }
                else {
                    System.out.println("warning executing...Globals " + globals.toString() + " is unrecognized");
                }
            }
            
            if( agenda ==null || agenda.trim().length()==0 ) {
                session.fireAllRules();
            }
            else {
                session.fireAllRules();
            }
            return null;
        } catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally{
            session.dispose();
        }
    }

    
}
