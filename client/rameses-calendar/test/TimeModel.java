
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Windhel
 */
public class TimeModel {
           
    private String text;
    private Calendar calendar;
    
    public TimeModel() {
        calendar = Calendar.getInstance();
    }
    
    public void init(String str) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime( format.parse(str) );
        } catch(Exception e) { e.printStackTrace(); }
    }

    public int getSecond() {
        return calendar.get( Calendar.SECOND );
    }
    
    public int getMinute() {
        return calendar.get( Calendar.MINUTE );
    }
    
    public int getHour() {
        return calendar.get( Calendar.HOUR );
    }
    
    public int getDay() {
        return calendar.get( Calendar.DAY_OF_MONTH );
    }
    
    public int getMonth() {
        return calendar.get( Calendar.MONTH );
    }
    
    public int getYear() {
        return calendar.get( Calendar.YEAR );
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
