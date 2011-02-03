
package com.rameses.calendar.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 20101210
 * @author Windhel
 */
public abstract class CalendarModel {
    
    private static final int MATRIX_SIZE = 42;
    
    private List<DateModel> modelList = new ArrayList();
    private Calendar currentMonth = Calendar.getInstance();
    private Listener listener;
    private DateModel selectedItem;
    
    
    public void init() {
        for(int i=0 ; i<MATRIX_SIZE ; i++) {
            modelList.add(new DateModel());
        }
    }
    
    public void load() {
        Calendar temp = Calendar.getInstance();
        temp.setTime( currentMonth.getTime() );
        Calendar before = Calendar.getInstance();
        before.setTime( currentMonth.getTime() );
        Calendar after = Calendar.getInstance();
        after.setTime( currentMonth.getTime() );
        
        int startDay = temp.get( Calendar.DAY_OF_WEEK );
        int maxDay = temp.getActualMaximum( Calendar.DAY_OF_MONTH );
        
        after.setTime( temp.getTime() );
        before.add( Calendar.MONTH, -1);
        before.set( Calendar.DAY_OF_MONTH, before.getActualMaximum( Calendar.DAY_OF_MONTH ) - startDay );
        after.add( Calendar.MONTH, 1);
        after.set( Calendar.DAY_OF_MONTH, 1);
        temp.set( Calendar.DAY_OF_MONTH, 1 );
        
        for(int i=0 ; i<MATRIX_SIZE; i++) {
            DateModel dm = modelList.get(i);
            dm.reset();
            if( i >= startDay && i <= maxDay+startDay-1 ) {
                if(dm.getDay() == temp.get(Calendar.DAY_OF_MONTH))
                    setSelectedItem( dm );
                
                dm.setIncluded( true );
                dm.init( temp.getTime() );
                temp.add(Calendar.DAY_OF_MONTH, 1);
            } else if( i < startDay ){
                dm.setIncluded( false );
                dm.init( before.getTime());
                before.add( Calendar.DAY_OF_MONTH, 1 );
            } else if( i > maxDay+startDay-2) {
                dm.setIncluded( false );
                dm.init( after.getTime());
                after.add( Calendar.DAY_OF_MONTH, 1);
            }
            
            fetchDay(dm);
        }
    }
    
    public abstract void fetchDay( DateModel d );
    
    public DateModel getSelectedItem(){
        return selectedItem;
    }
    
    public void setSelectedItem(DateModel d) {
        this.selectedItem = d;
    }
    
    public DateModel getToday(){
        DateModel dm = new DateModel();
        Calendar c = Calendar.getInstance();
        dm.init( c.getTime() );
        
        return dm;
    }
    
    public Date getSelectedDay(){
        return selectedItem.getDate();
    }
    
    public void nextMonth(){
        currentMonth.add( Calendar.MONTH, 1 );
        load();
        if( listener != null )
            listener.nextMonth();
    }
    
    public void previousMonth(){
        currentMonth.add( Calendar.MONTH, -1 );
        load();
        if( listener != null )
            listener.prevMonth();
    }
    
    public void nextYear(){
        currentMonth.add( Calendar.YEAR, 1 );
        load();
        if( listener != null )
            listener.nextYear();
    }
    
    public void previousYear(){
        currentMonth.add( Calendar.YEAR, -1 );
        load();
        if( listener != null)
            listener.prevYear();
    }
    
    public void refresh(){
        load();
        if( listener != null)
            listener.refresh();
    }
    
    public void refresh(Date d){
        load();
        if( listener != null)
            listener.refresh(d);
    }
    
    public void refresh(List<Date> days){
        load();
        if( listener != null)
            listener.refresh(days);
    }
    
    public void onSelect( DateModel d ){}
    
//    public Object onOpenItem( DateModel d ){}
    
    public String getMonthName() {
        return selectedItem.getMonthName();
    }
    
    public int getMonth(){
        return selectedItem.getMonth();
    }
    
    public int getDay(){
        return selectedItem.getDay();
    }
    
    public int getYear(){
        return selectedItem.getYear();
    }
    
    public int getFirstDayOfMonth(){
        Calendar c = Calendar.getInstance();
        c.setTime( selectedItem.getDate() );
        return c.getFirstDayOfWeek();
    }
    
    public int getLastDayOfMonth(){
        Calendar c = Calendar.getInstance();
        c.setTime( selectedItem.getDate() );
        return c.getActualMaximum( Calendar.MONTH );
    }
    
    public List<DateModel> getModelList() {
        return modelList;
    }
    
    public Listener getListener() {
        return listener;
    }
    
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Listener (interface) ">
    public static interface Listener {
        
        void nextMonth();
        void prevMonth();
        void nextYear();
        void prevYear();
        void refresh();
        void refresh(Date date);
        void refresh(List<Date> dates);
        
    }
    //</editor-fold>
}
