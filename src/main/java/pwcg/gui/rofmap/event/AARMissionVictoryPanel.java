package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARMissionVictoryPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private Campaign campaign;

    public AARMissionVictoryPanel(Campaign campaign)
	{
        super();

        this.aarCoordinator = AARCoordinator.getInstance();
        this.campaign = campaign;
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            JTabbedPane eventTabPane = createCrewMemberVictoriesTab();
            createPostCombatReportTabs(eventTabPane);
            this.add(eventTabPane, BorderLayout.WEST);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
    
    private void createPostCombatReportTabs(JTabbedPane eventTabPane)
    {
        JPanel postCombatPanel = new JPanel(new BorderLayout());
        postCombatPanel.setOpaque(false);

        postCombatPanel.add(eventTabPane, BorderLayout.CENTER);
        this.add(postCombatPanel, BorderLayout.CENTER);
    }

    private JTabbedPane createCrewMemberVictoriesTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, CampaignReportVictoryGUI> equipmentGuiList = createCrewMemberLostSubTabs() ;
        for (String tabName : equipmentGuiList.keySet())
        {
            eventTabPane.addTab(tabName, equipmentGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportVictoryGUI> createCrewMemberLostSubTabs() throws PWCGException 
	{
	    HashMap<String, List<VictoryEvent>> victoriesByCrewMembersInMySquadron = collateVictoriesByCrewMemberInMySquadron();
        HashMap<String, CampaignReportVictoryGUI> crewMemberVictoryPanelData = createVictoryTabs(victoriesByCrewMembersInMySquadron);
        return crewMemberVictoryPanelData;
	}

    private HashMap<String, List<VictoryEvent>> collateVictoriesByCrewMemberInMySquadron() throws PWCGException
    {
        HashMap<String, List<VictoryEvent>> victoriesByCrewMembersInMySquadron = new HashMap<>();
        List<VictoryEvent>  outOfMissionVictories = aarCoordinator.getAarContext().getUiDebriefData().getOutOfMissionVictoryPanelData().getOutOfMissionVictoryEvents();
        for (VictoryEvent victoryEvent : outOfMissionVictories)
		{
            if (victoryEvent.getSquadronId() == campaign.findReferencePlayer().getCompanyId())
            {
                if (!victoriesByCrewMembersInMySquadron.containsKey(victoryEvent.getCrewMemberName()))
                {
                    List<VictoryEvent> victoriesForCrewMember = new ArrayList<>();
                    victoriesByCrewMembersInMySquadron.put(victoryEvent.getCrewMemberName(), victoriesForCrewMember);
                }
                List<VictoryEvent> victoriesForCrewMember = victoriesByCrewMembersInMySquadron.get(victoryEvent.getCrewMemberName());
                victoriesForCrewMember.add(victoryEvent);
            }
		}
        return victoriesByCrewMembersInMySquadron;
    }

    private HashMap<String, CampaignReportVictoryGUI> createVictoryTabs(HashMap<String, List<VictoryEvent>> victoriesByCrewMembersInMySquadron) throws PWCGException
    {
        HashMap<String, CampaignReportVictoryGUI> crewMemberVictoryPanelData = new HashMap<>();
        for (String crewMemberName : victoriesByCrewMembersInMySquadron.keySet())
        {
            List<VictoryEvent> victoriesForCrewMember = victoriesByCrewMembersInMySquadron.get(crewMemberName);
            CampaignReportVictoryGUI victoryGUI = new CampaignReportVictoryGUI(campaign, victoriesForCrewMember);
            String tabName = "Notification of Victory: " + crewMemberName;
            crewMemberVictoryPanelData.put(tabName, victoryGUI);
        }
        return crewMemberVictoryPanelData;
    }
}
