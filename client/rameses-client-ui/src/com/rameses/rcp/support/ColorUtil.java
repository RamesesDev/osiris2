/*
 * ColorUtil.java
 *
 * Created on February 22, 2011, 3:00 PM
 * @author jaycverg
 */

package com.rameses.rcp.support;

import java.awt.Color;


public class ColorUtil {
    
    public static Color brighter(Color c, int value) {
        if (value < 0) return c;
        
        float[] hsb = Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),new float[3]);
        int h = (int) (hsb[0] * 360);
        int s = (int) (hsb[1] * 100);
        int b = (int) (hsb[2] * 100);
        
        int rm = 0;
        b += value;
        if (b > 100) {
            rm = b - 100;
            b = 100;
        }
        s -= rm;
        if (s < 0) s = 0;
        
        int rgb = Color.HSBtoRGB(h/360.0f, s/100.0f, b/100.0f);
        return new Color(rgb);
    }
    
    public static Color darker(Color c, int value) {
        if (value < 0) return c;
        
        float[] hsb = Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),new float[3]);
        int h = (int) (hsb[0] * 360);
        int s = (int) (hsb[1] * 100);
        int b = (int) (hsb[2] * 100);
        
        int rm = 0;
        b -= value;
        if (b < 0) {
            rm = b * (-1);
            b = 0;
        }
        s += rm;
        if (s > 100) s = 100;
        
        int rgb = Color.HSBtoRGB(h/360.0f, s/100.0f, b/100.0f);
        return new Color(rgb);
    }
    
}
