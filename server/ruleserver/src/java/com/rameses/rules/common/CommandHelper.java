/*
 * CommandUtil.java
 *
 * Created on September 16, 2010, 9:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules.common;

import org.drools.definition.type.FactType;
import org.drools.spi.KnowledgeHelper;

/**
 *
 * @author elmo
 */
public final class CommandHelper {
    
    public static final Object createFact( KnowledgeHelper helper, String name ) throws Exception {
        FactType ft = helper.getWorkingMemory().getRuleBase().getFactType(name);
        return ft.newInstance();
    }
    
}
