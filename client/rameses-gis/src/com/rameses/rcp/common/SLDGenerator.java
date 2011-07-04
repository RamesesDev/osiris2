package com.rameses.rcp.common;

import com.rameses.util.ValueUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 *
 * @author Windhel
 * 20110607
 */
public class SLDGenerator {
    
    //<editor-fold defaultstate="collapsed" desc=" createCustomSLD(String namedLayer, List<SLDRuleModel> rules) ">
    public static String createCustomSLD(String namedLayer, List<SLDRuleModel> rules) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<StyledLayerDescriptor version=\"1.0.0\"\n");
        sb.append("xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\"\n");
        sb.append("xmlns=\"http://www.opengis.net/sld\"\n");
        sb.append("xmlns:ogc=\"http://www.opengis.net/ogc\"\n");
        sb.append("xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n");
        sb.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
        sb.append("<NamedLayer>\n");
        sb.append("     <Name>" + namedLayer + "</Name>\n");
        sb.append("     <UserStyle>\n");
        sb.append("         <Title>"+ namedLayer + "</Title>\n");
        sb.append("         <FeatureTypeStyle>\n");
        
        sb.append(buildRules(rules));
        
        sb.append("         </FeatureTypeStyle>\n");
        sb.append("     </UserStyle>\n");
        sb.append("</NamedLayer>\n");
        sb.append("</StyledLayerDescriptor>\n");
        
        try {
            File file = new File(namedLayer);
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(sb.toString());
            out.close();
            fstream.close();
            return "file://" + file.getAbsolutePath();
        }catch(Exception ex) { ex.printStackTrace(); }
        
        return "";
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" buildRules(List<SLDRuleModel> rules) ">
    private static StringBuffer buildRules(List<SLDRuleModel> rules) {
        StringBuffer sb = new StringBuffer();
        for(SLDRuleModel srm : rules) {
            
            sb.append("         <Rule>\n");
            sb.append("           <Name>" + srm.getTitle() + "</Name>\n");
            sb.append("           <Title>" + srm.getTitle() + "</Title>\n");
            
            if(!ValueUtil.isEmpty(srm.getQuery()))
                sb.append(identifyQueryVariables(srm.getQuery()));
            
            sb.append(getRuleSymbolizers(srm));
            sb.append("         </Rule>\n");
            
        }
        return sb;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" identifyQueryVariables(String query) ">
    private static StringBuffer identifyQueryVariables(String query) {
        StringBuffer sb = new StringBuffer();
        query = query.trim();
        query = query.replace("'", "");
        int equationIndex = 0;
        int secondEquationIndex = 0;
        String equalityOperator = "";
        
        if(query.contains("=")) {               //EQUAL
            equalityOperator = "PropertyIsEqualTo";
            equationIndex = query.indexOf("=");
        }
        else if(query.contains("!=")) {         //ISNOTEQUALTO
            equalityOperator = "PropertyIsNotEqualTo";
            equationIndex = query.indexOf("!=");
        }
        if(query.contains(">=")) {             //GREATERTHANEQUALTO
            equalityOperator = "PropertyIsGreatherThanOrEqualTo";
            equationIndex = query.indexOf(">=");
        }
        if(query.contains(">")) {               //GREATERTHAN
            equalityOperator = "PropertyIsGreaterThan";
            equationIndex = query.indexOf(">");
        }
        if(query.contains("<")) {              //LESSTHAN
            equalityOperator = "PropertyIsLessThan";
            equationIndex = query.indexOf("<");
        }
        if(query.contains("<=")) {             //LESSTHANOREQUALTO
            equalityOperator = "PropertyIsLessThanOrEqualTo";
            equationIndex = query.indexOf("<=");
        }
        if(query.toUpperCase().contains("BETWEEN")) { //BETWEEN
            equalityOperator = "PropertyIsBetween";
            secondEquationIndex = query.toUpperCase().indexOf("AND");
            equationIndex = query.toUpperCase().indexOf("BETWEEN");
        }
        
        if(equalityOperator.equals("PropertyIsBetween")) {
            sb.append("             <ogc:Filter>\n");
            sb.append("                 <ogc:" + equalityOperator + ">\n");
            sb.append("                   <ogc:PropertyName>" + query.substring(0, equationIndex).trim() + "</ogc:PropertyName>\n");
            sb.append("                     <ogc:LowerBoundary>\n");
            sb.append("                       <ogc:Literal>" + query.substring(equationIndex + 7, secondEquationIndex).trim() + "</ogc:Literal>\n");
            sb.append("                     </ogc:LowerBoundary>\n");
            sb.append("                     <ogc:UpperBoundary>\n");
            sb.append("                       <ogc:Literal>" + query.substring(secondEquationIndex + 3, query.length()).trim() + "</ogc:Literal>\n");
            sb.append("                     </ogc:UpperBoundary>\n");
            sb.append("                 </ogc:" + equalityOperator + ">\n");
            sb.append("             </ogc:Filter>\n");
        } else {
            sb.append("             <ogc:Filter>\n");
            sb.append("                 <ogc:" + equalityOperator + ">\n");
            sb.append("                 <ogc:PropertyName>" + query.substring(0, equationIndex).trim() + "</ogc:PropertyName>\n");
            sb.append("                 <ogc:Literal>" + query.substring(equationIndex + 1, query.length()).trim() + "</ogc:Literal>\n");
            sb.append("                 </ogc:" + equalityOperator + ">\n");
            sb.append("             </ogc:Filter>\n");
        }
        
        return sb;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" getRuleSymbolizers(SLDRuleModel srm) ">
    private static StringBuffer getRuleSymbolizers(SLDRuleModel srm) {
        StringBuffer sb = new StringBuffer();
        for(RuleSymbolizerModel rsm : srm.getSymbolizers()) {
            sb.append( rsm.toXml() );
        }
        return sb;
    }
    //</editor-fold>
    
}
