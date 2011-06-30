import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public final class MoneyUtil {
    public final static int DEFAULT_SCALE = 2;
    
    public static double round( double amount ) {
        if(amount == 0) return amount;
        BigDecimal d = new BigDecimal(amount+"");
        return  d.setScale(2, RoundingMode.HALF_UP).doubleValue() ;
    }
    /*
    public static double calculateMonthInterest( double amount, double rate, Date fromDate, Date toDate ) {
        double term = (double)DateUtil.getMonthDiff( fromDate, toDate );
        return round(amount * rate * term);
    }
    
    public static double calculateMonthInterest( double amount, double rate, Date fromDate, Date toDate, int maxTerm ) {
        double term = (double)DateUtil.getMonthDiff( fromDate, toDate );
        if( term > maxTerm) term = maxTerm;
        double interest = round(amount * rate * term);
        return interest;
        
    }
    */
    public static double qtrValue( double amount, int qtr ) {
        if( qtr == 0 )
            return amount;
        BigDecimal a = new BigDecimal(amount+"");
        BigDecimal d = a.divide( new BigDecimal(4),2,RoundingMode.HALF_UP );
        double dbl = 0;
        if( qtr != 4 ) {
            dbl = round( d.doubleValue() );
        } else {
            dbl = round( d.doubleValue() );
            d = a.subtract( d ).subtract( d ).subtract( d );
            dbl = d.doubleValue();  //d.setScale( 0, RoundingMode.HALF_UP ).doubleValue();
        }
        return dbl;
    }
    
    public static double toDoubleValue(  Object o ) {
        if( o == null )
            return 0.0;
        Double dbl = 0.0;
        if( o instanceof Integer ) {
            dbl = new Double( o + "");
        } else {
            dbl = (Double)o;
        }
        BigDecimal bd =  new BigDecimal( dbl );
        bd = bd.setScale( 2, RoundingMode.HALF_UP );
        return bd.doubleValue();
    }
    
    public static String format(BigDecimal bd) {
        DecimalFormat df =  new DecimalFormat("#,##0.00");
        return df.format(bd);
    }
    
//<editor-fold defaultstate="collapsed" desc="----- BigDecimal Support -----">

    public static BigDecimal add( BigDecimal bd1, BigDecimal bd2) {
        return add(bd1, bd2, DEFAULT_SCALE);
    }
    
    public static BigDecimal add( BigDecimal bd1, BigDecimal bd2, int scale) {
        return bd1.add(bd2).setScale(scale, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal subtract( BigDecimal minued, BigDecimal subtrahend) {
        return minued.subtract(subtrahend).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal multiply( BigDecimal bd1, BigDecimal bd2) {
        return bd1.multiply(bd2).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }    
    
    public static BigDecimal addAll( BigDecimal[] addends ) {
        BigDecimal res = new BigDecimal(0);
        for(int i = 0; i < addends.length; i++) {
            res = res.add(addends[i]);
        }
        return res.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal round( BigDecimal amount ) {
        return round(amount, DEFAULT_SCALE);
    }
    
    public static BigDecimal round( BigDecimal amount, int scale ) {
        return amount.setScale(scale, RoundingMode.HALF_UP);
    }
    
    
    public static BigDecimal divide(BigDecimal val, BigDecimal divisor) {
        return val.divide(divisor, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    
//</editor-fold>

    
}
