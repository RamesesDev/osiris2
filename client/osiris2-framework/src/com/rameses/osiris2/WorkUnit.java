/*
 * WorkUnit.java
 *
 * Created on February 17, 2009, 7:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import com.rameses.osiris2.flow.PageFlow;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class WorkUnit implements Serializable {
    
    private String name;
    
    //The namespace determines from where this workunit was produced.
    private Module module;
    private Map pages = new Hashtable();
    private PageFlow pageFlow;
    
    private Page defaultPage;
    private String title;
    private Map properties = new HashMap();
    private String codeSource;
    private Class sourceClass;
    
    public WorkUnit() {
    }
    
    public WorkUnit(String name) {
        this.setName(name);
    }
    
    // <editor-fold defaultstate="collapsed" desc="ADD MEMBER UTILITY">
 
    
    public void addPage( Page page ) {
        //add a default name if page does not yet exist
        if(page.getName() == null || page.getName().length()==0 || page.getName().equals("default") ) {
            page.setName("default");
            defaultPage = page;
        }
        
        pages.put(page.getName(), page );
        
        //this is to enure that there will always be a default page.
        if( defaultPage == null ) {
            defaultPage = page;
        }
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    //</editor-fold>
    
    
    public void destroy() {
        Iterator iter = getPages().values().iterator();
        getPages().clear();
        pages = null;
    }
    
    public String toXml() {
        StringBuffer sb = new StringBuffer();
        sb.append( "<workunit>");
        if( codeSource != null ) {
            sb.append( "<code>");
            sb.append(codeSource);
            sb.append("</code>");
        }
        if( getPages().size() > 0 ) {
            sb.append("<pages>");
            Iterator iter = getPages().values().iterator();
            while(iter.hasNext()) {
                sb.append(((Page)iter.next()).toXml());
            }
            sb.append("</pages>");
        }
        sb.append("</workunit>");
        if( getPageFlow() !=null ) {
            sb.append("<pageflow>");
            sb.append( getPageFlow().toString() );
            sb.append("</pageflow>");
        }
        sb.append("</workunit>");
        return  sb.toString();
    }
    
    public WorkUnitInstance newInstance() {
        return new WorkUnitInstance(this);
    }
    
    public WorkUnitInstance newInstance(String id, String title) {
        WorkUnitInstance wi = new WorkUnitInstance(this);
        wi.setId(id);
        wi.setTitle(title);
        return wi;
    }

    public Map getPages() {
        return pages;
    }
    
    public Page getDefaultPage() {
        return defaultPage;
    }

    public PageFlow getPageFlow() {
        return pageFlow;
    }

    public void setPageFlow(PageFlow pageFlow) {
        this.pageFlow = pageFlow;
    }

    public Map getProperties() {
        return properties;
    }

    public String toString() {
        return getId();
    }

    public boolean equals(Object object) {
        if( object == null || !(object instanceof WorkUnit)) return false;
        WorkUnit w = (WorkUnit)object;        
        return getId().equals( w.getId() );
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
    
    public String getId() {
        return getModule().getNamespace() + ":" + getName();
    }

    public void setCodeSource(String codeSource) {
        this.codeSource = codeSource;
    }
    
    public Object getCodeInstance() {
        try {
            CodeProvider provider = module.getAppContext().getCodeProvider();
            if(sourceClass==null) {
                sourceClass = provider.createClass(codeSource);
            }
            return provider.createObject(sourceClass);
        }
        catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
   
}
