package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.utils.ContextSpecificImages;

public class CampaignReportMedalGUI extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private MedalEvent medalEvent = null;
	private SquadronMember medalRecipient;

	public CampaignReportMedalGUI(Campaign campaign, MedalEvent medalEvent) throws PWCGException
	{
	    this.setLayout(new BorderLayout());
	    this.setOpaque(false);

        this.medalEvent = medalEvent;
		this.medalRecipient = campaign.getPersonnelManager().getAnyCampaignMember(medalEvent.getPilotSerialNumber());
			
		makeGUI();		
	}
	
	private void makeGUI() 
	{		
		try
		{
			JPanel medalAwardPanel = formMedaUI();
	        this.add(medalAwardPanel, BorderLayout.CENTER);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}		
	}

    private JPanel formMedaUI() throws PWCGException
    {
        JPanel medalTextAndPilotPicPanel = new JPanel(new BorderLayout());
        medalTextAndPilotPicPanel.setOpaque(false);
     
        int leftRightBorder = getLeftRightBorder();        
        int topBottomBorder = getTopBottomBorder();

        medalTextAndPilotPicPanel.setBorder(BorderFactory.createEmptyBorder(topBottomBorder, leftRightBorder, topBottomBorder,leftRightBorder)); 

        Campaign campaign = PWCGContext.getInstance().getCampaign();
	    ICountry country = CountryFactory.makeCountryByCountry(medalRecipient.getCountry());
        Medal medal =  MedalManager.getMedalFromAnyManager(country, campaign, medalEvent.getMedal());
        if (medal != null)
        {
            JLabel lMedal = createMedalImage(medal);
            medalTextAndPilotPicPanel.add(lMedal, BorderLayout.SOUTH);
        }

        JPanel tMedal = formMedalTextBox();
        medalTextAndPilotPicPanel.add(tMedal, BorderLayout.CENTER);

        return medalTextAndPilotPicPanel;
    }

    private int getTopBottomBorder()
    {
        int topBottomBorder = 20;
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_MEDIUM || monitorSize == MonitorSize.FRAME_LARGE)
        {
            int area = Double.valueOf(PWCGMonitorSupport.getPWCGFrameSize().getHeight()).intValue() / 3;
            
            topBottomBorder = area / 6;
        }
        return topBottomBorder;
    }

    private int getLeftRightBorder()
    {
        int leftRightBorder = 40;
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        if (monitorSize == MonitorSize.FRAME_MEDIUM || monitorSize == MonitorSize.FRAME_LARGE)
        {
            leftRightBorder = 40 + ((Double.valueOf(PWCGMonitorSupport.getPWCGFrameSize().getWidth()).intValue() - 1200) / 4);
        }
        return leftRightBorder;
    }

    private JPanel formMedalTextBox() throws PWCGException
    {
        String headerText = getHeaderText();
        String bodyText = getBodyText();

        JPanel medalTextPanel = new JPanel(new BorderLayout());
        medalTextPanel.setOpaque(false);
            
        JComponent textHeaderPanel = formHeaderTextBox(headerText);
        medalTextPanel.add(textHeaderPanel, BorderLayout.NORTH);
        
        JComponent textPanel = formBodyTextBox(bodyText);
        medalTextPanel.add(textPanel, BorderLayout.CENTER);

        return medalTextPanel;
    }
    
    private JComponent formHeaderTextBox(String headerText) throws PWCGException
    {
        JTextArea tHeader = new JTextArea();
        tHeader.setOpaque(false);
        tHeader.setEditable(false);
        tHeader.setText(headerText);
        tHeader.setFont(PWCGMonitorFonts.getDecorativeFont());
        tHeader.setMargin(PWCGMonitorBorders.calculateBorderMargins(10, 20, 10, 20));

        return tHeader;
    }


    private JComponent formBodyTextBox(String bodyText) throws PWCGException
    {
        JTextArea tText = new JTextArea();
        tText.setOpaque(false);
        tText.setEditable(false);
        tText.setLineWrap(true);
        tText.setWrapStyleWord(true);
        tText.setText(bodyText);
        tText.setFont(PWCGMonitorFonts.getTypewriterFont());
        tText.setMargin(PWCGMonitorBorders.calculateBorderMargins(5, 20, 5, 20));

        return tText;
    }


    private String getHeaderText()
    {
        String medalHeaderText = "Award for valor";

        return medalHeaderText;
    }

	private String getBodyText()
	{
        String medalText = "Squadron: " + medalEvent.getSquadronName() + "\n";
        medalText += "Date: " + DateUtils.getDateStringPretty(medalEvent.getDate()) + "\n";
        medalText += medalRecipient.getNameAndRank() + " has been awarded the " + medalEvent.getMedal() + ".\n";               

        return medalText;
	}

    private JLabel createMedalImage(Medal medal)
    {
        SquadronMember pilot = medalRecipient;
        
        String medalPath = ContextSpecificImages.imagesMedals();
        ICountry country = CountryFactory.makeCountryByCountry(pilot.getCountry());
        if (country.getSide() == Side.ALLIED)
        {
            medalPath += "Allied\\";
        }
        else
        {
            medalPath += "Axis\\";
        }

        medalPath += medal.getMedalImage();

        ImageIcon medalIcon = null;  
        try 
        {
            medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
        }
        catch (Exception ex) 
        {
            PWCGLogger.logException(ex);
        }
                            
        JLabel lMedal = new JLabel(medalIcon);
        lMedal.setSize(medalIcon.getIconWidth(), medalIcon.getIconHeight());
        lMedal.setOpaque(false);
        return lMedal;
    }
    

	
	public void actionPerformed(ActionEvent ae)
	{
	}
	
	public void makeVisible(boolean visible) 
	{
	}
}
