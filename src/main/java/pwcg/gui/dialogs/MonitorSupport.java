package pwcg.gui.dialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.PWCGFrame;

public class MonitorSupport 
{
	public static Dimension getPWCGFrameSize()
	{
        Dimension frameSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        
	    Rectangle r = PWCGFrame.getInstance().getBounds();
	    frameSize.setSize(r.width, r.height);	    

		return frameSize;
	}

    public static Dimension getPWCGMonitorSize()
    {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height -= 40;
        
        // Or use the user specified value
        int screenSizeAuto;
        try
        {
            screenSizeAuto = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ScreenSizeAutoKey);
            if (screenSizeAuto == 0)
            {
                screenSize.height = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ScreenSizeHeightKey);
                screenSize.width = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.ScreenSizeWidthKey);
            }
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }


        return screenSize;
    }

    public static Insets calculateInset(int top, int left, int bottom, int right)
    {
        // Calculate the writable area of the text and generate margins scaled to screen size
        int marginTop = MonitorSupport.calculateInsetVertical(top);
        int marginLeft = MonitorSupport.calculateInsetHorixontal(left);
        int marginBottom = MonitorSupport.calculateInsetVertical(bottom);
        int marginRight = MonitorSupport.calculateInsetHorixontal(right);
        
        return new Insets(marginTop, marginLeft, marginBottom, marginRight);
    }

    public static int calculateInsetVertical(int insetBaseValue)
    {
        double ratioDivisor = getScreenRatioDivisor();

        Dimension screenSize = MonitorSupport.getPWCGFrameSize();
        Double screenHeight = Double.valueOf(screenSize.height);
        Double screenRatio = screenHeight / ratioDivisor;
        Double insetVertical = insetBaseValue * screenRatio;

        return insetVertical.intValue();
    }

    public static int calculateInsetHorixontal(int insetBaseValue)
    {
        double ratioDivisor = getScreenRatioDivisor();
        
        Dimension screenSize = MonitorSupport.getPWCGFrameSize();
        Double screenWidth = Double.valueOf(screenSize.width);
        Double screenRatio = screenWidth / ratioDivisor;
        Double insetHorizontal = insetBaseValue * screenRatio;

        return insetHorizontal.intValue();
    }

    private static double getScreenRatioDivisor()
    {
        double ratioDivisor = 900.0;
        Dimension monitorSize = getPWCGMonitorSize();
        
        if (monitorSize.getWidth() > 3000)
        {
            ratioDivisor = 600.0;
        }
        else if (monitorSize.getWidth() > 2000)
        {
            ratioDivisor = 700.0;
        }
        else if (monitorSize.getWidth() > 1700)
        {
            ratioDivisor = 800;
        }
        else if (monitorSize.getWidth() > 1200)
        {
            ratioDivisor = 900;
        }
        else
        {
            ratioDivisor = 1000;
        }
        
        return ratioDivisor;
    }

	public static Font getPrimaryFont() throws PWCGException
	{
	    // for small screens override medium font with small
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontSizeKey);
		
        if (isSmallScreen())
        {
            if (fontSize > 12)
            {
                fontSize = 12;
            }
        }
            
		Font font = new Font(
				fontName, 
				Font.BOLD, 
				fontSize);
		
		return font;
	}

    public static int getPilotPlateHeight() throws PWCGException
    {
        if (getPWCGFrameSize().getHeight() < 900)
        {
            return 25;
        }
        else if (getPWCGFrameSize().getHeight() < 1100)
        {
            return 30;
        }
        
        return 40;
    }

	public static Font getPrimaryFontSmall() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontSmallSizeKey);
		
        if (isSmallScreen())
        {
            if (fontSize > 10)
            {
                fontSize = 10;
            }
        }
            
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
		
        if (isSmallScreen())
        {
            if (fontSize > 10)
            {
                fontSize = 10;
            }
        }
            
		Font font = new Font(
				fontName, 
				Font.TRUETYPE_FONT, 
				fontSize);
		
		return font;
	}

	public static Font getPrimaryFontLarge() throws PWCGException
	{
        // for small screens override lmedium font with small
        if (isSmallScreen())
        {
            return getPrimaryFont();
        }            
            
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PrimaryFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PrimaryFontLargeSizeKey);
        
        if (isSmallScreen())
        {
            if (fontSize > 16)
            {
                fontSize = 16;
            }
        }
		
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
		
        if (isSmallScreen())
        {
            if (fontSize > 10)
            {
                fontSize = 10;
            }
        }
        
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
        
        if (isSmallScreen())
        {
            if (fontSize > 18)
            {
                fontSize = 18;
            }
        }
        
        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        
        return font;
    }

    /**
     * @return
     * @
     */
    public static Font getBriefingChalkboardFont() throws PWCGException
    {
        String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.BriefingFontKey);
        int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.BriefingFontSizeKey);
        
        if (isSmallScreen())
        {
            if (fontSize > 16)
            {
                fontSize = 16;
            }
        }
        
        Font font = new Font(
                fontName, 
                Font.BOLD, 
                fontSize);
        
        return font;
    }
	
	/**
	 * @return
	 * @
	 */
	public static Font getPilotFont() throws PWCGException
	{
		String fontName = ConfigManagerGlobal.getInstance().getStringConfigParam(ConfigItemKeys.PilotFontKey);
		int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.PilotFontSizeKey);
		
        if (isSmallScreen())
        {
            if (fontSize > 16)
            {
                fontSize = 16;
            }
        }
        
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
        
        if (isSmallScreen())
        {
            if (fontSize > 16)
            {
                fontSize = 16;
            }
        }
        
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
        
        if (isSmallScreen())
        {
            if (fontSize > 12)
            {
                fontSize = 12;
            }
        }
        
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
        
        if (isSmallScreen())
        {
            if (fontSize > 14)
            {
                fontSize = 14;
            }
        }
        
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
        
        if (isSmallScreen())
        {
            if (fontSize > 16)
            {
                fontSize = 16;
            }
        }
        
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

	public static boolean isSmallScreen()
	{
        if (getPWCGFrameSize().getHeight() < 900)
        {
            return true;
        }
        
        return false;
	}

}
