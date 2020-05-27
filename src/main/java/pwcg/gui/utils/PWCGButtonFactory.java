package pwcg.gui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.MonitorSupport;

public class PWCGButtonFactory extends JButton
{
    private static final long serialVersionUID = 1L;

    public static JButton makeMenuButton(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        
        Font font = MonitorSupport.getPrimaryFontLarge();
        
        buttonText = padStringToExtendImageSize(buttonText);
        PWCGJButton button = makeButton(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makePaperButton(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = MonitorSupport.getPrimaryFont();
        
        PWCGJButton button = makeButton(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makePaperButtonWithBorder(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_OFFSET;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = MonitorSupport.getPrimaryFont();
        
        PWCGJButton button = makeButtonWithBorder(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makeChalkBoardButton(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = MonitorSupport.getChalkboardFont();
        
        PWCGJButton button = makeButton(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makeBriefingChalkBoardButton(String buttonText, String commandText, ActionListener actionListener) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = MonitorSupport.getBriefingChalkboardFont();
        
        PWCGJButton button = makeButton(buttonText, commandText, actionListener, bgColor, fgColor, font);

        return button;
    }
    

    public static JRadioButton makeRadioButton(String buttonName, String action, String toolTipText, boolean selected, ActionListener actionListener) throws PWCGException
    {
        Color bg = ColorMap.WOOD_BACKGROUND;
        Color fg = ColorMap.CHALK_FOREGROUND;

        Font font = MonitorSupport.getPrimaryFont();

        JRadioButton button= new JRadioButton(buttonName);
        button.setOpaque(false);
        button.setActionCommand(action);
        button.addActionListener(actionListener);
        button.setFont(font);
        button.setBackground(bg);
        button.setForeground(fg);

        ToolTipManager.setToolTip(button, toolTipText);

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
        Font font = MonitorSupport.getPrimaryFontLarge();
        
        labelText = padStringToExtendImageSize(labelText);
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makePaperLabelLarge(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = MonitorSupport.getPrimaryFontLarge();
        
        labelText = padStringToExtendImageSize(labelText);
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makePlaqueLabelLarge(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.PLAQUE_GOLD;
        Font font = MonitorSupport.getPrimaryFontLarge();
        
        labelText = padStringToExtendImageSize(labelText);
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makePaperLabelMedium(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = MonitorSupport.getPrimaryFont();
        
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makeChalkBoardLabel(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = MonitorSupport.getChalkboardFont();
        
        JLabel label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static JLabel makeBriefingChalkBoardLabel(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = MonitorSupport.getBriefingChalkboardFont();
        
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
        
        Dimension screenSize = MonitorSupport.getPWCGFrameSize();
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

    public static JCheckBox makeCheckBox(String buttonText, String actionCommand, Color fgColor, ActionListener actionListener) throws PWCGException 
    {
        Font font = MonitorSupport.getPrimaryFont();

        JCheckBox button = new JCheckBox(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setActionCommand(actionCommand);
        button.addActionListener(actionListener);

        return button;
    }

    public static JCheckBox makeSmallCheckBox(String buttonText, String actionCommand, Color fgColor, ActionListener actionListener) throws PWCGException 
    {
        Font font = MonitorSupport.getPrimaryFontSmall();

        JCheckBox button = new JCheckBox(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setActionCommand(actionCommand);
        button.addActionListener(actionListener);

        return button;
    }

}
