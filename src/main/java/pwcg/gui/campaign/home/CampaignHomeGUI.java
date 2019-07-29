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
import pwcg.aar.ui.events.model.AARPilotEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.GreatAce;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.CampaignRosterBasePanelFactory;
import pwcg.gui.campaign.CampaignRosterSquadronPanelFactory;
import pwcg.gui.campaign.CampaignRosterTopAcesPanelFactory;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.CampaignMainGUI;
import pwcg.gui.rofmap.event.AARMainPanel;
import pwcg.gui.rofmap.event.AARMainPanel.EventPanelReason;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ReferencePlayerFinder;
import pwcg.gui.utils.ToolTipManager;

public class CampaignHomeGUI extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private CampaignMainGUI parent = null;
    private Campaign campaign = null;
    private JButton changeReferencePilot = null;
    private JButton loneWolfMission = null;
    private List<JButton> activeButtons = new ArrayList<JButton>();
    private boolean needContextRefresh = false;

    public CampaignHomeGUI(CampaignMainGUI parent, Campaign campaign) 
    {
        super();
        this.parent = parent;
        this.campaign = campaign;
        
        SquadronMember referencePlayer = ReferencePlayerFinder.findReferencePlayer(campaign);
        PWCGContextManager.getInstance().setReferencePlayer(referencePlayer);
        
        this.makeGUI();
    }

    public void makeGUI() 
    {
        try
        {
            createPilotContext();
        }
        catch (PWCGException e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
    
    @Override
    public void refreshCenterPanel() throws PWCGException
    {
        if (needContextRefresh)
        {
            needContextRefresh = false;
            createPilotContext();
        }
    }

    private JPanel makeLeftPanel() throws PWCGException 
    {
        String imagePath = getSideImage("CampaignLeft.jpg");

        ImageResizingPanel campaignButtonPanel = new ImageResizingPanel(imagePath);
        campaignButtonPanel.setLayout(new BorderLayout());
        campaignButtonPanel.setOpaque(true);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        enableButtonsAsNeeded();

        return campaignButtonPanel;
    }

    private void makePlainButtons(JPanel buttonPanel) throws PWCGException
    {
        JLabel spacer = new JLabel("");
        buttonPanel.add(spacer);

        activeButtons.clear();
        
        if (campaign.isCoop())
        {
            changeReferencePilot = makeMenuButton("Change Reference Pilot", "CampChangeReferencePilot", "Change the reference pilot for the UI");
            addButton(buttonPanel, changeReferencePilot);
        }

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

        if (isAddHumanPilot())
        {
            JButton addHumanPilotButton = makeMenuButton("Add Pilot", "AddHumanPilot", "Add a human pilot");
            addButton(buttonPanel, addHumanPilotButton);
        }
        
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
    
            JButton equipmentDepoButton = makeMenuButton("Equipment Depo Report", "EquipmentDepoReport", "View equipment depo report");
            addButton(buttonPanel, equipmentDepoButton);
    
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

    private boolean isAddHumanPilot() throws PWCGException
    {
        if (!campaign.isCampaignActive())
        {
            return true;
        }

        if (campaign.isCoop())
        {
            return true;
        }

        return false;
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

    public void createPilotContext() throws PWCGException 
    {
		MusicManager.playCampaignTheme(determineCampaignSideForMusic());

        CampaignRosterBasePanelFactory pilotListDisplay = new CampaignRosterSquadronPanelFactory(this);
        pilotListDisplay.makePilotList();
        createSquadronMemberContext(pilotListDisplay);
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
            SquadronMember referencePlayer = campaign.getReferenceCampaignMember();
            return referencePlayer.determineCountry(campaign.getDate()).getSide();
        }
     }

    private void createTopAceContext() throws PWCGException 
    {
		CampaignRosterBasePanelFactory topAceListDisplay = new CampaignRosterTopAcesPanelFactory(this);
        topAceListDisplay.makePilotList();
        createSquadronMemberContext(topAceListDisplay);
    }

    private void createEquipmentContext() throws PWCGException 
    {
        setLeftPanel(makeLeftPanel());
        
        CampaignEquipmentChalkBoard equipmentDisplay = new CampaignEquipmentChalkBoard();
        equipmentDisplay.makeEquipmentPanel(campaign);
        setCenterPanel(equipmentDisplay);

        CampaignRosterBasePanelFactory pilotListDisplay = new CampaignRosterSquadronPanelFactory(this);
        pilotListDisplay.makePilotList();
        pilotListDisplay.makeCampaignHomePanels();
        setRightPanel(pilotListDisplay.getPilotListPanel());

        CampaignGuiContextManager.getInstance().clearContextStack();
        CampaignGuiContextManager.getInstance().pushToContextStack(this);
    }    

    private void createSquadronMemberContext(CampaignRosterBasePanelFactory squadronMemberListDisplay) throws PWCGException 
    {
        squadronMemberListDisplay.makeCampaignHomePanels();
        setLeftPanel(makeLeftPanel());
        setCenterPanel(squadronMemberListDisplay.getChalkboardPanel());
        setRightPanel(squadronMemberListDisplay.getPilotListPanel());

        CampaignGuiContextManager.getInstance().clearContextStack();
        CampaignGuiContextManager.getInstance().pushToContextStack(this);
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
                createPilotContext();
            }
            else if (action.equalsIgnoreCase("CampTopAces"))
            {
                createTopAceContext();
            }
            else if (action.equalsIgnoreCase("Equipment"))
            {
                createEquipmentContext();
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
            Logger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            campaign.setCurrentMission(null);
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
        catch (Throwable t)
        {
            campaign.setCurrentMission(null);
            Logger.logException(t);
            ErrorDialog.internalError(t.getMessage());
        }
    }

    public void campaignTimePassed(int timePassedDays, AARPilotEvent pilotEvent, EventPanelReason reason) throws PWCGException 
    {
        campaign.setCurrentMission(null);

        if (reason == EventPanelReason.EVENT_PANEL_REASON_LEAVE)
        {
            AARCoordinator.getInstance().submitLeave(campaign, timePassedDays);            
        }
        else
        {
            AARCoordinator.getInstance().submitTransfer(campaign, timePassedDays);
            
        }

        AARMainPanel eventDisplay = new AARMainPanel(campaign, this, reason, pilotEvent);
        eventDisplay.makePanels();		
        
        CampaignGuiContextManager.getInstance().pushToContextStack(eventDisplay);
    }

    public void clean() throws PWCGException  
    {       
        PwcgGuiContext context = CampaignGuiContextManager.getInstance().getCurrentContext();
        if (context.getCenterPanel() != null)
        {
            remove(context.getCenterPanel());        
        }
        if (context.getRightPanel() != null)
        {
            remove(context.getRightPanel());       
        }
        if (context.getLeftPanel() != null)
        {
            remove(context.getLeftPanel());       
        }
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
