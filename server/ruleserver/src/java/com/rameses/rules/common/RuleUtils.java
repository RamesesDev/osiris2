/*
 * CommandUtil.java
 *
 * Created on September 16, 2010, 9:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.drools.definition.type.FactType;
import org.drools.spi.KnowledgeHelper;

/**
 *
 * @author elmo
 */
public final class RuleUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    
    public static final Object createFact( KnowledgeHelper helper, String name ) throws Exception {
        FactType ft = helper.getWorkingMemory().getRuleBase().getFactType(name);
        return ft.newInstance();
    }
    
    public static final String formatDate( Date dt ) throws Exception {
       String format = System.getProperty(  "drools.dateformat" );
       //this is the default mask of drools
       if(format==null || format.trim().length()==0) format = "dd-MMM-yyyy";
       SimpleDateFormat df = new SimpleDateFormat(format);
       return df.format( dt );
    }
    
    
}
