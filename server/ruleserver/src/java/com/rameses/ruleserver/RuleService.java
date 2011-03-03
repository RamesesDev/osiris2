package com.rameses.ruleserver;

import com.rameses.rules.common.RuleAction;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.ejb.Stateless;
import org.drools.KnowledgeBase;
import org.drools.definition.type.FactType;
import org.drools.runtime.StatefulKnowledgeSession;

@Stateless
@Local(RuleServiceLocal.class)
public class RuleService implements RuleServiceLocal {
    
    public RuleService() {
        
    }

    public Object createFact( String ruleset, String name ) throws Exception {
        return  createFact( ruleset, name, null );
    }
    
    //the data is the map coming from the client. we need to copy its properties
    public Object createFact( String ruleset, String name, Map data ) throws Exception {
        KnowledgeBase kb = RuleManager.getInstance().getKnowledgeBase(ruleset);
        if(kb==null)
            throw new Exception("Knowledge set " + ruleset + " not found"); 
        String pkg = name.substring(0, name.lastIndexOf("."));
        String cls = name.substring(name.lastIndexOf(".")+1);
        FactType ftype = kb.getFactType( pkg, cls );
        if( ftype ==null )
            throw new Exception("Fact type " + pkg + "." + cls + " does not exist");
        Object fact = ftype.newInstance();
        if( data !=null ) {
            //ftype.setFromMap( fact, data );
            for(Object m: data.entrySet()) {
                Map.Entry me = (Map.Entry)m;
                try {
                    ftype.set(fact, me.getKey()+"", me.getValue());
                }
                catch(Exception ign){;}
            }
        }
        return fact;
    }
    
    public void execute(String ruleset, List facts, Object globals, String agenda) throws Exception {
        KnowledgeBase kb = RuleManager.getInstance().getKnowledgeBase(ruleset);
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
                if( globals instanceof RuleAction ) {
                    RuleAction ac = (RuleAction)globals;
                    try {
                        session.setGlobal(ac.getName(), ac);
                    }
                    catch(Exception ign){
                        System.out.println(ign.getMessage());
                    }
                }
                else if( globals instanceof Map ) {
                    Map g = (Map)globals;
                    for(Object o: g.entrySet()) {
                        Map.Entry me = (Map.Entry)o;
                        try {
                            session.setGlobal( me.getKey()+"", me.getValue() );
                        }
                        catch(Exception ign){
                            System.out.println(ign.getMessage());
                        }
                    }
                }
                else {
                    System.out.println("warning executing...Globals " + globals.toString() + " is unrecognized");
                }
            }
            
            if( agenda !=null && agenda.trim().length() > 0 ) {
                session.getAgenda().getAgendaGroup(agenda).setFocus();
            }
            session.fireAllRules();
        } 
        catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally{
            session.dispose();
        }
    }
    /*
    public class SimpleAgendaFilter implements AgendaFilter {
          private final String  name;
          public SimpleAgendaFilter(String n) {
              name = n;
          }
          public boolean accept(final Activation activation) {
              return activation.getAgendaGroup().getName().equals(name);
          }
      }
    */
    public void execute(String ruleset, List facts) throws Exception {
        execute(ruleset, facts, null, null);
    }

    public void execute(String ruleset, List facts, Object globals) throws Exception {
        execute(ruleset,facts,globals,null);
    }

    public RuleAction createRuleAction() {
        return new RuleAction();
    }

    
}
