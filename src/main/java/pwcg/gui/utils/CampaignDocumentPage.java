package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.core.exception.PWCGException;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;

/**
 * CampaignReportGUI acts as both a primary panel when the user want a  and as
 * a secondary panel when another pilot wants a .  Because of this it extends CampaignEventPanel
 * and sets shouldDisplay to true.  shouldDisplay is not actually used when this object is being
 * created in a secondary role
 * 
 * @author Patrick Wilson
 *
 */
public class CampaignDocumentPage extends JPanel
{
    private static final long serialVersionUID = 1L;


    /**
     * Constructor for player  only
     * 
     * @param campaignMissionResults
     */
    public CampaignDocumentPage()
    {
        super();
        
        setLayout(new BorderLayout());
        setOpaque(false);
    }

        
    /**
     * @param Event
     * @return
     * @throws PWCGException 
     */
    public void formDocument(String headerText, String bodyText) throws PWCGException
    {
        JComponent textHeaderPanel = formHeaderTextBox(headerText);
        this.add(textHeaderPanel, BorderLayout.NORTH);
        
        JComponent textPanel = formTextBox(bodyText);
        this.add(textPanel, BorderLayout.CENTER);
        
        return;
    }

    
    /**
     * @param Event
     * @return
     * @throws PWCGException 
     */
    private JComponent formHeaderTextBox(String headerText) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getDecorativeFont();
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(10, 20, 10, 20);

        JTextArea tHeader = new JTextArea();
        tHeader.setOpaque(false);
        tHeader.setEditable(false);
        tHeader.setText(headerText);
        tHeader.setFont(font);
        tHeader.setMargin(margins);

        return tHeader;
    }

    
    /**
     * @return
     * @throws PWCGException 
     */
    private JComponent formTextBox(String bodyText) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(5, 20, 5, 20);

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
