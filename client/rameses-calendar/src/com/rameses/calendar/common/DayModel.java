
package com.rameses.calendar.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 20101214
 * @author Windhel
 */
public abstract class DayModel {
    
    private List<ScheduleModel> modelList = new ArrayList();
    
    public abstract List fetchSchedules();
    public abstract Calendar fetchDate();
    
    public void init() {
        for(int i=0 ; i<fetchSchedules().size() ; i++) {
            if( fetchSchedules().get( i ) instanceof ScheduleModel ) {
                ScheduleModel sm = (ScheduleModel) fetchSchedules().get( i );
                sm.renderTooltip();
                if(sm.getFromDay() == fetchDate().get(Calendar.DAY_OF_MONTH))
                    modelList.add( sm );
            }
        }
    }
    
    public void load() {
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Setter/Getter ">
    public List<ScheduleModel> getModelList() {
        return modelList;
    }
    
    public void setModelList(List<ScheduleModel> modelList) {
        this.modelList = modelList;
    }
    //</editor-fold>
    
}