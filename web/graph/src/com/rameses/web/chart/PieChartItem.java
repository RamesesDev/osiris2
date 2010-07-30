/*
 * IChartItem.java
 *
 * Created on November 29, 2007, 11:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.chart;

import java.util.Map;
/**
 *
 * @author rameses
 */
public class PieChartItem {
    
    private Comparable category;
    private Number value;

    public PieChartItem(){
        
    }
    
    public PieChartItem(Comparable category, Number num){
        this.value = num;
        this.category = category;
    }
    
    public PieChartItem(Map map){
        this.value = (Number)map.get("value");
        this.category = (Comparable)map.get("category");
    }

    
    public Number getValue() {
        return value;
    }

    public void setValue(Number num) {
        this.value = num;
    }

    public Comparable getCategory() {
        return category;
    }

    public void setCategory(Comparable category) {
        this.category = category;
    }
}
