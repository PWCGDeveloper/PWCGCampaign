package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARMedalPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private AARCoordinator aarCoordinator;

    public AARMedalPanel(Campaign campaign)
	{
        super();
        this.campaign = campaign;
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            JTabbedPane eventTabPane = createTab();
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


    private JTabbedPane createTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, CampaignReportMedalGUI> crewMemberMedalGuiList = createCrewMemberMedalList() ;
        for (String tabName : crewMemberMedalGuiList.keySet())
        {
            eventTabPane.addTab(tabName, crewMemberMedalGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportMedalGUI> createCrewMemberMedalList() throws PWCGException 
	{
        List<MedalEvent> medalsAwarded = aarCoordinator.getAarContext().getUiDebriefData().getMedalPanelData().getMedalsAwarded();
        HashMap<String, CampaignReportMedalGUI> crewMemberMedalGuiList = new HashMap<String, CampaignReportMedalGUI>();
        for (MedalEvent medalEvent : medalsAwarded)
        {
            CrewMember referencePlayer = campaign.findReferencePlayer();
            if (medalEvent.getSquadronId() == referencePlayer.getCompanyId())
            {
                if (medalEvent.isNewsWorthy())
                {
                    CampaignReportMedalGUI medalGui = new CampaignReportMedalGUI(campaign, medalEvent);
                    String tabName = "Medal Awarded: " + medalEvent.getCrewMemberName();
                    crewMemberMedalGuiList.put(tabName, medalGui);
                }
            }
        }
        
        return crewMemberMedalGuiList;
	}
}
