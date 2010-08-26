/*
 * CountDownTimer.java
 *
 * Created on August 21, 2010, 5:06 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class CountDownTimer extends ScheduledTask {
    
    private long secondsLeft = -1;
    private Calendar timerCal = Calendar.getInstance();
    private SimpleDateFormat formatter;
    private boolean executed;
    private boolean paused;
    
    
    public CountDownTimer() {
        formatter = new SimpleDateFormat(getTimeFormat());
    }
    
    public abstract long getMaxSeconds();
    public abstract void onTimeout();
    public void onProgress() {}
    
    public String getTimeFormat() {
        return "KK:mm:ss";
    }
    
    public final long getInterval() {
        return 1000;
    }
    
    public final void execute() {
        if( !executed ) {
            executed = true;
            secondsLeft = getMaxSeconds();
        }
        secondsLeft--;
        
        onProgress();
        if ( isEnded() ) {
            onTimeout();
        }
    }
    
    public final boolean accept() {
        return !paused;
    }
    
    public final boolean isEnded() {
        return executed && secondsLeft <= 0;
    }
    
    public final boolean isImmediate() {
        return true;
    }
    
    public final long getSecondsLeft() {
        if ( !executed ) {
            return getMaxSeconds();
        }
        return secondsLeft;
    }
    
    public final Date getTimeLeft() {
        long curSeconds = getSecondsLeft();
        int hours = (int) curSeconds / 60 / 60;
        int mins = (int) (curSeconds / 60) % 60;
        int secs = (int) curSeconds % 60;
        
        timerCal.set(Calendar.HOUR, hours);
        timerCal.set(Calendar.MINUTE, mins);
        timerCal.set(Calendar.SECOND, secs);
        
        return timerCal.getTime();
    }
    
    public final String getFormattedTimeLeft() {
        return formatter.format(getTimeLeft());
    }
    
    public void pause() {
        paused = true;
    }
    
    public void resume() {
        paused = false;
    }
    
    public void reset() {
        secondsLeft = -1;
        executed = false;
    }
    
}
