package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.IAAREventPanel;
import pwcg.gui.utils.ImageResizingPanel;

public class AARPilotLossPanel extends ImageResizingPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private Campaign campaign;
    private boolean shouldDisplay = false;

    public AARPilotLossPanel(Campaign campaign)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.aarCoordinator = AARCoordinator.getInstance();
        this.campaign = campaign;
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            String imagePath = UiImageResolver.getImageMain("document.png");
            this.setImage(imagePath);

            JTabbedPane eventTabPane = createPilotsLostTab();
            createPostCombatReportTabs(eventTabPane);
            this.add(eventTabPane, BorderLayout.CENTER);
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

    private JTabbedPane createPilotsLostTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, CampaignReportPilotStatusGUI> pilotLostGuiList = createPilotLostSubTabs() ;
        for (String tabName : pilotLostGuiList.keySet())
        {
            eventTabPane.addTab(tabName, pilotLostGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportPilotStatusGUI> createPilotLostSubTabs() throws PWCGException 
	{
	    SquadronMember referencePlayer = campaign.findReferencePlayer();
        AARCombatReportPanelData combatReportData = aarCoordinator.getAarContext()
                        .findUiCombatReportDataForSquadron(referencePlayer.getSquadronId()).getCombatReportPanelData();

        HashMap<String, CampaignReportPilotStatusGUI> pilotLostGuiList = new HashMap<>();
        for (PilotStatusEvent pilotStatusEvent : combatReportData.getSquadronMembersLostInMission().values())
        {
            if (pilotStatusEvent.getSquadronId() == referencePlayer.getSquadronId())
            {
                CampaignReportPilotStatusGUI pilotLostGui = new CampaignReportPilotStatusGUI(pilotStatusEvent);
                String tabName = "Pilot Lost: " + pilotStatusEvent.getPilotName();
                pilotLostGuiList.put(tabName, pilotLostGui);
            }
        }
        
        Map<Integer, PilotStatusEvent> pilotsLostOutOfMission = aarCoordinator.getAarContext().getUiDebriefData().getPilotLossPanelData().getSquadMembersLost();
        for (PilotStatusEvent pilotStatusEvent : pilotsLostOutOfMission.values())
        {
            if (pilotStatusEvent.getSquadronId() == referencePlayer.getSquadronId())
            {
                CampaignReportPilotStatusGUI pilotLostGui = new CampaignReportPilotStatusGUI(pilotStatusEvent);
                String tabName = "Pilot Lost: " + pilotStatusEvent.getPilotName();
                pilotLostGuiList.put(tabName, pilotLostGui);
            }
        }
        
        return pilotLostGuiList;
	}

    @Override
    public void finished()
    {
    }

    @Override
    public boolean isShouldDisplay()
    {
        return shouldDisplay;
    }

    @Override
    public JPanel getPanel()
    {
        return this;
    }
}
