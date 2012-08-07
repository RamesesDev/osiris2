package com.rameses.anubis;

import groovy.text.Template;
import java.util.Map;


public class ContentTemplate {
    private Template template;
    public ContentTemplate(Template t) {
        this.template = t;
    }
    public String render(Map map) {
        return template.make(map).toString();
    }
}