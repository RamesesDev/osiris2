/*
 * IChartItem.java
 *
 * Created on November 29, 2007, 11:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.bi.chart;

import java.util.Map;
/**
 *
 * @author rameses
 */
public class ChartItem {
    
    private Comparable colkey;
    private Comparable rowkey;
    private Number value;

    public ChartItem(){
        
    }
    
    public ChartItem( Map map ){
        this.colkey = (Comparable)map.get("colkey");
        this.rowkey = (Comparable)map.get("rowkey");
        this.value = (Number)map.get("value");
    }

    public ChartItem(Comparable colkey, Comparable rowkey, Number value  ){
        this.colkey = colkey;
        this.rowkey = rowkey;
        this.value = value;
    }
    
    public Comparable getRowkey() {
        return rowkey;
    }

    public void setRowkey(Comparable rowkey) {
        this.rowkey = rowkey;
    }

    public Comparable getColkey() {
        return colkey;
    }

    public void setColkey(Comparable colkey) {
        this.colkey = colkey;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number num) {
        this.value = num;
    }

}
