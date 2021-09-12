package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javax.swing.SwingConstants;

import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;

public class PwcgTranslucentButton
{
    public static Pane makeTranslucentButton(ActionListener actionListener, String text, String action, String toolTiptext) throws PWCGException
    {

        String imagePath = ContextSpecificImages.imagesMisc() + "TranslucentButton.png";
        ImageResizingPanel translucentButtonImagePanel = new ImageResizingPanel(imagePath);
        translucentButtonImagePanel.setLayout(new BorderLayout());

        Button translucentButton = new ButtonNoBackground();
        translucentButton.setForeground(ColorMap.CHALK_FOREGROUND);
        translucentButton.setOpaque(false);
        translucentButton.setFont(PWCGMonitorFonts.getPrimaryFont());
        translucentButton.setAlignment(SwingConstants.LEFT);
        translucentButton.setActionCommand(action);
        translucentButton.addActionListener(actionListener);
        translucentButton.setBorderPainted(false);
        translucentButton.setFocusPainted(false);
        translucentButton.setText(text);

        translucentButtonImagePanel.add(translucentButton, BorderLayout.CENTER);

        Pane translucentButtonPanel = new Pane(new BorderLayout());
        translucentButtonPanel.setOpaque(false);
        translucentButtonPanel.add(translucentButtonImagePanel, BorderLayout.CENTER);

        ToolTipManager.setToolTip(translucentButton, toolTiptext);

        return translucentButtonPanel;
    }

}
