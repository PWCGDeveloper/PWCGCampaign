package pwcg.gui.dialogs;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class FontCache
{
    private static FontCache instance = null;
    private static HashMap<String, Font> fontCache = new HashMap<>();

    private FontCache ()
    {
    }

    public static FontCache getInstance()
    {
        if (instance == null)
        {
            instance = new FontCache();
        }

        return instance;
    }

    public Font getFont(String fontPath, float size) throws PWCGIOException
    {
        try
        {
            Font font = fontCache.get(fontPath);
            if (font == null)
            {
                File file = new File(fontPath);
                if (file.exists())
                {
                    font = Font.createFont(Font.TRUETYPE_FONT, file);
                    Map<TextAttribute, Object> attributes = new HashMap<>();
                    attributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
                    attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
                    font = font.deriveFont(attributes);
                    fontCache.put(fontPath, font);
                }
                else
                {
                    PWCGLogger.log(LogLevel.ERROR, "Font not found: " + fontPath);
                }
            }

            if (font == null)
                return null;
            else
                return font.deriveFont(size);
        }
        catch (IOException | FontFormatException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}
