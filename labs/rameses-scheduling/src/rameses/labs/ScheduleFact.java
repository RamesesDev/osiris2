/*
 * Schedule.java
 * Created on July 16, 2011, 2:58 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package rameses.labs;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jzamss
 */
public class ScheduleFact implements Serializable {

    private String id;
    private int day;
    private String code;
    private Date fromdate;
    private Date todate;
    private String fromTime;
    private String toTime;

    public ScheduleFact() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Date getFromdate() {
        return fromdate;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public Date getTodate() {
        return todate;
    }

    public void setTodate(Date todate) {
        this.todate = todate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
}
