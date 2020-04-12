package pwcg.gui.campaign.home;

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
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class ReferencePilotSelector extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JComboBox<String> squadronMemberSelector;
    private CampaignHomeGUI campaignHomeGui;
    private Campaign campaign;
    private Map<String, SquadronMember> coopSquadronMembersInCampaign = new HashMap<>();

    public ReferencePilotSelector(Campaign campaign,CampaignHomeGUI campaignHomeGui)
    {
        super();
        this.campaign = campaign;
        this.campaignHomeGui = campaignHomeGui;
    }
    
    public void makePanels() 
    {
        try
        {
            setCenterPanel(makeCoopPersonaSelectorPanel());
            setLeftPanel(makeNavigatePanel());
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeCoopPersonaSelectorPanel() throws PWCGException
    {
        ImageResizingPanel centerPanel = null;
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        centerPanel = new ImageResizingPanel(imagePath);
        centerPanel.setLayout(new BorderLayout());

        Font font = MonitorSupport.getPrimaryFontLarge();
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
        
        centerPanel.add(squadronMemberSelector, BorderLayout.NORTH);
        
        return centerPanel;
    }

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigLeft.jpg");

        JPanel navPanel = new ImageResizingPanel(imagePath);
        navPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Accept Reference Pilot", "AcceptReferencePilot", this);
        buttonPanel.add(acceptButton);

        JButton cancelButton = PWCGButtonFactory.makeMenuButton("Cancel", "CancelReferencePilot", this);
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
                
                campaignHomeGui.clean();
                campaignHomeGui.createPilotContext();

                campaignHomeGui.enableButtonsAsNeeded();
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


