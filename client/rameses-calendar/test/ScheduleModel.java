
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Windhel
 */
public class ScheduleModel {
    
    private Calendar calendar;
    private String str;
    
    public ScheduleModel() {
        calendar = Calendar.getInstance();
    }
    
    public void init(String str) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime( format.parse(str) );
            System.out.println("" + calendar.getTime());
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Helper Methods ">
    public int getHour() {
        return calendar.get( Calendar.HOUR );
    }
    
    public int getMinute() {
        return calendar.get( Calendar.MINUTE );
    }
    
    public int getSecond() {
        return calendar.get( Calendar.SECOND );
    }
    
    public int getYear() {
        return calendar.get( Calendar.YEAR );
    }
    
    public int getMonth() {
        return calendar.get( Calendar.MONTH );
    }
    
    public int getDay() {
        return calendar.get( Calendar.DAY_OF_MONTH );
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
    //</editor-fold>
}
