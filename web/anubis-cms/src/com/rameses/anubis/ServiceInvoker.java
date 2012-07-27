package com.rameses.anubis;

public interface ServiceInvoker {
    Object invokeMethod(String methodName, Object[] args);
}