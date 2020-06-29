package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

public class ChalkboardSelector extends JPanel implements ActionListener
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
        JPanel selectorPanel = new JPanel(new GridLayout(0, 2));
        selectorPanel.setOpaque(false);

        JLabel space1 = new JLabel("");
        selectorPanel.add(space1);

        JLabel space2 = new JLabel("");
        selectorPanel.add(space2);

        JRadioButton pilotsButton = makeRadioButton("Pilots", "CampPilots", "Show squadron pilot chalk board");
        selectorPanel.add(pilotsButton);

        JRadioButton topAcesButton = makeRadioButton("Top Aces: All", "CampTopAces", "Show top aces chalk board");
        selectorPanel.add(topAcesButton);

        JRadioButton equipmentButton = makeRadioButton("Equipment", "Equipment", "Show equipment chalk board");
        selectorPanel.add(equipmentButton);

        JRadioButton topAcesForServiceButton = makeRadioButton("Top Aces: Service", "CampTopAcesService", "Show top aces chalk board for your service");
        selectorPanel.add(topAcesForServiceButton);

        JLabel space3 = new JLabel("");
        selectorPanel.add(space3);

        JRadioButton topAcesNoHistoricalButton = makeRadioButton("Top Aces: Exclude Historical", "CampTopAcesNoHistorical", "Show top aces chalk board with no historical aces");
        selectorPanel.add(topAcesNoHistoricalButton); 
        
        this.add(selectorPanel, BorderLayout.CENTER);
    }

    private JRadioButton makeRadioButton(String buttonText, String action, String toolTiptext) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setActionCommand(action);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
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
        JPanel squadronPanel = CampaignHomeRightPanelFactory.makeCampaignHomeSquadronRightPanel(campaignHome.getCampaign(), campaignHome, squadronMembers, referencePlayer.getSquadronId());

        campaignHome.createNewContext(equipmentChalkboardDisplay, squadronPanel);
    }    

    public void createPlayerSquadronContext() throws PWCGException 
    {
        List<SquadronMember> squadronMembers = makePilotList();
        JPanel chalkboardPanel =  CampaignHomeCenterPanelFactory.makeCampaignHomeCenterPanel(campaignHome, squadronMembers);
        
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        JPanel squadronPanel = CampaignHomeRightPanelFactory.makeCampaignHomeSquadronRightPanel(campaignHome.getCampaign(), campaignHome, squadronMembers, referencePlayer.getSquadronId());

        campaignHome.createNewContext(chalkboardPanel, squadronPanel);
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
        
        JPanel topAcesListPanel = CampaignHomeRightPanelFactory.makeCampaignHomeAcesRightPanel(campaignHome, acesToDisplay);

        campaignHome.createNewContext(topAceListChalkboard, topAcesListPanel);
    }
}
