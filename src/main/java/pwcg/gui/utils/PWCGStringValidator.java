package pwcg.gui.utils;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class PWCGStringValidator
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
    
    private static boolean isPureAscii(String v)
    {
        return asciiEncoder.canEncode(v);
    }


    public static boolean isValidName(String text)
    {
        if (text == null)
        {
            return false;
        }

        if (text.length() == 0)
        {
            return false;
        }

        char[] chars = text.toCharArray();
        
        if (!isPureAscii(text))
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

    public static boolean isValidDescriptor(String text)
    {
        if (text == null)
        {
            return false;
        }

        if (text.length() == 0)
        {
            return false;
        }

        char[] chars = text.toCharArray();
        
        if (!isPureAscii(text))
        {
            return false;
        }

        for (char c : chars)
        {
            if (!Character.isLetter(c) && !Character.isDigit(c) &&!(c == ' ')&& !(c == '_') && !(c == '-') && !(c == '.'))
            {
                return false;
            }
        }

        return true;
    }
}
