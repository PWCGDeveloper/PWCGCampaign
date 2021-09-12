package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javafx.scene.control.ButtonGroup;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.campaign.home.TopAcesListBuilder.TopAcesListType;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ToolTipManager;

public class ChalkboardSelector extends Pane implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private CampaignHomeScreen campaignHome;
    private ButtonGroup buttonGroup = new ButtonGroup();

    public ChalkboardSelector(CampaignHomeScreen campaignHome)
    {
        this.campaignHome = campaignHome;
        this.campaign = campaignHome.getCampaign();
        
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    public void createSelectorPanel() throws PWCGException
    {
        Pane selectorPanel = new Pane(new GridLayout(0, 3));
        selectorPanel.setOpaque(false);

        RadioButton  pilotsButton = makeRadioButton("Pilots", "CampPilots", "Show squadron pilot chalk board");
        selectorPanel.add(pilotsButton);

        RadioButton  playerPilotsButton = makeRadioButton("Player Pilots", "CampPlayerPilots", "Show player pilots chalk board");
        selectorPanel.add(playerPilotsButton);

        RadioButton  topAcesButton = makeRadioButton("Top Aces: All", "CampTopAces", "Show top aces chalk board");
        selectorPanel.add(topAcesButton);

        RadioButton  equipmentButton = makeRadioButton("Equipment", "Equipment", "Show equipment chalk board");
        selectorPanel.add(equipmentButton);

        selectorPanel.add(new Label(""));

        RadioButton  topAcesForServiceButton = makeRadioButton("Top Aces: Service", "CampTopAcesService", "Show top aces chalk board for your service");
        selectorPanel.add(topAcesForServiceButton);

        selectorPanel.add(new Label(""));
        selectorPanel.add(new Label(""));

        RadioButton  topAcesNoHistoricalButton = makeRadioButton("Top Aces: Exclude Historical", "CampTopAcesNoHistorical", "Show top aces chalk board with no historical aces");
        selectorPanel.add(topAcesNoHistoricalButton); 
        
        this.add(selectorPanel, BorderLayout.CENTER);
    }

    private RadioButton  makeRadioButton(String buttonText, String action, String toolTiptext) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        RadioButton  button = new RadioButton (buttonText);
        button.setActionCommand(action);
        button.setAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);
        
        ToolTipManager.setToolTip(button, toolTiptext);

        buttonGroup.add(button);

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();

            if (action.equalsIgnoreCase("CampPilots"))
            {
                createPlayerSquadronContext();
            }            
            else if (action.equalsIgnoreCase("CampPlayerPilots"))
            {
                createPlayerPilotsContext();
            }
            else if (action.equalsIgnoreCase("CampTopAcesService"))
            {
                createTopAceContext(TopAcesListType.TOP_ACES_SERVICE);
            }
            else if (action.equalsIgnoreCase("CampTopAcesNoHistorical"))
            {
                createTopAceContext(TopAcesListType.TOP_ACES_NO_HISTORICAL);
            }
            else if (action.equalsIgnoreCase("CampTopAces"))
            {
                createTopAceContext(TopAcesListType.TOP_ACES_ALL);
            }
            else if (action.equalsIgnoreCase("Equipment"))
            {
                createEquipmentContext();
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

    public void createEquipmentContext() throws PWCGException 
    {       
        CampaignEquipmentChalkBoardPanelSet equipmentChalkboardDisplay = new CampaignEquipmentChalkBoardPanelSet(campaignHome.getChalkboardSelector());
        equipmentChalkboardDisplay.makeEquipmentPanel(campaignHome.getCampaign());
        
        List<SquadronMember> squadronMembers = makePilotList();
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        Pane squadronPanel = CampaignHomeRightPanelFactory.makeCampaignHomeSquadronRightPanel(campaignHome.getCampaign(), campaignHome, squadronMembers, referencePlayer.getSquadronId());

        campaignHome.createNewContext(equipmentChalkboardDisplay, squadronPanel);
    }    

    public void createPlayerSquadronContext() throws PWCGException 
    {
        List<SquadronMember> squadronMembers = makePilotList();
        Pane chalkboardPanel =  CampaignHomeCenterPanelFactory.makeCampaignHomeCenterPanel(campaignHome, squadronMembers);
        
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        Pane squadronPanel = CampaignHomeRightPanelFactory.makeCampaignHomeSquadronRightPanel(campaignHome.getCampaign(), campaignHome, squadronMembers, referencePlayer.getSquadronId());

        campaignHome.createNewContext(chalkboardPanel, squadronPanel);
    }

    public void createPlayerPilotsContext() throws PWCGException 
    {
        List<SquadronMember> playerPilots = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList();
        Pane chalkboardPanel =  CampaignHomeCenterPanelFactory.makeCampaignHomeCenterPanel(campaignHome, playerPilots);
        
        Pane playerPilotPanel = CampaignHomeRightPanelFactory.makeCampaignHomeAcesRightPanel(campaignHome, playerPilots);

        campaignHome.createNewContext(chalkboardPanel, playerPilotPanel);
    }
    
    private List<SquadronMember> makePilotList() throws PWCGException 
    {
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(referencePlayer.getSquadronId());
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        return squadronMembers.sortPilots(campaign.getDate());
    }

    private void createTopAceContext(TopAcesListType topAcesListType) throws PWCGException 
    {
        TopAcesListBuilder topAcesListBuilder = new TopAcesListBuilder(campaign);
        List<SquadronMember> acesToDisplay = topAcesListBuilder.getTopTenAces(topAcesListType);
        
        CampaignHomeTopAcesCenterPanel topAceListChalkboard = new CampaignHomeTopAcesCenterPanel(campaignHome);
        topAceListChalkboard.makePanel(acesToDisplay);
        
        Pane topAcesListPanel = CampaignHomeRightPanelFactory.makeCampaignHomeAcesRightPanel(campaignHome, acesToDisplay);

        campaignHome.createNewContext(topAceListChalkboard, topAcesListPanel);
    }
}
