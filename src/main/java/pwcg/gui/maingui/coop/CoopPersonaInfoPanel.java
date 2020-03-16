package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.coop.CoopPersonaDataBuilder;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ScrollBarWrapper;

public class CoopPersonaInfoPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	private List<CoopDisplayRecord> coopDisplayRecords = new ArrayList<>();

	public CoopPersonaInfoPanel()
	{
	    super(ContextSpecificImages.imagesMisc() + "Paper.jpg");
	}
	
	public void makePanels() 
	{
		try
		{
	        JPanel centerPanel = makeDisplay();
            this.add(centerPanel, BorderLayout.NORTH);
		}
		catch (Throwable e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel makeDisplay() throws PWCGException
    {
        loadCoopRecords();

        JPanel recordListPanel = new JPanel(new GridLayout(0, 4, 10, 5));
        recordListPanel.setOpaque(false);

        JPanel recordListHolderPanel = new JPanel();
        recordListHolderPanel.setOpaque(false);
        recordListHolderPanel.add(recordListPanel, BorderLayout.NORTH);

        JScrollPane planeListScroll = ScrollBarWrapper.makeScrollPane(recordListHolderPanel);

        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecords)
        {
            JLabel usernameLabel = makeVersionPanel(coopDisplayRecord.getUsername());
            JLabel campaignNameLabel = makeVersionPanel(coopDisplayRecord.getCampaignName());
            JLabel pilotNameLabel = makeVersionPanel(coopDisplayRecord.getPilotNameAndRank());
            JLabel squadronNameLabel = makeVersionPanel(coopDisplayRecord.getSquadronName());
            recordListPanel.add(usernameLabel);
            recordListPanel.add(campaignNameLabel);
            recordListPanel.add(pilotNameLabel);
            recordListPanel.add(squadronNameLabel);
        }

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(planeListScroll, BorderLayout.CENTER);
        return centerPanel;
    }

    private void loadCoopRecords() throws PWCGUserException, PWCGException
    {
        coopDisplayRecords.clear();
        List<String> campaigns = Campaign.getCampaignNames();
        for (String campaignName : campaigns)
        {
            Campaign campaign = new Campaign();
            if (campaign.open(campaignName))              
            {
                CoopPersonaDataBuilder coopPersonaDataBuilder = new CoopPersonaDataBuilder();
                PWCGContext.getInstance().setCampaign(campaign);
                if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
                {
                    List<CoopDisplayRecord> coopDisplayRecordsForUser = coopPersonaDataBuilder.getPlayerSquadronMembersForUser(campaign);
                    coopDisplayRecords.addAll(coopDisplayRecordsForUser);
                }
            }
        }
    }
    
    public JLabel makeVersionPanel(String labelText) throws PWCGException  
    {

        Font font = MonitorSupport.getPrimaryFontLarge();

        Color bg = ColorMap.PAPER_BACKGROUND;
        Color fg = ColorMap.PAPER_FOREGROUND;

        JLabel label = new JLabel(labelText, JLabel.LEFT);
        label.setBackground(bg);
        label.setForeground(fg);
        label.setOpaque(false);
        label.setFont(font);

       return label;
    }

}
