package pwcg.gui.dialogs;

import java.awt.Font;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;

public class PWCGMonitorFonts 
{
    public static final int LARGE_SCREEN_FONT_SIZE = 22;
    public static final int MEDIUM_SCREEN_FONT_SIZE = 16;
    public static final int SMALL_SCREEN_FONT_SIZE = 14;

    public enum PWCGFonts
    {
        PrimaryFont,
        PrimaryFontSmall,
        PrimaryFontLarge,
        ChalkBoardFont,
        ChalkBoardBriefingFont,
        CrewMemberLogBookFont,
        NewspaperFont,
        TyoewriterFont,
        DecorativeFint,
        CursiveFont
    }
    
    public static Font getPrimaryFont() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
            
		Font font = buildBoldFont(fontName, fontSize);
		
		return font;
	}

	public static Font getPrimaryFontSmall() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontSmallSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
            
		Font font = buildBoldFont(fontName, fontSize);
		
		return font;
	}

	public static Font getPrimaryFontLarge() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontLargeSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
		
		Font font = buildBoldFont(fontName, fontSize);
		
		return font;
	}

	public static Font getChalkboardFont() throws PWCGException
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.ChalkboardFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ChalkboardFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
        Font font = buildBoldFont(fontName, fontSize);
        
        return font;
    }

    public static Font getBriefingChalkboardFont() throws PWCGException
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.BriefingFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.BriefingFontSizeKey);
        fontSize = verifyFontSizeForCrowdedScreenSize(fontSize);
        
        Font font = buildBoldFont(fontName, fontSize);
        
        return font;
    }

    public static Font getNewspaperFont() throws PWCGException
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.NewspaperFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.NewspaperFontSizeKey);
        
        Font font = buildTrueTypeFont(fontName, fontSize);
        
        return font;
    }

    public static Font getCrewMemberLogBookFont() throws PWCGException 
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.CrewMemberFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.CrewMemberFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
        Font font = buildBoldFont(fontName, fontSize);
        
        return font;
    }

    public static Font getTypewriterFont() throws PWCGException 
    {
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.TypewriterFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);                
        return getTypewriterFontWithSize(fontSize);
    }

    public static Font getTypewriterFontWithSize(int fontSize) throws PWCGException 
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.TypewriterFontKey);
        Font font = buildBoldFont(fontName, fontSize);        
        return font;
    }

    public static Font getCursiveFont() throws PWCGException 
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.CursiveFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.CursiveFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
        Font font = buildBoldFont(fontName, fontSize);
        
        return font;
    }

    public static Font getDecorativeFont() throws PWCGException 
    {
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.DecorativeFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);        
        return getDecorativeFontWithSize(fontSize);
    }

    public static Font getDecorativeFontWithSize(int fontSize) throws PWCGException 
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.DecorativeFontKey);        
        Font font = buildBoldFont(fontName, fontSize);
        return font;
    }

    private static Font buildBoldFont(String fontName, int fontSize)
    {
        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        return font;
    }

    private static Font buildTrueTypeFont(String fontName, int fontSize)
    {
        Font font = new Font(
                fontName, 
                Font.TRUETYPE_FONT, 
                fontSize);
        return font;
    }

    public static int getCrewMemberPlateHeight() throws PWCGException
    {
        if (PWCGMonitorSupport.isSmallScreen())
        {
            return 25;
        }
        else if (PWCGMonitorSupport.isMediumScreen())
        {
            return 30;
        }
        
        return 40;
    }

    private static int verifyFontSizeForScreenSize(int fontSize)
    {
        if (PWCGMonitorSupport.isVerySmallScreen())
        {
            if (fontSize > SMALL_SCREEN_FONT_SIZE)
            {
                fontSize = SMALL_SCREEN_FONT_SIZE;
            }
        }
        else if (PWCGMonitorSupport.isSmallScreen())
        {
            if (fontSize > MEDIUM_SCREEN_FONT_SIZE)
            {
                fontSize = MEDIUM_SCREEN_FONT_SIZE;
            }
        }
        else if (PWCGMonitorSupport.isMediumScreen())
        {
            if (fontSize > LARGE_SCREEN_FONT_SIZE)
            {
                fontSize = LARGE_SCREEN_FONT_SIZE;
            }
        }
        return fontSize;
    }

    private static int verifyFontSizeForCrowdedScreenSize(int fontSize)
    {
        if (PWCGMonitorSupport.isVerySmallScreen())
        {
            if (fontSize > 12)
            {
                fontSize = 12;
            }
        }
        else if (PWCGMonitorSupport.isSmallScreen())
        {
            if (fontSize > 14)
            {
                fontSize = 14;
            }
        }
        else if (PWCGMonitorSupport.isMediumScreen())
        {
            if (fontSize > 20)
            {
                fontSize = 20;
            }
        }
        return fontSize;
    }
}
