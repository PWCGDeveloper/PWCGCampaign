package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.CampaignMainGUI;
import pwcg.gui.rofmap.event.AARMainPanel;
import pwcg.gui.rofmap.event.AARMainPanel.EventPanelReason;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.utils.ImageResizingPanel;

public class CampaignHome extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private CampaignMainGUI parent;
    private PwcgThreePanelUI pwcgThreePanel;
    private Campaign campaign;
    private boolean needContextRefresh = false;
    private ChalkboardSelector chalkboardSelector;

    public CampaignHome(CampaignMainGUI parent, Campaign campaign) throws PWCGException
    {
        super("");
        this.setLayout(new BorderLayout());

        this.pwcgThreePanel = new PwcgThreePanelUI(this);
        this.parent = parent;
        this.campaign = campaign;
        this.makePanel();
    }

    public void makePanel()
    {
        try
        {
            String imagePath = UiImageResolver.getImageMain("BrickWall.jpg");
            this.setImage(imagePath);
            createCampaignHomeContext();
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public void createCampaignHomeContext() throws PWCGException
    {
        MusicManager.playCampaignTheme(determineCampaignSideForMusic());
        this.add(BorderLayout.WEST, makeLeftPanel());
        
        createSelectorPanel();
        
        pwcgThreePanel.setLeftPanel(makeLeftPanel());
        pwcgThreePanel.setCenterPanel(makeDefaultCenterPanel());
        pwcgThreePanel.setRightPanel(makeDefaultRightPanel());
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
        CampaignHomeGUILeftPanelBuilder leftPanelBuilder = new CampaignHomeGUILeftPanelBuilder(campaign, this);
        return leftPanelBuilder.makeLeftPanel();
    }

    private JPanel makeDefaultCenterPanel() throws PWCGException
    {
        List<SquadronMember> squadronMembers = makePilotList();
        return CampaignHomeCenterPanelFactory.makeCampaignHomeCenterPanel(this, squadronMembers);
    }

    private JPanel makeDefaultRightPanel() throws PWCGException
    {
        List<SquadronMember> squadronMembers = makePilotList();
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        return CampaignHomeRightPanelFactory.makeCampaignHomeSquadronRightPanel(campaign, this, squadronMembers, referencePlayer.getSquadronId());
    }

    private void createSelectorPanel() throws PWCGException
    {
        chalkboardSelector = new ChalkboardSelector(this);
        chalkboardSelector.createSelectorPanel();
    }

    private List<SquadronMember> makePilotList() throws PWCGException 
    {
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(referencePlayer.getSquadronId());
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        return squadronMembers.sortPilots(campaign.getDate());
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
            else
            {
                CampaignHomeAction homeGUIAction = new CampaignHomeAction(this, campaign);
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

    public Campaign getCampaign()
    {
        return campaign;
    }
    
    public void createNewContext(JPanel centerPanel, JPanel rightPanel) throws PWCGException
    {
        pwcgThreePanel.setCenterPanel(centerPanel);
        pwcgThreePanel.setRightPanel(rightPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
    }

    public ChalkboardSelector getChalkboardSelector()
    {
        return chalkboardSelector;
    }
}
