package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.layout.Pane;
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
            JTabbedPane eventTabPane = createPilotVictoriesTab();
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
        Pane postCombatPanel = new Pane(new BorderLayout());
        postCombatPanel.setOpaque(false);

        postCombatPanel.add(eventTabPane, BorderLayout.CENTER);
        this.add(postCombatPanel, BorderLayout.CENTER);
    }

    private JTabbedPane createPilotVictoriesTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, CampaignReportVictoryGUI> equipmentGuiList = createPilotLostSubTabs() ;
        for (String tabName : equipmentGuiList.keySet())
        {
            eventTabPane.addTab(tabName, equipmentGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportVictoryGUI> createPilotLostSubTabs() throws PWCGException 
	{
	    HashMap<String, List<VictoryEvent>> victoriesByPilotsInMySquadron = collateVictoriesByPilotInMySquadron();
        HashMap<String, CampaignReportVictoryGUI> pilotVictoryPanelData = createVictoryTabs(victoriesByPilotsInMySquadron);
        return pilotVictoryPanelData;
	}

    private HashMap<String, List<VictoryEvent>> collateVictoriesByPilotInMySquadron() throws PWCGException
    {
        HashMap<String, List<VictoryEvent>> victoriesByPilotsInMySquadron = new HashMap<>();
        List<VictoryEvent>  outOfMissionVictories = aarCoordinator.getAarContext().getUiDebriefData().getOutOfMissionVictoryPanelData().getOutOfMissionVictoryEvents();
        for (VictoryEvent victoryEvent : outOfMissionVictories)
		{
            if (victoryEvent.getSquadronId() == campaign.findReferencePlayer().getSquadronId())
            {
                if (!victoriesByPilotsInMySquadron.containsKey(victoryEvent.getPilotName()))
                {
                    List<VictoryEvent> victoriesForPilot = new ArrayList<>();
                    victoriesByPilotsInMySquadron.put(victoryEvent.getPilotName(), victoriesForPilot);
                }
                List<VictoryEvent> victoriesForPilot = victoriesByPilotsInMySquadron.get(victoryEvent.getPilotName());
                victoriesForPilot.add(victoryEvent);
            }
		}
        return victoriesByPilotsInMySquadron;
    }

    private HashMap<String, CampaignReportVictoryGUI> createVictoryTabs(HashMap<String, List<VictoryEvent>> victoriesByPilotsInMySquadron) throws PWCGException
    {
        HashMap<String, CampaignReportVictoryGUI> pilotVictoryPanelData = new HashMap<>();
        for (String pilotName : victoriesByPilotsInMySquadron.keySet())
        {
            List<VictoryEvent> victoriesForPilot = victoriesByPilotsInMySquadron.get(pilotName);
            CampaignReportVictoryGUI victoryGUI = new CampaignReportVictoryGUI(campaign, victoriesForPilot);
            String tabName = "Notification of Victory: " + pilotName;
            pilotVictoryPanelData.put(tabName, victoryGUI);
        }
        return pilotVictoryPanelData;
    }
}
