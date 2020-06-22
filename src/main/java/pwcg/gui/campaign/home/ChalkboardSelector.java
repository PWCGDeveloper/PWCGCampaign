package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;

public class ChalkboardSelector extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private CampaignHome campaignHome;
    
    public ChalkboardSelector(CampaignHome campaignHome)
    {
        this.campaignHome = campaignHome;
        this.campaign = campaignHome.getCampaign();
        
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    public void createSelectorPanel() throws PWCGException
    {
        JPanel selectorPanel = new JPanel(new GridLayout(0, 1));
        selectorPanel.setOpaque(false);

        JLabel space1 = new JLabel("");
        selectorPanel.add(space1);

        JButton pilotsButton = makeMenuButton("Pilots", "CampPilots", "Show squadron pilot chalk board");
        selectorPanel.add(pilotsButton);

        JButton equipmentButton = makeMenuButton("Equipment", "Equipment", "Show equipment chalk board");
        selectorPanel.add(equipmentButton);

        JButton topAcesButton = makeMenuButton("Top Aces: All", "CampTopAces", "Show top aces chalk board");
        selectorPanel.add(topAcesButton);

        JButton topAcesForServiceButton = makeMenuButton("Top Aces: Service", "CampTopAcesService", "Show top aces chalk board for your service");
        selectorPanel.add(topAcesForServiceButton);

        JButton topAcesNoHistoricalButton = makeMenuButton("Top Aces: Exclude Historical", "CampTopAcesNoHistorical", "Show top aces chalk board with no historical aces");
        selectorPanel.add(topAcesNoHistoricalButton); 
        
        this.add(selectorPanel, BorderLayout.CENTER);
    }

    private JButton makeMenuButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, null);
        ToolTipManager.setToolTip(button, toolTiptext);
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
            else if (action.equalsIgnoreCase("CampTopAces"))
            {
                createTopAceContext();
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
        CampaignEquipmentChalkBoard equipmentDisplay = new CampaignEquipmentChalkBoard();
        equipmentDisplay.makeEquipmentPanel(campaignHome.getCampaign());
    }    

    public void createPlayerSquadronContext() throws PWCGException 
    {
        List<SquadronMember> squadronMembers = makePilotList();
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        JPanel squadronPanel = CampaignHomeRightPanelFactory.makeSquadronPanel(campaignHome, squadronMembers, referencePlayer.getSquadronId());
    }

    private List<SquadronMember> makePilotList() throws PWCGException 
    {
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(referencePlayer.getSquadronId());
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        return squadronMembers.sortPilots(campaign.getDate());
    }

    private void createTopAceContext() throws PWCGException 
    {
        CampaignRosterBasePanelFactory topAceListDisplay = new CampaignRosterTopAcesPanelFactory(campaignHome);
        topAceListDisplay.makePilotList();
        createSquadronMemberContext(topAceListDisplay);
    }

    private void createSquadronMemberContext(CampaignRosterBasePanelFactory squadronMemberListDisplay) throws PWCGException 
    {
        squadronMemberListDisplay.makeCampaignHomePanels();
        this.add(BorderLayout.CENTER, squadronMemberListDisplay.getChalkboardPanel());
        this.add(BorderLayout.EAST, squadronMemberListDisplay.getPilotListPanel());

        CampaignGuiContextManager.getInstance().clearContextStack();
        CampaignGuiContextManager.getInstance().pushToContextStack(campaignHome);
    }

}
