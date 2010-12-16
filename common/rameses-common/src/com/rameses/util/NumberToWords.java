/*
 * NumberToWords.java
 *
 * Created on October 17, 2007, 8:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class NumberToWords {
    
    private static NumberToWords instance;
    
    private DecimalFormat _formatter = new DecimalFormat("0.00");
    
    public static NumberToWords getInstance() {
        if(instance==null) instance = new NumberToWords();
        return  instance;
    }
    
    private static final String[] majorNames = {
        "",
        " thousand",
        " million",
        " billion",
        " trillion",
        " quadrillion",
        " quintillion"
    };
    
    private static final String[] tensNames = {
        "",
        " ten",
        " twenty",
        " thirty",
        " forty",
        " fifty",
        " sixty",
        " seventy",
        " eighty",
        " ninety"
    };
    
    private static final String[] numNames = {
        "",
        " one",
        " two",
        " three",
        " four",
        " five",
        " six",
        " seven",
        " eight",
        " nine",
        " ten",
        " eleven",
        " twelve",
        " thirteen",
        " fourteen",
        " fifteen",
        " sixteen",
        " seventeen",
        " eighteen",
        " nineteen"
    };
    
    
    
    private String convertLessThanOneThousand(int number) {
        String soFar;
        
        if (number % 100 < 20){
            soFar = numNames[number % 100];
            number /= 100;
        } else {
            soFar = numNames[number % 10];
            number /= 10;
            
            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) return soFar;
        return numNames[number] + " hundred" + soFar;
    }
    
    public String convert(long number) {
        /* special case */
        if (number == 0) { return "zero"; }
        
        String prefix = "";
        
        if (number < 0) {
            number = -number;
            prefix = "negative";
        }
        
        String soFar = "";
        int place = 0;
        
        do {
            long n = number % 1000;
            if (n != 0) {
                String s = convertLessThanOneThousand((int)n);
                soFar = s + majorNames[place] + soFar;
            }
            place++;
            number /= 1000;
        } while (number > 0);
        
        return (prefix + soFar).trim();
        
    }
    
    public String convert(int number) {
        return convert((long)number);
    }
    
    public String convert(double number) {
        String str 		= number+"";
        String strDec	= "";
        String wn 		= convert((long)number);
        
        if (str.indexOf(".") >= 0) {
            strDec = str.replaceAll( ((long)number) + ".","");
            
            if ( Long.parseLong(strDec) > 0)
                strDec = " AND " + getCents(strDec) + "/100";
            else
                strDec = "";
        }
        
        return wn + strDec;
    }
    
    public String convert(BigDecimal d) {
        return convert(d.doubleValue());
    }
    
    public String getCents(String cents) {
        cents = _formatter.format(Double.valueOf("." + cents));
        return cents.substring(cents.indexOf(".")+1);
    }
    
    
    
    
}
