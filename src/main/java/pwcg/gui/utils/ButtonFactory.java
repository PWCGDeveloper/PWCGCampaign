package pwcg.gui.utils;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import com.sun.java.swing.plaf.motif.MotifBorders.BevelBorder;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;

public class ButtonFactory extends Button
{
    public static Button makePaperButton(String buttonText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        Button button = makeButton(buttonText, bgColor, fgColor, font);

        return button;
    }

    public static Button makeRedPaperButton(String buttonText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.BRITISH_RED;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        Button button = makeButton(buttonText, bgColor, fgColor, font);

        return button;
    }

    public static Button makePaperButtonWithBorder(String buttonText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_OFFSET;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        Button button = makeButtonWithBorder(buttonText, bgColor, fgColor, font);

        return button;
    }

    public static Button makeChalkBoardButton(String buttonText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();
        Button button = makeButton(buttonText, bgColor, fgColor, font);

        return button;
    }

    public static Button makeBriefingChalkBoardButton(String buttonText, String toolTipText) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        Button button = makeTranslucentMenuButtonGrayMenu(buttonText, toolTipText);
        button.setFont(font);

        return button;
    }

    public static  Button makeTranslucentMenuButton(String buttonText, String toolTipText) throws PWCGException
    {
        Button button = ImageButton.makeTranslucentButton("TranslucentButton.png");
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        button.setText(buttonText);
        // JAVAFX 
        //button.setVerticalTextPosition(AbstractButton.CENTER);
        //button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setFont(font);

        ToolTipManager.setToolTip(button, toolTipText);
        return button;
    }

    public static  Button makeTranslucentMenuButtonGrayMenu(String buttonText, String toolTipText) throws PWCGException
    {
        Button button = ImageButton.makeTranslucentButton("TranslucentButtonGrayMenu.png");
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        button.setText(buttonText);
        button.setAlignment(Pos.CENTER);

        // JAVAFX need equivalents
        //button.setForeground(fgColor);
        button.setFont(font);

        Tooltip tooltip = new Tooltip(toolTipText);
        button.setTooltip(tooltip);
        return button;
    }

    public static RadioButton  makeRadioButton(String buttonName, String toolTipText, boolean selected, Color fg) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();

        RadioButton  button= new RadioButton (buttonName);
        // JAVAFX need equivalents
//      button.setOpaque(false);
//      button.setForeground(ColorMap.CHALK_FOREGROUND);
        button.setFont(font);

        Tooltip tooltip = new Tooltip(toolTipText);
        button.setTooltip(tooltip);

        return button;
    }

    public static RadioButton  makeBriefingChalkBoardRadioButton() throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        
        RadioButton  button= new RadioButton ();
        // JAVAFX need equivalents
//        button.setOpaque(false);
//        button.setForeground(ColorMap.CHALK_FOREGROUND);
        button.setFont(font);

        return button;
    }


    public static Label makeDummy()
    {
        Label lDummy = new Label("     ");
        // JAVAFX need equivalents
        // lDummy.setOpaque(false);
        
        return lDummy;
    }

    private static Button makeButton(
                    String buttonText, 
                    Color bgColor,
                    Color fgColor, 
                    Font font)
    {
        Button button = new Button(buttonText);
        
        // JAVAFX need equivalents
//        button.setBackground(bgColor);
//        button.setForeground(fgColor);
//        button.setOpaque(false);
//        button.setBorderPainted(false);
        
        button.setFont(font);
        button.setAlignment(Pos.CENTER_LEFT);
        return button;
    }

    public static Label makeMenuLabelLarge(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        Label label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static Label makePaperLabelLarge(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        Label label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static Label makePlaqueLabelLarge(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.PLAQUE_GOLD;
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        
        Label label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static Label makePaperLabelMedium(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        
        Label label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static Label makeChalkBoardLabel(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();
        
        Label label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    public static Label makeBriefingChalkBoardLabel(String labelText) throws PWCGException 
    {
        Color bgColor = ColorMap.CHALK_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getBriefingChalkboardFont();
        
        Label label = makeLabel(labelText, bgColor, fgColor, font);
        
        return label;
    }

    private static Label makeLabel(String labelText, Color bgColor, Color fgColor, Font font)
    {
        Label label = new Label(labelText);
        label.setFont(font);
        label.setAlignment(Pos.CENTER_LEFT);

        // JAVAFX need equivalents
//      button.setBackground(bgColor);
//      button.setForeground(fgColor);
//      button.setOpaque(false);

        return label;
    }

    public static CheckBox makeCheckBox(String buttonText, Color fgColor) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getPrimaryFont();

        CheckBox button = new CheckBox(buttonText);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setFont(font);
        
        // JAVAFX need equivalents
//      button.setBackground(bgColor);
//      button.setForeground(fgColor);
//      button.setOpaque(false);
//        button.setFocusPainted(false);
        return button;
    }

    public static CheckBox makeSmallCheckBox(String buttonText, Color fgColor) throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        CheckBox button = new CheckBox(buttonText);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setFont(font);

        // JAVAFX need equivalents
//        button.setBorderPainted(false);
//        button.setFocusPainted(false);
//        button.setOpaque(false);
//        button.setForeground(fgColor);

        return button;
    }

}
