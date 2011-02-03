package com.rameses.util;

public class SystemException extends AppException {
    
    public static long serialVersionUID = 1L;
    
    private String errcode;
    private Object info;
    
    public SystemException(String msg) {
        super(msg);
    }
    
    public SystemException(String errcode, String msg) {
        super(msg);
        this.errcode = errcode;
    }
    
    public SystemException(String errcode, String msg, Object info) {
        super(msg);
        this.errcode = errcode;
        this.info = info;
    }
    
    public String getErrcode() {
        return errcode;
    }

    public Object getInfo() {
        return info;
    }

    
}
