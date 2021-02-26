package pwcg.gui.campaign.personnel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignReferencePilotSelectorScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JComboBox<String> squadronMemberSelector;
    private CampaignHomeScreen campaignHomeGui;
    private Campaign campaign;
    private Map<String, SquadronMember> coopSquadronMembersInCampaign = new HashMap<>();

    public CampaignReferencePilotSelectorScreen(Campaign campaign,CampaignHomeScreen campaignHomeGui)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.campaignHomeGui = campaignHomeGui;
    }
    
    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignReferencePilotSelectorScreen);
            this.setImageFromName(imagePath);

            this.add(BorderLayout.WEST, makeNavigatePanel());
            this.add(BorderLayout.CENTER, makeCoopPersonaSelectorPanel());
            this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeCoopPersonaSelectorPanel() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        squadronMemberSelector = new JComboBox<String>();
        squadronMemberSelector.setOpaque(false);
        squadronMemberSelector.setBackground(ColorMap.PAPER_BACKGROUND);
        squadronMemberSelector.setFont(font);
        
        SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (SquadronMember player : players.getSquadronMemberList())
        {
            if (player.getPilotActiveStatus() >= SquadronMemberStatus.STATUS_CAPTURED)
            {
                String selectiontext = player.getNameAndRank();
                squadronMemberSelector.addItem(selectiontext);
                coopSquadronMembersInCampaign.put(selectiontext, player);
            }
        }

        if (squadronMemberSelector.getItemCount() > 0)
        {
            squadronMemberSelector.setSelectedIndex(0);
        }
        
        JPanel gridPanel = new JPanel(new GridLayout(0,3));
        gridPanel.setOpaque(false);
        
        gridPanel.add(PWCGButtonFactory.makeDummy());
        gridPanel.add(squadronMemberSelector);
        gridPanel.add(PWCGButtonFactory.makeDummy());

        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel centerPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(gridPanel, BorderLayout.NORTH);
        this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        return centerPanel;
    }

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept", "AcceptReferencePilot", "Make this pilot the reference pilot", this);
        buttonPanel.add(acceptButton);

        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "CancelReferencePilot", "Do not change the reference pilot", this);
        buttonPanel.add(cancelButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("AcceptReferencePilot"))
            {
                SquadronMember referencePlayer = coopSquadronMembersInCampaign.get(squadronMemberSelector.getSelectedItem());
                if (referencePlayer != null)
                {
                    campaign.getCampaignData().setReferencePlayerSerialNumber(referencePlayer.getSerialNumber());
                }
                
                campaignHomeGui.refreshInformation();
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("CancelReferencePilot"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}


