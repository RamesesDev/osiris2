package com.rameses.osiris2;

import com.rameses.osiris2.flow.AbstractNode;
import com.rameses.osiris2.flow.EndNode;
import com.rameses.osiris2.flow.PageFlow;
import com.rameses.osiris2.flow.PageNode;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author elmo
 */
public class WorkUnitInstance implements Serializable {
    
    WorkUnit workunit;
    private Object controller;
    private Page currentPage;
    private PageFlowInstance pageFlowInstance;
    
    private String title;   //title of the instance
    private String id;      //instance id
    
    public WorkUnitInstance(WorkUnit workunit) {
        this.workunit = workunit;
        //set the current page as the default
        try {
            this.currentPage = getDefaultPage();
        } catch(Exception ex){;}
    }
    
    public Object getController() {
        if( controller == null ) {
            try {
                controller = workunit.getCodeInstance();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        return controller;
    }
    
    public Page getDefaultPage() {
        return workunit.getDefaultPage();
    }
    
    public Page getPage(String name) {
        return (Page)workunit.getPages().get(name);
    }
    
    //WE DO THIS SO IT CAN BE VIEWED BY JSF PAGES
    
    //This should return the page instance.
    //Search first if it exists in the page instances.
    //If not then create new
    
    public Page getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(String name) {
        if( name  == null )
            currentPage = null;
        else
            currentPage = (Page) workunit.getPages().get(name);
    }
    
    
    //PAGE FLOW RELATED FUNCTIONS
    // <editor-fold defaultstate="collapsed" desc="PAGE FLOW RELATED">
    public PageFlow getPageFlow() {
        return getWorkunit().getPageFlow();
    }
    
    private PageFlowInstance getPageFlowInstance() {
        if( pageFlowInstance == null) {
            if( getPageFlow() == null ) return null;
            pageFlowInstance = new PageFlowInstance( this );
        }
        return pageFlowInstance;
    }
    
    public AbstractNode getCurrentNode() {
        return getPageFlowInstance().getCurrentNode();
    }
    
    
    public List getTransitions() {
        return getPageFlowInstance().getTransitions();
    }
    
    public String getStateTitle() {
        return getPageFlowInstance().getCurrentNode().getTitle();
    }
    
    public boolean isStarted() {
        return getPageFlowInstance().isStarted();
    }
    
    //</editor-fold>
    
    //THIS SHOULD BE CALLED BY ANY BEAN
    public void start() {
        signal();
    }
    
    public void start(String m) {
        signal( m );
    }
    
    public void signal() {
        signal(null);
    }
    
    public void signal(String transition) {
        PageFlowInstance p = getPageFlowInstance();
        p.signal(transition);
        if( p.getCurrentNode() instanceof PageNode ) {
            PageNode pg = (PageNode)p.getCurrentNode();
            this.setCurrentPage(pg.getName());
        } else if( p.getCurrentNode() instanceof EndNode ) {
            //check if there is an end node page defined
            EndNode en = (EndNode)p.getCurrentNode();
            String ename = en.getName();
            if(ename == null || ename.length()==0) ename = "end";
            Page pg = this.getPage(ename);
            if( pg == null ) ename = null;
            this.setCurrentPage(ename);
        }
    }
    
    public void copyProperties( Map props ) {
        Iterator iter = props.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry me = (Map.Entry)iter.next();
            try {
                PropertyUtils.setNestedProperty( getController(), me.getKey()+"", me.getValue() );
            } catch(Exception ex) {
                System.out.println("error in WorkUnitInstance.copyProperties. " + ex.getMessage());
            }
        }
    }
    
    //INSTANCE VALUES...
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    //</editor-fold>
    
    public WorkUnit getWorkunit() {
        return workunit;
    }
    
    public Module getModule() {
        return workunit.getModule();
    }
    
    public boolean equals(Object object) {
        if( object == null ) return false;
        if(! (object instanceof WorkUnitInstance )) return false;
        WorkUnitInstance wi = (WorkUnitInstance)object;
        return hashCode() == wi.hashCode() ;
    }
    
    public int hashCode() {
        return getId().hashCode();
    }
    
    public boolean isPageFlowCompleted() {
        return getPageFlowInstance().isEnded();
    }
    
    public void invoke(String method) {
        if( method != null && method.trim().length() > 0 ) {
            try {
                Object o = MethodUtils.invokeMethod(getController(), method, null);
                if( o != null ) {
                    if( o instanceof String ) {
                        String p = (String)o;
                        setCurrentPage(p);
                    }
                }
                if( getCurrentPage()==null ) {
                    setCurrentPage("default");
                }
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
    
    public void setProperty(String name, Object value) {
        try {
            PropertyUtils.setNestedProperty( getController(), name, value );
        } catch(Exception ex){
            throw new IllegalStateException(ex);
        }
    }
    
    public void setProperties( Map params ) {
        if( params !=null ) {
            Iterator iter = params.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry me = (Map.Entry)iter.next();
                String k = me.getKey()+"";
                Object v = me.getValue();
                setProperty(k, v);
            }
        }
    }
    
    //this is a special method that analyzes the msg type. 
    //If this is a page flow then it is signal.
    //If not a pageflow, action is invoked
    public void fireAction( String msg ) {
        if( getPageFlow()!=null) {
            signal(msg);
        }
        else {
            invoke( msg );
        }
    }
    
    
}
