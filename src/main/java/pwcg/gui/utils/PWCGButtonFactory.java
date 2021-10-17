package pwcg.gui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;

public class PWCGButtonFactory extends JButton
{
    private static final long serialVersionUID = 1L;

    public static JButton makePaperButton(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        PWCGJButton button = makeButton(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makeRedPaperButton(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.BRITISH_RED;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        PWCGJButton button = makeButton(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makePaperButtonWithBorder(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_OFFSET;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        PWCGJButton button = makeButtonWithBorder(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makeChalkBoardButton(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();
        
        PWCGJButton button = makeButton(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makeBriefingChalkBoardButton(String buttonText, String commandText, String toolTipText, ActionListener actionListener) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        
        JButton button = makeTranslucentMenuButtonGrayMenu(buttonText, commandText, toolTipText, actionListener);
        button.setFont(font);
        button.setForeground(fgColor);

        return button;
    }

    public static  JButton makeTranslucentMenuButton(String buttonText, String commandText, String toolTipText, ActionListener listener) throws PWCGException
    {
        PWCGJButton button = ImageButton.makeTranslucentButton("TranslucentButton.png");
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        button.setText(InternationalizationManager.getTranslation(buttonText));
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setActionCommand(commandText);
        button.addActionListener(listener);

        ToolTipManager.setToolTip(button, toolTipText);
        return button;
    }

    public static  JButton makeTranslucentMenuButtonGrayMenu(String buttonText, String commandText, String toolTipText, ActionListener listener) throws PWCGException
    {
        PWCGJButton button = ImageButton.makeTranslucentButton("TranslucentButtonGrayMenu.png");
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        button.setText(buttonText);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setActionCommand(commandText);
        button.addActionListener(listener);

        ToolTipManager.setToolTip(button, toolTipText);
        return button;
    }

    public static JRadioButton makeRadioButton(String buttonName, String action, String toolTipText, boolean selected, ActionListener actionListener, Color fg) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button= new JRadioButton(buttonName);
        button.setOpaque(false);
        button.setActionCommand(action);
        button.addActionListener(actionListener);
        button.setFont(font);
        button.setForeground(fg);

        ToolTipManager.setToolTip(button, toolTipText);

        return button;
    }

    public static JRadioButton makeBriefingChalkBoardRadioButton(String action, ActionListener actionListener) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        
        JRadioButton button= new JRadioButton();
        button.setOpaque(false);
        button.setActionCommand(action);
        button.addActionListener(actionListener);
        button.setFont(font);
        button.setForeground(ColorMap.CHALK_FOREGROUND);

        return button;
    }


    public static JLabel makeDummy()
    {
        JLabel lDummy = new JLabel("     ");
        lDummy.setOpaque(false);
        
        return lDummy;
    }

    private static PWCGJButton makeButton(
                    String buttonText, 
                    String commandText, 
                    ActionListener actionListener, 
                    Color bgColor,
                    Color fgColor, 
                    Font font)
    {
        PWCGJButton button = new PWCGJButton(buttonText);
        button.setActionCommand(commandText);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setOpaque(false);
        button.setFont(font);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(actionListener);
        return button;
    }


    private static PWCGJButton makeButtonWithBorder(
                    String buttonText, 
                    String commandText, 
                    ActionListener actionListener, 
                    Color bgColor,
                    Color fgColor, 
                    Font font)
    {
        PWCGJButton button = new PWCGJButton(buttonText);
        button.setActionCommand(commandText);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setOpaque(true);
        button.setFont(font);
        button.setBorderPainted(true);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addActionListener(actionListener);
        
        //Border raisedBorder = BorderFactory.createRaisedBevelBorder();
        //button.setBorder(new RoundedBorder(8)); //10 is the radius
        Border raisedBorder = BorderFactory.createSoftBevelBorder(BevelBorder.RAISED);
        button.setBorder(raisedBorder); 

        return button;
    }

    public static JLabel makeMenuLabelLarge(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        labelText = padStringToExtendImageSize(labelText);
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makePaperLabelLarge(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        labelText = padStringToExtendImageSize(labelText);
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makePlaqueLabelLarge(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.PLAQUE_GOLD;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        labelText = padStringToExtendImageSize(labelText);
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makePaperLabelMedium(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makeChalkBoardLabel(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();
        
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makeBriefingChalkBoardLabel(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    private static JLabel makeLabel(String labelText, Color bgColor, Color fgColor, Font font)
    {
        JLabel label = new JLabel(labelText);
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setOpaque(false);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    public static String padStringToExtendImageSize(String originalString)
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

    public static JCheckBox makeCheckBox(String buttonText, Color fgColor) throws PWCGException 
    {
        return makeCheckBox(buttonText, "", fgColor, null);
    }

    public static JCheckBox makeCheckBox(String buttonText, String actionCommand, Color fgColor, ActionListener actionListener) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();

        JCheckBox button = new JCheckBox(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);
        if (actionListener != null)
        {
            button.setActionCommand(actionCommand);
            button.addActionListener(actionListener);
        }
        return button;
    }

    public static JCheckBox makeSmallCheckBox(String buttonText, String actionCommand, Color fgColor, ActionListener actionListener) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        JCheckBox button = new JCheckBox(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setActionCommand(actionCommand);
        button.addActionListener(actionListener);

        return button;
    }

}
