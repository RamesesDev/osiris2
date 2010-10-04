package com.rameses.util;

import java.text.MessageFormat;


public class BusinessException extends RuntimeException {
    
    private String errno;
    private Object[] args;
    
    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(String errno, String msg) {
        super(msg);
        this.errno = errno;
    }

    public BusinessException(String errno, String msg, Object[] args) {
        super(msg);
        this.errno = errno;
        this.args = args;
    }

    public String getErrno() {
        return errno;
    }

    public String format(String pattern) {
        if(args!=null) {
            return MessageFormat.format(pattern, args);
        }
        return pattern;
    }
    
}
