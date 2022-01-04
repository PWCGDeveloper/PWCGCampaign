package pwcg.gui.campaign.home;

import java.awt.event.ActionEvent;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGErrorBundler;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.activity.CampaignActivityScreen;
import pwcg.gui.campaign.config.CampaignConfigurationScreen;
import pwcg.gui.campaign.crewmember.CampaignCrewMemberScreen;
import pwcg.gui.campaign.intel.CampaignIntelScreen;
import pwcg.gui.campaign.mission.CampaignMissionScreen;
import pwcg.gui.campaign.personnel.CampaignPersonnelScreen;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.UIUtils;

public class CampaignHomeAction
{
    private CampaignHomeScreen campaignHome = null;
    private Campaign campaign = null;

    public CampaignHomeAction(CampaignHomeScreen parent, Campaign campaign) 
    {
        super();
        this.campaignHome = parent;
        this.campaign = campaign;
    }
    

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("CampError"))
            {
                PWCGErrorBundler errorBundler = new PWCGErrorBundler();
                errorBundler.bundleDebuggingData();
                ErrorDialog.internalError("Error during AAR process - please post " + errorBundler.getTargetErrorFileName() + ".zip");
            }
            else if (action.equalsIgnoreCase("CampMission"))
            {
                showCampaignMissionActions();
            }
            else if (action.equalsIgnoreCase("CampPersonnel"))
            {
                showCampaignPersonnelActions();
            }
            else if (action.equalsIgnoreCase("CampActivity"))
            {
                showCampaignActivities();
            }
            else if (action.equals("CampIntel"))
            {
                showIntel();
            }
            else if (action.equals("CampConfig"))
            {
                showConfig();
            }
            else if (action.startsWith("CampFlowCrewMember"))
            {
                showCrewMember(action);
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

    private void showCrewMember(String action) throws PWCGException 
    {
        CrewMember crewMember = UIUtils.getCrewMemberFromAction(campaign, action);
        if (crewMember != null)
        {
            Company company = crewMember.determineSquadron();
            if (crewMember instanceof TankAce)
            {
                TankAce ace = (TankAce)crewMember;
                company =  ace.determineSquadron();;
            }
            CampaignCrewMemberScreen crewMemberPanel = new CampaignCrewMemberScreen(campaign, company, crewMember, campaignHome);
            crewMemberPanel.makePanels();
            
            CampaignGuiContextManager.getInstance().pushToContextStack(crewMemberPanel);
        }
    }

    private void showCampaignMissionActions() throws PWCGException
    {
        CampaignMissionScreen missionGUI = new CampaignMissionScreen(campaign, campaignHome);
        missionGUI.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(missionGUI);        
    }
    
    private void showCampaignPersonnelActions() throws PWCGException
    {
        CampaignPersonnelScreen personnelGUI = new CampaignPersonnelScreen(campaign, campaignHome);
        personnelGUI.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(personnelGUI);        
    }
    
    private void showCampaignActivities() throws PWCGException
    {
        CampaignActivityScreen activityGUI = new CampaignActivityScreen(campaign, campaignHome);
        activityGUI.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(activityGUI);        
    }

    private void showIntel() throws PWCGException
    {
        CampaignIntelScreen intelGUI = new CampaignIntelScreen(campaign);
        intelGUI.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(intelGUI);
    }

    private void showConfig() throws PWCGException
    {
        CampaignConfigurationScreen configGUI = new CampaignConfigurationScreen(campaign);
        configGUI.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(configGUI);
    }

}
