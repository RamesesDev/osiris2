/*
 * IWorkUnitParser.java
 *
 * Created on February 25, 2009, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import com.rameses.osiris2.flow.AbstractNode;
import com.rameses.osiris2.flow.EndNode;
import com.rameses.osiris2.flow.PageFlow;
import com.rameses.osiris2.flow.PageNode;
import com.rameses.osiris2.flow.ProcessNode;
import com.rameses.osiris2.flow.StartNode;
import com.rameses.osiris2.flow.SubProcessNode;
import com.rameses.osiris2.flow.Transition;
import com.rameses.util.AbortSAXException;
import com.rameses.util.ParserUtil;
import com.rameses.util.URLDirectory;
import com.rameses.util.URLDirectory.URLFilter;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author elmo
 * This parser parses all workunits inside the module.
 */
public class ModuleParser implements URLFilter {
    
    private AppContext appContext;
    private Module module;
    private ClassLoader loader;
    private WorkUnitParser workunitParser = new WorkUnitParser();
    
    public ModuleParser(ClassLoader loader) {
        this.loader = loader;
    }
    
    public void parse(Module module) {
        this.module = module;
        this.appContext = module.getAppContext();
        try {
            URL url = new URL(module.getContextPath() + "/workunits");
            URLDirectory u = new URLDirectory(url);
            u.list( this, loader );
        } catch(Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }
    
    public boolean accept(URL u, String filter) {
        String path = u.toExternalForm();
        if( path.endsWith(".xml") ) {
            InputStream is = null;
            try {
                String workunitname = path.substring( path.lastIndexOf("/workunits/")+11, path.lastIndexOf(".") );
                is = u.openStream();
                
                //initialize the workunit
                WorkUnit workunit = new WorkUnit();
                workunit.setModule(module);
                workunit.setName(workunitname);
                
                SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
                workunitParser.setWorkUnit(workunit);
                sp.parse( is, workunitParser );
                
                //add to modules only if workunit was successfully parsed.
                module.getWorkUnits().put( workunit.getName(), workunit );
                
            } catch(AbortSAXException ab) {
                //do nothing
            } catch(Exception ex) {
                System.out.println("error parsing " + u.toExternalForm() + " " + ex.getMessage() );
            } finally {
                try { is.close(); } catch(Exception ign){;}
            }
        }
        return false;
    }
    
    
    private class WorkUnitParser extends DefaultHandler {
        private WorkUnit workunit;
        private StringBuffer sb = new StringBuffer();
        private PageFlow pageFlow = null;
        private AbstractNode abstractNode = null;
        
        public WorkUnitParser() {
            
        }
        
        public WorkUnitParser(WorkUnit wu) {
            this.workunit = wu;
        }
        
        public void setWorkUnit(WorkUnit wu) {
            this.workunit = wu;
            this.sb = new StringBuffer();
            pageFlow = null;
        }
        
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(pageFlow==null) {
                if(qName.equals("workunit")) {
                    ParserUtil.loadAttributes(workunit, workunit.getProperties(), attributes, appContext.getPropertyResolver());
                    //check if it has extends. If yes, parse it first.
                    String s = (String)workunit.getProperties().get("extends");
                    if( s!=null && s.trim().length() > 0 ) {
                        workunit.getProperties().remove("extends");
                        String arr[] = s.split(",");
                        for(int i=0; i<arr.length; i++) {
                            String module = null;
                            String path = null;
                            try {
                                path = arr[i].trim();
                                if ( path.matches("^[^/]+:/.+") ) {
                                    String[] ss = path.split(":/");
                                    module = ss[0];
                                    path = ss[1];
                                }
                                URL u = null;
                                if ( module == null ){
                                    u = loader.getResource(path);
                                } else {
                                    Module m = workunit.getModule().getAppContext().getModule(module);
                                    if ( m == null )
                                        throw new RuntimeException("Module " + module + " not found.");
                                    
                                    u = m.getResource(path);
                                }
                                
                                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                                if(u!=null) {
                                    WorkUnitParser wp = new WorkUnitParser(workunit);
                                    parser.parse( u.openStream(), wp );
                                }
                            }catch(Exception e) {
                                System.out.println("ERROR PARSING " + arr[i] + "! DETAILS: " + e.getMessage());
                            }
                        }
                    }
                } else if( qName.equals("invoker") ) {
                    Invoker in = new Invoker();
                    in.setModule( module );
                    in.setWorkunitid( module.getName() + ":" + workunit.getName() );
                    in.setWorkunitname( workunit.getName() );
                    ParserUtil.loadAttributes( in, in.getProperties(), attributes,appContext.getPropertyResolver() );
                    module.getInvokers().add( in );
                } else if(qName.equals("code")) {
                    String className = attributes.getValue("class");
                    if ( className != null ) workunit.setClassName(className);
                    sb.delete(0, sb.length());
                } else if(qName.equals("pageflow")) {
                    pageFlow = new PageFlow();
                } else if( qName.equals("page")) {
                    Page p = new Page();
                    ParserUtil.loadAttributes(p, p.getProperties(), attributes,appContext.getPropertyResolver());
                    workunit.addPage(p);
                }
            } else {
                //reserved for pageFlow
                //page flow related parsing
                if(qName.equals("start")) {
                    abstractNode = new StartNode();
                    ParserUtil.loadAttributes(abstractNode, abstractNode.getProperties(), attributes,appContext.getPropertyResolver());
                    pageFlow.addNode(abstractNode);
                } else if(qName.equals("end")) {
                    abstractNode = new EndNode();
                    ParserUtil.loadAttributes(abstractNode, abstractNode.getProperties(), attributes,appContext.getPropertyResolver());
                    pageFlow.addNode(abstractNode);
                } else if( qName.equals("page") ) {
                    abstractNode = new PageNode();
                    ParserUtil.loadAttributes(abstractNode, abstractNode.getProperties(), attributes,appContext.getPropertyResolver());
                    pageFlow.addNode(abstractNode);
                } else if(qName.equals("process")) {
                    abstractNode = new ProcessNode();
                    ParserUtil.loadAttributes(abstractNode, abstractNode.getProperties(), attributes,appContext.getPropertyResolver());
                    pageFlow.addNode(abstractNode);
                } else if(qName.equals("subprocess")) {
                    abstractNode = new SubProcessNode();
                    ParserUtil.loadAttributes(abstractNode, abstractNode.getProperties(), attributes,appContext.getPropertyResolver());
                    pageFlow.addNode(abstractNode);
                } else if( qName.equals("transition")) {
                    Transition transition = new Transition();
                    ParserUtil.loadAttributes(transition, transition.getProperties(), attributes,appContext.getPropertyResolver());
                    abstractNode.getTransitions().add(transition);
                }
            }
        }
        
        public void characters(char[] c, int i, int i0) throws SAXException {
            sb.append( c, i, i0 );
        }
        
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if( qName.equals("code")) {
                workunit.setCodeSource( sb.toString() );
            } else if(qName.equals("pageflow")) {
                workunit.setPageFlow(pageFlow);
                pageFlow = null;
            }
        }
    }
}