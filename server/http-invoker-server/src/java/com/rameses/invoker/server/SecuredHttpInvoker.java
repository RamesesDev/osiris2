package com.rameses.invoker.server;

import com.rameses.util.CipherUtil;
import java.io.Serializable;

public class SecuredHttpInvoker extends HttpInvoker {
    
    protected Object[] filterInput( Object obj ) throws Exception {
        return (Object[]) CipherUtil.decode( (Serializable)obj );
    }

    protected Object filterOutput(Object obj) throws Exception {
        return CipherUtil.encode( (Serializable)obj );
    }
    
}
