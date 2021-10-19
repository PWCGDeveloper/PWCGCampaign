package pwcg.gui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;

public class PWCGLabelFactory extends JButton
{
    private static final long serialVersionUID = 1L;

    public static JLabel makeDummyLabel()
    {
        JLabel lDummy = new JLabel("     ");
        lDummy.setOpaque(false);
        return lDummy;
    }

    public static JLabel makeMenuLabelLarge(String displayText) throws PWCGException
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        JLabel label = makeLabel(displayText, bgColor, fgColor, font);
        return label;
    }

    public static JLabel makePaperLabelLarge(String displayText) throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        JLabel label = makeLabel(displayText, bgColor, fgColor, font);
        return label;
    }

    public static JLabel makeLabel(String displayText, Color bgColor, Color fgColor, Font font) throws PWCGException
    {
        displayText = InternationalizationManager.getTranslation(displayText);
        displayText = padStringToExtendImageSize(displayText);

        JLabel label = new JLabel(displayText);
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setOpaque(false);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    public static JLabel makeImageLabel(ImageIcon icon)
    {
        JLabel imageLabel= new JLabel(icon);
        return imageLabel;
    }

    private static String padStringToExtendImageSize(String originalString)
    {
        String paddedString = originalString;
        
        Dimension screenSize = PWCGMonitorSupport.getPWCGFrameSize();
        Double pixelsToUseDouble = screenSize.width * .2;
        int pixelsToUse = pixelsToUseDouble.intValue();
        
        int charactersToUse = pixelsToUse / 8;
        
        if (originalString.length() < charactersToUse)
        {
            for (int i = originalString.length(); i < charactersToUse; ++i)
            {
                paddedString += " ";
            }
        }
        
        return paddedString;
    }
}
