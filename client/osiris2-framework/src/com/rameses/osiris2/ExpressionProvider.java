package com.rameses.osiris2;

import java.util.Map;

public interface ExpressionProvider {
    Object eval(String expr, Map params);
}
