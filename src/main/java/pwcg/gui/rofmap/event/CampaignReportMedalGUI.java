package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.ImageIconCache;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.utils.CampaignDocumentPage;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignReportMedalGUI extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private MedalEvent medalEvent = null;
	private SquadronMember medalRecipient;

	public CampaignReportMedalGUI(Campaign campaign, MedalEvent medalEvent) throws PWCGException
	{
		super(ContextSpecificImages.imagesMedals() + "medalAwardAllied.jpg");

        this.medalEvent = medalEvent;
		this.medalRecipient = campaign.getPersonnelManager().getAnyCampaignMember(medalEvent.getPilotSerialNumber());
		
        ICountry country = CountryFactory.makeCountryByCountry(medalRecipient.getCountry());
        if (country.getSide() == Side.AXIS)
        {
            imagePath = ContextSpecificImages.imagesMedals() + "medalAwardGerman.jpg";
            setImage(imagePath);
        }
		setLayout(new GridLayout(0,1));
	
		makeGUI();		
	}
	
	private void makeGUI() 
	{		
		try
		{
			JPanel medalAwardPanel = formMedaUI();
	        this.add(medalAwardPanel);

            this.add(PWCGButtonFactory.makeDummy());
            this.add(PWCGButtonFactory.makeDummy());
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
     
        int leftRightBorder = 40;
        if (PWCGMonitorSupport.getPWCGFrameSize().getWidth() > 1200)
        {
            leftRightBorder = 40 + ((Double.valueOf(PWCGMonitorSupport.getPWCGFrameSize().getWidth()).intValue() - 1200) / 4);
        }

        
        int topBottomBorder = 20;
        if (PWCGMonitorSupport.getPWCGFrameSize().getHeight() > 1200)
        {
            int area = Double.valueOf(PWCGMonitorSupport.getPWCGFrameSize().getHeight()).intValue() / 3;
            
            topBottomBorder = area / 6;
        }

        medalTextAndPilotPicPanel.setBorder(BorderFactory.createEmptyBorder(topBottomBorder, leftRightBorder, topBottomBorder,leftRightBorder)); 

        Campaign campaign = PWCGContext.getInstance().getCampaign();
	    ICountry country = CountryFactory.makeCountryByCountry(medalRecipient.getCountry());
        Medal medal =  MedalManager.getMedalFromAnyManager(country, campaign, medalEvent.getMedal());
        if (medal != null)
        {
            JLabel lMedal = createMedalImage(medal);
            medalTextAndPilotPicPanel.add(lMedal, BorderLayout.WEST);
        }

        JPanel tMedal = formMedalTextBox();
        medalTextAndPilotPicPanel.add(tMedal, BorderLayout.CENTER);

        JLabel pilotLabel = formPilotPicture();
        medalTextAndPilotPicPanel.add(pilotLabel, BorderLayout.EAST);

        return medalTextAndPilotPicPanel;
    }

    private JLabel formPilotPicture()
    {
        JLabel pilotLabel = new JLabel();
        
        SquadronMember pilot = medalRecipient;
        if (pilot != null)
        {
            ImageIcon imageIcon = pilot.determinePilotPicture();  
            if (imageIcon != null)
            {
                pilotLabel = new JLabel(imageIcon);
                pilotLabel.setOpaque(false);
                pilotLabel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
            }
            else
            {
                PWCGLogger.log(LogLevel.ERROR, "Failed to get picture for " + pilot.getSerialNumber() + " at " + pilot.getPicName());
            }
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "No pilot for medal event"); 
        }

        return pilotLabel;
    }

    private JPanel formMedalTextBox() throws PWCGException
    {
        String headerText = getHeaderText();
        String bodyText = getBodyText();
            
        CampaignDocumentPage campaignDocumentPage = new CampaignDocumentPage();
        campaignDocumentPage.formDocument(headerText, bodyText);

        return campaignDocumentPage;
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
