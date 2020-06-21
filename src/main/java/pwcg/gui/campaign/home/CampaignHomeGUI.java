package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadmember.GreatAce;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.CampaignMainGUI;
import pwcg.gui.rofmap.event.AARMainPanel;
import pwcg.gui.rofmap.event.AARMainPanel.EventPanelReason;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;

public class CampaignHomeGUI extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private CampaignMainGUI parent = null;
    private Campaign campaign = null;
    private JButton changeReferencePilot = null;
    private JButton loneWolfMission = null;
    private List<JButton> activeButtons = new ArrayList<JButton>();
    private boolean needContextRefresh = false;
    private ChalkboardSelector chalkboardSelector;

    public CampaignHomeGUI(CampaignMainGUI parent, Campaign campaign) throws PWCGException
    {
        super();
        this.parent = parent;
        this.campaign = campaign;
        this.chalkboardSelector = new ChalkboardSelector(this);
        this.makeGUI();
    }

    public void makeGUI()
    {
        try
        {
            createCampaignHomeContext();
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public void refreshScreen() throws PWCGException
    {
        if (needContextRefresh)
        {
            needContextRefresh = false;
            createCampaignHomeContext();
        }
    }

    private JPanel makeLeftPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getSideImage(campaign, "BrickLeft.jpg");

        ImageResizingPanel campaignButtonPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        campaignButtonPanel.setLayout(new BorderLayout());
        campaignButtonPanel.setOpaque(true);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);

        campaignButtonPanel.add(buttonPanel, BorderLayout.NORTH);
        enableButtonsAsNeeded();

        return campaignButtonPanel;
    }

    private void makePlainButtons(JPanel buttonPanel) throws PWCGException
    {
        JLabel spacer = new JLabel("");
        buttonPanel.add(spacer);

        activeButtons.clear();

        if (isDisplayMissionButton())
        {
            JButton createButton = makeMenuButton("Mission", "CampMission", "Generate a mission");
            addButton(buttonPanel, createButton);

            if (!campaign.isCoop())
            {
                loneWolfMission = makeMenuButton("Lone Wolf Mission", "CampMissionLoneWolf", "Generate a lone wolf mission");
                addButton(buttonPanel, loneWolfMission);
            }
        }

        if (isDisplayAARButton())
        {
            JButton combatReportButton = makeMenuButton("Combat Report", "CampFlowCombatReport", "File an After Action Report (AAR) for a mission");
            addButton(buttonPanel, combatReportButton);
        }

        JLabel space1 = new JLabel("");
        buttonPanel.add(space1);

        JButton pilotsButton = makeMenuButton("Pilots", "CampPilots", "Show squadron pilot chalk board");
        addButton(buttonPanel, pilotsButton);

        JButton topAcesButton = makeMenuButton("Top Aces", "CampTopAces", "Show top aces chalk board");
        addButton(buttonPanel, topAcesButton);

        JButton equipmentButton = makeMenuButton("Equipment", "Equipment", "Show equipment chalk board");
        addButton(buttonPanel, equipmentButton);

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

        changeReferencePilot = makeMenuButton("Change Reference Pilot", "CampChangeReferencePilot", "Change the reference pilot for the UI");
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
        activeButtons.add(button);
    }

    private JButton makeMenuButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, this);

        ToolTipManager.setToolTip(button, toolTiptext);

        return button;
    }

    public void createCampaignHomeContext() throws PWCGException
    {
        MusicManager.playCampaignTheme(determineCampaignSideForMusic());
        this.add(BorderLayout.WEST, makeLeftPanel());
        chalkboardSelector.createPlayerSquadronContext();

    }

    private Side determineCampaignSideForMusic() throws PWCGException
    {
        if (campaign.isCoop())
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 50)
            {
                return Side.ALLIED;
            }
            else
            {
                return Side.AXIS;
            }
        }
        else
        {
            SquadronMember referencePlayer = campaign.findReferencePlayer();
            return referencePlayer.determineCountry(campaign.getDate()).getSide();
        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();

            if (action.equalsIgnoreCase("CampMainMenu"))
            {
                campaign.write();
                parent.refresh();

                return;
            }
            else if (action.equalsIgnoreCase("CampPilots"))
            {
                chalkboardSelector.actionPerformed(ae);
            }
            else if (action.equalsIgnoreCase("CampTopAces"))
            {
                chalkboardSelector.actionPerformed(ae);
            }
            else if (action.equalsIgnoreCase("Equipment"))
            {
                chalkboardSelector.actionPerformed(ae);
            }
            else
            {
                CampaignHomeGUIAction homeGUIAction = new CampaignHomeGUIAction(this, campaign);
                homeGUIAction.actionPerformed(ae);
            }
        }
        catch (PWCGUserException ue)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
        catch (Throwable t)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(t);
            ErrorDialog.internalError(t.getMessage());
        }
    }

    public void campaignTimePassedForLeave(int timePassedDays) throws PWCGException
    {
        campaign.setCurrentMission(null);
        AARCoordinator.getInstance().submitLeave(campaign, timePassedDays);
        AARMainPanel eventDisplay = new AARMainPanel(campaign, this, EventPanelReason.EVENT_PANEL_REASON_LEAVE);
        eventDisplay.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(eventDisplay);
    }

    public void campaignTimePassedForTransfer(int timePassedDays, TransferEvent pilotEvent) throws PWCGException
    {
        campaign.setCurrentMission(null);
        AARCoordinator.getInstance().submitTransfer(campaign, timePassedDays);
        AARMainPanel eventDisplay = new AARMainPanel(campaign, null, EventPanelReason.EVENT_PANEL_REASON_TRANSFER, pilotEvent);
        eventDisplay.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(eventDisplay);
    }

    public void enableButtonsAsNeeded() throws PWCGException
    {
        if (loneWolfMission != null)
        {
            if (campaign.isCampaignActive() && !campaign.isCoop())
            {
                if (GreatAce.isGreatAce(campaign))
                {
                    loneWolfMission.setEnabled(true);
                }
                else
                {
                    loneWolfMission.setEnabled(false);
                }
            }
        }
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

}
