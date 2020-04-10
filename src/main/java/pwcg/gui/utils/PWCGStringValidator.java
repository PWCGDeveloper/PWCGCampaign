package pwcg.gui.utils;

public class PWCGStringValidator
{
    public static boolean validateStringIsAlpha(String data)
    {
        if (data == null) {
            return false;
        }

        if (data.length() == 0) {
            return false;
        }

        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z') &&!(c == ' ')) {
                return false;
            }
        }
        return true;
    }
}
