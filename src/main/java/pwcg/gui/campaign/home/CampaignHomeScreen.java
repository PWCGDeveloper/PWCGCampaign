package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.IRefreshableParentUI;
import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.maingui.PwcgMainScreen;
import pwcg.gui.rofmap.event.AARReportMainPanel;
import pwcg.gui.rofmap.event.AARReportMainPanel.EventPanelReason;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.utils.ImageResizingPanel;

public class CampaignHomeScreen extends ImageResizingPanel implements ActionListener, IRefreshableParentUI
{
    private static final long serialVersionUID = 1L;

    private PwcgMainScreen parent;
    private PwcgThreePanelUI pwcgThreePanel;
    private Campaign campaign;
    private boolean needContextRefresh = false;
    private ChalkboardSelector chalkboardSelector;

    public CampaignHomeScreen(PwcgMainScreen parent, Campaign campaign) throws PWCGException
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.pwcgThreePanel = new PwcgThreePanelUI(this);
        this.parent = parent;
        this.campaign = campaign;
        this.makePanel();
    }

    public void makePanel()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignHomeScreen);
            this.setImageFromName(imagePath);
            
            refreshInformation();
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public void refreshInformation() throws PWCGException
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
            refreshInformation();
        }
    }

    private JPanel makeLeftPanel() throws PWCGException
    {
        CampaignHomeGUILeftPanelBuilder leftPanelBuilder = new CampaignHomeGUILeftPanelBuilder(this);
        return leftPanelBuilder.makeLeftPanel();
    }

    private JPanel makeDefaultCenterPanel() throws PWCGException
    {
        List<CrewMember> squadronMembers = makeCrewMemberList();
        return CampaignHomeCenterPanelFactory.makeCampaignHomeCenterPanel(this, squadronMembers);
    }

    private JPanel makeDefaultRightPanel() throws PWCGException
    {
        List<CrewMember> squadronMembers = makeCrewMemberList();
        CrewMember referencePlayer = campaign.findReferencePlayer();
        return CampaignHomeRightPanelFactory.makeCampaignHomeSquadronRightPanel(campaign, this, squadronMembers, referencePlayer.getCompanyId());
    }

    private void createSelectorPanel() throws PWCGException
    {
        chalkboardSelector = new ChalkboardSelector(this);
        chalkboardSelector.createSelectorPanel();
    }

    private List<CrewMember> makeCrewMemberList() throws PWCGException 
    {
        CrewMember referencePlayer = campaign.findReferencePlayer();
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(referencePlayer.getCompanyId());
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        return squadronMembers.sortCrewMembers(campaign.getDate());
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
            CrewMember referencePlayer = campaign.findReferencePlayer();
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
        AARReportMainPanel eventDisplay = new AARReportMainPanel(campaign, this, EventPanelReason.EVENT_PANEL_REASON_LEAVE);
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

    @Override
    public JPanel getScreen()
    {
        return this;
    }
}
