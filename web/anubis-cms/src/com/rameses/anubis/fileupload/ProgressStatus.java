package com.rameses.anubis.fileupload;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ProgressStatus implements Serializable {
    
    private static final DecimalFormat NUM_FORMAT = new DecimalFormat("0.00");
    
    private long percentCompleted;
    private double uploadRate;
    private double estimatedRuntime;
    
        
    public ProgressStatus() {}

    public long getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(long percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public double getUploadRate() {
        return uploadRate;
    }

    public void setUploadRate(double uploadRate) {
        this.uploadRate = uploadRate;
    }

    public double getEstimatedRuntime() {
        return estimatedRuntime;
    }

    public void setEstimatedRuntime(double estimatedRuntime) {
        this.estimatedRuntime = estimatedRuntime;
    }
    
    public String toJSON() {
        return "{" +
                " percentCompleted: " + percentCompleted + ", " +
                " uploadRate: " + NUM_FORMAT.format(uploadRate) + ", " +
                " estimatedRuntime: " + NUM_FORMAT.format(estimatedRuntime) +
                "}";
    }
    
    public String toString() {
        return toJSON();
    }
    
    public Map toMap() {
        Map m = new HashMap();
        m.put("percentCompleted", percentCompleted);
        m.put("uploadRate", uploadRate);
        m.put("estimatedRuntime", estimatedRuntime);
        return m;
    }
    
}
