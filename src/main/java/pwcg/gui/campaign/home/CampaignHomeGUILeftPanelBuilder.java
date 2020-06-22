package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;

public class CampaignHomeGUILeftPanelBuilder
{
    private CampaignHome parent;
    private Campaign campaign;
    
    public CampaignHomeGUILeftPanelBuilder(Campaign campaign, CampaignHome parent)
    {
        this.campaign = campaign;
        this.parent = parent;
    }

    public JPanel makeLeftPanel() throws PWCGException
    {
        JPanel campaignButtonPanel = new JPanel(new BorderLayout());
        campaignButtonPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);

        campaignButtonPanel.add(buttonPanel, BorderLayout.NORTH);

        return campaignButtonPanel;
    }

    private void makePlainButtons(JPanel buttonPanel) throws PWCGException
    {
        JLabel spacer = new JLabel("");
        buttonPanel.add(spacer);

        if (isDisplayMissionButton())
        {
            JButton createButton = makeMenuButton("Mission", "CampMission", "Generate a mission");
            addButton(buttonPanel, createButton);

            if (!campaign.isCoop())
            {
                JButton loneWolfMission = makeMenuButton("Lone Wolf Mission", "CampMissionLoneWolf", "Generate a lone wolf mission");
                addButton(buttonPanel, loneWolfMission);
            }
        }

        if (isDisplayAARButton())
        {
            JButton combatReportButton = makeMenuButton("Combat Report", "CampFlowCombatReport", "File an After Action Report (AAR) for a mission");
            addButton(buttonPanel, combatReportButton);
        }

        if (campaign.isCoop())
        {
            JButton addHumanPilotButton = makeMenuButton("Administer Coop Pilots", "AdminCoopPilots", "Administer coop pilots for this campaign");
            addButton(buttonPanel, addHumanPilotButton);
        }
        else
        {
            JButton addHumanPilotButton = makeMenuButton("Add Pilot", "AddHumanPilot", "Add a human pilot");
            addButton(buttonPanel, addHumanPilotButton);
        }

        JButton changeReferencePilot = makeMenuButton("Change Reference Pilot", "CampChangeReferencePilot", "Change the reference pilot for the UI");
        addButton(buttonPanel, changeReferencePilot);

        JLabel space2 = new JLabel("");
        buttonPanel.add(space2);

        if (campaign.isCampaignActive())
        {
            JButton leaveButton = makeMenuButton("Leave", "CampFlowLeave", "Request leave");
            addButton(buttonPanel, leaveButton);
        }

        if (isDisplayTransferButton())
        {
            JButton transferButton = makeMenuButton("Transfer", "CampFlowTransfer", "Transfer to a new squadron");
            addButton(buttonPanel, transferButton);
        }

        JButton recordsButton = makeMenuButton("Journal", "CampFlowJournal", "Update your personal journal");
        addButton(buttonPanel, recordsButton);

        JButton squadronLogButton = makeMenuButton("Squadron Log", "CampFlowLog", "View campaign logs");
        addButton(buttonPanel, squadronLogButton);

        if (campaign.isCampaignActive())
        {
            JButton skinManagementButton = makeMenuButton("Skin Management", "CampSkinManager", "Manage skins for the squadron");
            addButton(buttonPanel, skinManagementButton);

            JButton intellMapButton = makeMenuButton("Intel Map", "CampIntelMap", "View intelligence maps");
            addButton(buttonPanel, intellMapButton);

            JButton intelligenceButton = makeMenuButton("Intelligence Report", "CampFlowIntelligence", "View intelligence reports");
            addButton(buttonPanel, intelligenceButton);

            JButton equipmentDepotButton = makeMenuButton("Equipment Depot Report", "EquipmentDepotReport", "View equipment depot report");
            addButton(buttonPanel, equipmentDepotButton);

            JLabel space3 = new JLabel("");
            buttonPanel.add(space3);

            JButton simpleConfigButton = makeMenuButton("Simple Config", "CampSimpleConfig", "Set simple configuration for this campaign");
            addButton(buttonPanel, simpleConfigButton);

            JButton advancedConfigButton = makeMenuButton("Advanced Config", "CampAdvancedConfig", "Set advanced configuration for this campaign");
            addButton(buttonPanel, advancedConfigButton);
        }

        JLabel space4 = new JLabel("");
        buttonPanel.add(space4);

        JButton mainButton = makeMenuButton("Leave Campaign", "CampMainMenu", "Return to PWCG main menu");
        addButton(buttonPanel, mainButton);

        JLabel space5 = new JLabel("");
        buttonPanel.add(space5);

        JButton errorButton = makeMenuButton("Report Error", "CampError", "Bundle up data files for error reporting");
        addButton(buttonPanel, errorButton);
    }

    private boolean isDisplayMissionButton() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return false;
        }

        if (!campaign.isCampaignCanFly())
        {
            return false;
        }

        if (campaign.findReferencePlayer() == null)
        {
            return false;
        }

        if (campaign.findReferencePlayer().getPilotActiveStatus() != SquadronMemberStatus.STATUS_ACTIVE)
        {
            return false;
        }

        return true;
    }

    private boolean isDisplayAARButton() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return false;
        }

        return true;
    }

    private boolean isDisplayTransferButton() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return false;
        }

        if (!campaign.isCampaignCanFly())
        {
            return false;
        }

        if (campaign.isCoop())
        {
            return false;
        }

        return true;
    }

    private void addButton(JPanel buttonPanel, JButton button)
    {
        buttonPanel.add(button);
    }

    private JButton makeMenuButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, parent);

        ToolTipManager.setToolTip(button, toolTiptext);

        return button;
    }
}
