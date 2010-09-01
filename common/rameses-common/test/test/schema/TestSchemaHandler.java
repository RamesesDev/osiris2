package test.schema;

import com.rameses.schema.ComplexField;
import com.rameses.schema.LinkField;
import com.rameses.schema.Schema;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaHandler;
import com.rameses.schema.SchemaHandlerStatus;
import com.rameses.schema.SimpleField;


// <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
public class TestSchemaHandler implements SchemaHandler {
    
    
    
    public void startSchema(Schema schema) {
    }
    
    public void startElement(SchemaElement element, Object data) {
        //System.out.println("element name->" + element.getName());
    }
    
    public void processField(SimpleField f, String refname, Object value) {
        System.out.println("field->"+refname+ "="+value);
    }
    
    public void endElement(SchemaElement element) {
        //System.out.println("end element->"+element.getName());
    }
        
    public void endSchema(Schema schema) {
    }

    public void startLinkField(LinkField f, String refname, SchemaElement element) {
    }

    public void endLinkField(LinkField f) {
    }


    public void endComplexField(ComplexField cf) {
    }

    public void setStatus(SchemaHandlerStatus status) {
    }

    public void startComplexField(ComplexField cf, String refname, SchemaElement element,Object data) {
    }

    
}