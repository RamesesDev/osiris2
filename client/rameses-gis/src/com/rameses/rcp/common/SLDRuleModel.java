package com.rameses.rcp.common;

import java.util.List;

/**
 *
 * @author Windhel
 * 20110606
 */
public abstract class SLDRuleModel {
    private String title;
    private String query;
    
    public SLDRuleModel(){}
    
    public SLDRuleModel(String title, String query) {
        this.title = title;
        this.query = query;
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Setter/Getter ">
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    //</editor-fold>
    
    public abstract List<RuleSymbolizerModel> getSymbolizers();
}
