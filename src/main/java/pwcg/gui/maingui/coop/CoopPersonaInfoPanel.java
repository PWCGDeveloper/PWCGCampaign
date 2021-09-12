package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.coop.CoopPersonaDataBuilder;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ScrollBarWrapper;

public class CoopPersonaInfoPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	private List<CoopDisplayRecord> coopDisplayRecords = new ArrayList<>();

	public CoopPersonaInfoPanel()
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	}
	
	public void makePanels() 
	{
		try
		{
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
            this.setImageFromName(imagePath);
            this.setBorder(BorderFactory.createEmptyBorder(150,40,40,150));

            Pane centerPanel = makeDisplay();
            this.add(centerPanel, BorderLayout.NORTH);
		}
		catch (Throwable e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private Pane makeDisplay() throws PWCGException
    {
        loadCoopRecords();

        Pane recordListPanel = new Pane(new GridLayout(0, 4, 10, 5));
        recordListPanel.setOpaque(false);

        Pane recordListHolderPanel = new Pane();
        recordListHolderPanel.setOpaque(false);
        recordListHolderPanel.add(recordListPanel, BorderLayout.NORTH);

        JScrollPane planeListScroll = ScrollBarWrapper.makeScrollPane(recordListHolderPanel);

        for (CoopDisplayRecord coopDisplayRecord : coopDisplayRecords)
        {
            Label usernameLabel = makeVersionPanel(coopDisplayRecord.getUsername());
            Label campaignNameLabel = makeVersionPanel(coopDisplayRecord.getCampaignName());
            Label pilotNameLabel = makeVersionPanel(coopDisplayRecord.getPilotNameAndRank());
            Label squadronNameLabel = makeVersionPanel(coopDisplayRecord.getSquadronName());
            recordListPanel.add(usernameLabel);
            recordListPanel.add(campaignNameLabel);
            recordListPanel.add(pilotNameLabel);
            recordListPanel.add(squadronNameLabel);
        }

        Pane centerPanel = new Pane();
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
    
    public Label makeVersionPanel(String labelText) throws PWCGException  
    {

        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        Color bg = ColorMap.PAPER_BACKGROUND;
        Color fg = ColorMap.PAPER_FOREGROUND;

        Label label = new Label(labelText, Label.LEFT);
        label.setBackground(bg);
        label.setForeground(fg);
        label.setOpaque(false);
        label.setFont(font);

       return label;
    }

}
