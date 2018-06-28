package pwcg.gui.utils;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;


public class StringValidity
{
    static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder(); 

    public static boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }

        return true;
    }

    public static boolean isInteger(String str)
    {
        try
        {
            Integer.parseInt(str);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }

        return true;
    }
    
    public static boolean isPureAscii(String v)
    {
        return asciiEncoder.canEncode(v);
    }


    public static boolean isAlpha(String name)
    {
        char[] chars = name.toCharArray();
        
        if (!isPureAscii(name))
        {
            return false;
        }

        for (char c : chars)
        {
            if (!Character.isLetter(c) && !(c == ' ') && !(c == '-') && !(c == '.'))
            {
                return false;
            }
        }

        return true;
    }
}
