package pwcg.gui.utils;

public class PWCGStringValidator
{
    public static boolean validateStringIsAlpha(String text)
    {
        if (text == null)
        {
            return false;
        }

        if (text.length() == 0)
        {
            return false;
        }

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z') && !(c == ' '))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean validateStringIsAlphaNumeric(String text)
    {
        if (text == null)
        {
            return false;
        }

        if (text.length() == 0)
        {
            return false;
        }

        String pattern= "^[a-zA-Z0-9 ]*$";
        if (text.matches(pattern))
        {
            return true;
        }

        return false;
    }
}
