package com.rameses.rcp.impl;

import com.rameses.common.ExpressionResolver;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

public class ExpressionResolverImpl implements ExpressionResolver
{
    private ExpressionFactory factory = new ExpressionFactoryImpl();
    
    public ExpressionResolverImpl() {
    }

    public Object evaluate(Object bean, String expression) {
        SimpleContext context = new SimpleContext() ;
        context.setVariable("bean", factory.createValueExpression(bean, Object.class));        
        VEContext vex = new VEContext();
        vex.factory = factory;
        vex.bean = bean;
        
        ValueExpression ve = factory.createValueExpression(vex, expression, Object.class);
        return ve.getValue(context);
    }
    
    
    // <editor-fold defaultstate="collapsed" desc=" VEContext (Class) ">
    private class VEContext extends SimpleContext
    {
        private VEVariableMapper mapper;
        private ExpressionFactory factory;
        
        Object bean;

        public VariableMapper getVariableMapper() 
        {
            if (mapper == null)
            {
                mapper = new VEVariableMapper();
                mapper.mapper = super.getVariableMapper();
                mapper.factory = factory;
                mapper.ctx = this;
            }
            return mapper;
        }        
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" VEVariableMapper (Class) ">
    private class VEVariableMapper extends VariableMapper
    {
        private VariableMapper mapper;
        private ExpressionFactory factory;
        private VEContext ctx;
        
        public ValueExpression resolveVariable(String name) 
        {
            ValueExpression ve = mapper.resolveVariable(name);
            if (ve == null)
            {
                SimpleContext sc = new SimpleContext();
                sc.setVariable("bean", factory.createValueExpression(ctx.bean, Object.class));
                
                ValueExpression x = factory.createValueExpression(sc, "${bean." + name + "}", Object.class);
                ve = factory.createValueExpression(x.getValue(sc), Object.class);
                setVariable(name, ve);
            }
            return ve;
        }

        public ValueExpression setVariable(String name, ValueExpression ve) {
            return mapper.setVariable(name, ve);
        }        
    }
    // </editor-fold>
    
}
