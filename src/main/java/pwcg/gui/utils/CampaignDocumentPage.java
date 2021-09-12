package pwcg.gui.utils;

import java.awt.BorderLayout;
import javafx.scene.text.Font;
import java.awt.Insets;

import javax.swing.JComponent;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;


public class CampaignDocumentPage extends Pane
{
    private static final long serialVersionUID = 1L;

    public CampaignDocumentPage()
    {
        super();
        
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    public void formDocument(String headerText, String bodyText) throws PWCGException
    {
        JComponent textHeaderPanel = formHeaderTextBox(headerText);
        this.add(textHeaderPanel, BorderLayout.NORTH);
        
        JComponent textPanel = formTextBox(bodyText);
        this.add(textPanel, BorderLayout.CENTER);
        
        return;
    }

    private JComponent formHeaderTextBox(String headerText) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getDecorativeFont();
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(20, 30, 50, 50);

        JTextArea tHeader = new JTextArea();
        tHeader.setOpaque(false);
        tHeader.setEditable(false);
        tHeader.setText(headerText);
        tHeader.setFont(font);
        tHeader.setMargin(margins);

        return tHeader;
    }

    private JComponent formTextBox(String bodyText) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(20, 30, 50, 50);

        JTextArea tText = new JTextArea();
        tText.setOpaque(false);
        tText.setEditable(false);
        tText.setLineWrap(true);
        tText.setWrapStyleWord(true);
        tText.setText(bodyText);
        tText.setFont(font);
        tText.setMargin(margins);

        JScrollPane textScrollPane = ScrollBarWrapper.makeScrollPaneForWrappingText(tText);
        textScrollPane.setViewportView(tText);

        return textScrollPane;
    }

}
