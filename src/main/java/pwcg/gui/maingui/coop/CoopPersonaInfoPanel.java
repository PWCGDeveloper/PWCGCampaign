package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

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
import pwcg.gui.utils.PWCGLabelFactory;
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
            this.setBorder(BorderFactory.createEmptyBorder(150, 40, 40, 150));

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
            JLabel usernameLabel = makeCoopInfoLabel(coopDisplayRecord.getUsername());
            JLabel campaignNameLabel = makeCoopInfoLabel(coopDisplayRecord.getCampaignName());
            JLabel crewMemberNameLabel = makeCoopInfoLabel(coopDisplayRecord.getCrewMemberNameAndRank());
            JLabel squadronNameLabel = makeCoopInfoLabel(coopDisplayRecord.getSquadronName());
            recordListPanel.add(usernameLabel);
            recordListPanel.add(campaignNameLabel);
            recordListPanel.add(crewMemberNameLabel);
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
                    List<CoopDisplayRecord> coopDisplayRecordsForUser = coopPersonaDataBuilder.getPlayerCrewMembersForUser(campaign);
                    coopDisplayRecords.addAll(coopDisplayRecordsForUser);
                }
            }
        }
    }

    public JLabel makeCoopInfoLabel(String labelText) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        JLabel label = PWCGLabelFactory.makeTransparentLabel(labelText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.RIGHT);
        return label;
    }

}
