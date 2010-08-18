/*
 * EntityManager.java
 *
 * Created on August 15, 2010, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.schema.SchemaManager;
import com.rameses.sql.SqlManager;

/**
 *
 * @author elmo
 */
public class DefaultEntityManager implements EntityManager {
    
    private SqlManager sqlManager;
    private SchemaManager schemaManager;
    
    public DefaultEntityManager(SchemaManager scm, SqlManager sqm) {
        sqlManager= sqm;
        schemaManager = scm;
    }
    
    public Object create(String schemaName, Object data) {
        return data;
    }

    public Object read(String schemaName, Object data) {
        return data;
    }

    public Object update(String schemaName, Object data) {
        return data;
    }

    public void delete(String schemaName, Object data) {
        
    }
    
    /*
    public void persist( Object data, String schemaName ) {
        sqlFactory.getConf().getExtensions().put( SchemaManager.class, schemaFactory );
        Schema schema = schemaFactory.getSchema( schemaName );
        SchemaScanner scanner = schemaFactory.newSchemaScanner();
        
        SqlContext sm =  sqlFactory.createContext();
        CrudExecutor createHandler = new CrudExecutor( schema,sm, "create" );
        scanner.scan(schema, data, createHandler );
        executeQueue( createHandler.getQueue(),sm );
    }
    
    public void executeQueue( Queue q, SqlContext sm ) {
        Object o = null;
        while(q.peek()!=null) {
            SqlExecutor se = (SqlExecutor)q.remove();
            System.out.println("executing...");
            System.out.println(se.getStatement());
            int i = 0;
            for(String s: se.getParameterNames()) {
                System.out.println(s + "=" +se.getParameterValues().get(i++) );
            }
            //for(  )
        }
    }
    */
    
    /*
    public class CrudExecutor implements SchemaHandler {

        private Queue queue = new LinkedList();
        private Schema schema;
        private SqlContext sqlManager;
        private String action;
        
        public CrudExecutor(Schema s, SqlContext sm, String action) {
            schema = s;
            sqlManager = sm;
            this.action = action;
        }
        
        public void startSchema() {
        }

        public void startElement(SchemaElement element, Object data) {
            String schemaName = element.getName();
            SqlExecutor qe = sqlManager.createNamedExecutor(element.getName()+"_"+action+".xml-schema");
            qe.setParameters( (Map)data );
            queue.add( qe );
        }

        public void startField(SimpleField f, Object value) {
        }

        public void endElement(String name) {
        }

        public void endSchema() {
        }

        public Queue getQueue() {
            return queue;
        }
        
    }
    */

    
    
}
