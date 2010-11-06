package com.rameses.rcp.support;
import java.awt.Color;
import java.awt.Font;

public final class ThemeUI 
{
    static
    {
        
    }
    
    
    private ThemeUI() {} 

    public static Font getFont(String key) 
    {
        return Font.decode("Arial-plain-11");
    }
    
    public static Color getColor(String key) 
    {
        return null;
    }    
}
