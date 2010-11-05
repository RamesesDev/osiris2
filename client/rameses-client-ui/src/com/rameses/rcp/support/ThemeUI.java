package com.rameses.rcp.support;
import java.awt.Color;
import java.awt.Font;

public final class ThemeUI 
{
    static
    {
        
    }
    
    
    private ThemeUI() {} 

    public static Font getSystemFont() 
    {
        Font f = Font.decode("Arial-plain-11");
        return f.deriveFont(f.getStyle(), f.getSize2D()); 
    }
    
    
    public static Font getFont(String key) 
    {
        return null;
    }
    
    public static Color getColor(String key) 
    {
        return null;
    }    
}
