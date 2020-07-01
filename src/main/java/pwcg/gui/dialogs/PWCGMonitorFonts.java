package pwcg.gui.dialogs;

import java.awt.Font;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;

public class PWCGMonitorFonts 
{
	public static Font getPrimaryFont() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
            
		Font font = new Font(
				fontName, 
				Font.BOLD, 
				fontSize);
		
		return font;
	}

    public static int getPilotPlateHeight() throws PWCGException
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

	public static Font getPrimaryFontSmall() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontSmallSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
            
		Font font = new Font(
				fontName, 
				Font.BOLD, 
				fontSize);
		
		return font;
	}

	public static Font getPrimaryFontSmallNotBold() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontSmallSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
            
		Font font = new Font(
				fontName, 
				Font.TRUETYPE_FONT, 
				fontSize);
		
		return font;
	}

	public static Font getPrimaryFontLarge() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontLargeSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
		
		Font font = new Font(
				fontName, 
				Font.BOLD, 
				fontSize);
		
		return font;
	}

	public static Font getMissionDescriptionFont() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.MissionDescriptionFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.MissionDescriptionFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
		Font font = new Font(
				fontName, 
				Font.BOLD, 
				fontSize);

		return font;
	}

    public static Font getChalkboardFont() throws PWCGException
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.ChalkboardFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ChalkboardFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        
        return font;
    }

    public static Font getBriefingChalkboardFont() throws PWCGException
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.BriefingFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.BriefingFontSizeKey);
        fontSize = verifyFontSizeForCrowdedScreenSize(fontSize);
        
        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        
        return font;
    }

	public static Font getPilotFont() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PilotFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PilotFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
		Font font = new Font(
				fontName, 
				Font.BOLD, 
				fontSize);
		
		return font;
	}

    public static Font getPilotLogBookFont() throws PWCGException 
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PilotFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PilotFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        
        return font;
    }

    public static Font getTypewriterFont() throws PWCGException 
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.TypewriterFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.TypewriterFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        
        return font;
    }

    public static Font getCursiveFont() throws PWCGException 
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.CursiveFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.CursiveFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);
        
        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        
        return font;
    }

    public static Font getDecorativeFont() throws PWCGException 
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.DecorativeFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.DecorativeFontSizeKey);
        fontSize = verifyFontSizeForScreenSize(fontSize);

        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        
        return font;
    }

	public static Font getSpecialFont(String fontName, int fontStyle, int fontSize) 
	{
		Font font = new Font(
				fontName, 
				fontStyle, 
				fontSize);
		
		return font;
	}

    private static int verifyFontSizeForScreenSize(int fontSize)
    {
        if (PWCGMonitorSupport.isVerySmallScreen())
        {
            if (fontSize > 14)
            {
                fontSize = 14;
            }
        }
        else if (PWCGMonitorSupport.isSmallScreen())
        {
            if (fontSize > 16)
            {
                fontSize = 16;
            }
        }
        else if (PWCGMonitorSupport.isMediumScreen())
        {
            if (fontSize > 22)
            {
                fontSize = 22;
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
