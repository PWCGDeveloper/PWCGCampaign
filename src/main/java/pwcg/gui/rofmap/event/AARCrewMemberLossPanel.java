package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARCrewMemberLossPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private Campaign campaign;

    public AARCrewMemberLossPanel(Campaign campaign)
	{
        super();

        this.aarCoordinator = AARCoordinator.getInstance();
        this.campaign = campaign;
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            JTabbedPane eventTabPane = createCrewMembersLostTab();
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

    private JTabbedPane createCrewMembersLostTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, CampaignReportCrewMemberStatusGUI> crewMemberLostGuiList = createCrewMemberLostSubTabs() ;
        for (String tabName : crewMemberLostGuiList.keySet())
        {
            eventTabPane.addTab(tabName, crewMemberLostGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportCrewMemberStatusGUI> createCrewMemberLostSubTabs() throws PWCGException 
	{
	    CrewMember referencePlayer = campaign.findReferencePlayer();
        AARCombatReportPanelData combatReportData = aarCoordinator.getAarContext()
                        .findUiCombatReportDataForSquadron(referencePlayer.getCompanyId()).getCombatReportPanelData();

        HashMap<String, CampaignReportCrewMemberStatusGUI> crewMemberLostGuiList = new HashMap<>();
        for (CrewMemberStatusEvent crewMemberStatusEvent : combatReportData.getCrewMembersLostInMission().values())
        {
            if (crewMemberStatusEvent.getSquadronId() == referencePlayer.getCompanyId())
            {
                CampaignReportCrewMemberStatusGUI crewMemberLostGui = new CampaignReportCrewMemberStatusGUI(crewMemberStatusEvent);
                String tabName = "CrewMember Lost: " + crewMemberStatusEvent.getCrewMemberName();
                crewMemberLostGuiList.put(tabName, crewMemberLostGui);
            }
        }
        
        Map<Integer, CrewMemberStatusEvent> crewMembersLostOutOfMission = aarCoordinator.getAarContext().getUiDebriefData().getCrewMemberLossPanelData().getSquadMembersLost();
        for (CrewMemberStatusEvent crewMemberStatusEvent : crewMembersLostOutOfMission.values())
        {
            if (crewMemberStatusEvent.getSquadronId() == referencePlayer.getCompanyId())
            {
                CampaignReportCrewMemberStatusGUI crewMemberLostGui = new CampaignReportCrewMemberStatusGUI(crewMemberStatusEvent);
                String tabName = "CrewMember Lost: " + crewMemberStatusEvent.getCrewMemberName();
                crewMemberLostGuiList.put(tabName, crewMemberLostGui);
            }
        }
        
        return crewMemberLostGuiList;
	}
}
