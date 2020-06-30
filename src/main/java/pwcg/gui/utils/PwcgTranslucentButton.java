package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;

public class PwcgTranslucentButton
{
    public static JPanel makeTranslucentButton(ActionListener actionListener, String text, String action, String toolTiptext) throws PWCGException
    {

        String imagePath = ContextSpecificImages.imagesMisc() + "TranslucentButton.png";
        ImageResizingPanel translucentButtonImagePanel = new ImageResizingPanel(imagePath);
        translucentButtonImagePanel.setLayout(new BorderLayout());

        JButton translucentButton = new PWCGButtonNoBackground();
        translucentButton.setForeground(ColorMap.CHALK_FOREGROUND);
        translucentButton.setOpaque(false);
        translucentButton.setFont(PWCGMonitorFonts.getPrimaryFont());
        translucentButton.setHorizontalAlignment(SwingConstants.LEFT);
        translucentButton.setActionCommand(action);
        translucentButton.addActionListener(actionListener);
        translucentButton.setBorderPainted(false);
        translucentButton.setFocusPainted(false);
        translucentButton.setText(text);

        translucentButtonImagePanel.add(translucentButton, BorderLayout.CENTER);

        JPanel translucentButtonPanel = new JPanel(new BorderLayout());
        translucentButtonPanel.setOpaque(false);
        translucentButtonPanel.add(translucentButtonImagePanel, BorderLayout.CENTER);

        ToolTipManager.setToolTip(translucentButton, toolTiptext);

        return translucentButtonPanel;
    }

}
