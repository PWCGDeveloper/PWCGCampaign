package pwcg.gui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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

public class PWCGButtonFactory extends JButton
{
    private static final long serialVersionUID = 1L;

    public static JButton makePaperButton(String displayText, String commandText, String toolTipText,  ActionListener actionListener) throws PWCGException 
    {
        displayText = InternationalizationManager.getTranslation(displayText);
        toolTipText = InternationalizationManager.getTranslation(toolTipText);

        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        PWCGJButton button = makeButton(displayText, commandText, toolTipText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makeRedPaperButton(String displayText, String commandText, String toolTipText, ActionListener actionListener) throws PWCGException 
    {
        displayText = InternationalizationManager.getTranslation(displayText);
        toolTipText = InternationalizationManager.getTranslation(toolTipText);

        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.BRITISH_RED;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        PWCGJButton button = makeButton(displayText, commandText, toolTipText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makePaperButtonWithBorder(String displayText, String commandText, String toolTipText, ActionListener actionListener) throws PWCGException 
    {
        displayText = InternationalizationManager.getTranslation(displayText);
        toolTipText = InternationalizationManager.getTranslation(toolTipText);

        Color bgColor = ColorMap.PAPER_OFFSET;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        PWCGJButton button = makeButtonWithBorder(displayText, commandText, toolTipText, actionListener, bgColor, fgColor, font);

        return button;
    }

    public static JButton makeBriefingChalkBoardButton(String displayText, String commandText, String toolTipText, ActionListener actionListener) throws PWCGException 
    {
        displayText = InternationalizationManager.getTranslation(displayText);
        toolTipText = InternationalizationManager.getTranslation(toolTipText);

        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        
        JButton button = makeTranslucentMenuButtonGrayMenu(displayText, commandText, toolTipText, actionListener);
        button.setFont(font);
        button.setForeground(fgColor);

        return button;
    }

    public static  JButton makeTranslucentMenuButton(String displayText, String commandText, String toolTipText, ActionListener listener) throws PWCGException
    {
        displayText = InternationalizationManager.getTranslation(displayText);

        PWCGJButton button = ImageButton.makeTranslucentButton("TranslucentButton.png");
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        button.setText(displayText);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setActionCommand(commandText);
        button.addActionListener(listener);

        toolTipText = InternationalizationManager.getTranslation(toolTipText);
        ToolTipManager.setToolTip(button, toolTipText);

        return button;
    }

    public static  JButton makeTranslucentMenuButtonGrayMenu(String displayText, String commandText, String toolTipText, ActionListener listener) throws PWCGException
    {
        displayText = InternationalizationManager.getTranslation(displayText);

        PWCGJButton button = ImageButton.makeTranslucentButton("TranslucentButtonGrayMenu.png");
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        button.setText(displayText);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setActionCommand(commandText);
        button.addActionListener(listener);

        toolTipText = InternationalizationManager.getTranslation(toolTipText);
        ToolTipManager.setToolTip(button, toolTipText);

        return button;
    }

    public static PWCGJButton makeImageButton(ImageIcon imageIcon, Color background, String commandText, String noIcontext, String toolTipText, ActionListener listener) throws PWCGException
    {
        PWCGJButton crewMemberPictureButton = null;
        if (imageIcon != null)
        {
            crewMemberPictureButton = new PWCGJButton(imageIcon);
        }
        else
        {
            crewMemberPictureButton = new PWCGJButton(noIcontext);
        }
        crewMemberPictureButton.addActionListener(listener);
        crewMemberPictureButton.setBackground(background);
        crewMemberPictureButton.setOpaque(false);
        crewMemberPictureButton.setBorderPainted(false);
        crewMemberPictureButton.setFocusPainted(false);
        crewMemberPictureButton.setActionCommand(commandText);

        toolTipText = InternationalizationManager.getTranslation(toolTipText);
        ToolTipManager.setToolTip(crewMemberPictureButton, toolTipText);
        
        return crewMemberPictureButton;
    }

    public static JRadioButton makeRadioButton(String displayText, String commandText, String toolTipText, Font font, Color foreground, boolean selected, ActionListener actionListener) throws PWCGException
    {
        if (font == null)
        {
            font = PWCGMonitorFonts.getPrimaryFont();
        }
        
        displayText = InternationalizationManager.getTranslation(displayText);

        JRadioButton button= new JRadioButton(displayText);
        button.setActionCommand(commandText);
        button.setFont(font);
        button.setForeground(foreground);
        button.setSelected(selected);
        button.addActionListener(actionListener);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT );

        toolTipText = InternationalizationManager.getTranslation(toolTipText);
        ToolTipManager.setToolTip(button, toolTipText);

        return button;
    }

    public static JCheckBox makeCheckBox(String displayText, String actionCommand, Font font, Color fgColor, ActionListener actionListener) throws PWCGException 
    {
        displayText = InternationalizationManager.getTranslation(displayText);

        JCheckBox button = new JCheckBox(displayText);
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

    public static  JCheckBox makeCheckBoxWithDimensions(String name, String displayText, Font font, Dimension dimension) throws PWCGException 
    {
        displayText = InternationalizationManager.getTranslation(displayText);

        JCheckBox checkBox= new JCheckBox();
        checkBox.setFont(font);
        checkBox.setHorizontalAlignment(JLabel.LEFT);
        checkBox.setOpaque(false);
        checkBox.setSize(dimension.width, dimension.height);
        checkBox.setName(name);
        checkBox.setText(displayText);
        
        return checkBox;
    }

    public static JCheckBox makeCheckBoxWithIcon(Icon selectedIcon, Icon notSelectedIcon, String displayText, Font font, String imageName, Dimension dimension) throws PWCGException
    {
        displayText = InternationalizationManager.getTranslation(displayText);

        JCheckBox checkBox = new JCheckBox(displayText);
        checkBox.setFont(font);
        checkBox.setHorizontalAlignment(JLabel.LEFT);
        checkBox.setOpaque(false);
        checkBox.setSize(dimension.width, dimension.height);
        checkBox.setIcon(notSelectedIcon);
        checkBox.setSelectedIcon(selectedIcon);

        return checkBox;
    }

    private static PWCGJButton makeButton(
                    String displayText, 
                    String commandText, 
                    String toolTipText, 
                    ActionListener actionListener, 
                    Color bgColor,
                    Color fgColor, 
                    Font font) throws PWCGException
    {      
        PWCGJButton button = new PWCGJButton(displayText);
        button.setActionCommand(commandText);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setOpaque(false);
        button.setFont(font);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(actionListener);

        toolTipText = InternationalizationManager.getTranslation(toolTipText);
        ToolTipManager.setToolTip(button, toolTipText);
        
        return button;
    }


    private static PWCGJButton makeButtonWithBorder(
                    String displayText, 
                    String commandText, 
                    String toolTipText, 
                    ActionListener actionListener, 
                    Color bgColor,
                    Color fgColor, 
                    Font font) throws PWCGException
    {
        PWCGJButton button = new PWCGJButton(displayText);
        button.setActionCommand(commandText);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setOpaque(true);
        button.setFont(font);
        button.setBorderPainted(true);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addActionListener(actionListener);
        
        Border raisedBorder = BorderFactory.createSoftBevelBorder(BevelBorder.RAISED);
        button.setBorder(raisedBorder);
        
        toolTipText = InternationalizationManager.getTranslation(toolTipText);
        ToolTipManager.setToolTip(button, toolTipText);

        return button;
    }
}
