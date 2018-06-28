package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;

import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class AARPilotLossPanel extends AAREventPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;

    public AARPilotLossPanel()
	{
        super();
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            JTabbedPane eventTabPane = createPilotsLostTab();
            createPostCombatReportTabs(eventTabPane);
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
    
    private void createPostCombatReportTabs(JTabbedPane eventTabPane)
    {
        ImageResizingPanel postCombatPanel = new ImageResizingPanel(ContextSpecificImages.imagesMisc() + "PaperPart.jpg");
        postCombatPanel.setLayout(new BorderLayout());
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
            
            shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportPilotStatusGUI> createPilotLostSubTabs() throws PWCGException 
	{
        HashMap<String, CampaignReportPilotStatusGUI> pilotLostGuiList = new HashMap<String, CampaignReportPilotStatusGUI>();

        for (PilotStatusEvent pilotStatusEvent : aarCoordinator.getAarContext().getUiCombatReportData().getCombatReportPanelData().getSquadronMembersLostInMission().values())
		{
            CampaignReportPilotStatusGUI pilotLostGui = new CampaignReportPilotStatusGUI(pilotStatusEvent);
            String tabName = "Pilot Lost: " + pilotStatusEvent.getPilot().getNameAndRank();
            pilotLostGuiList.put(tabName, pilotLostGui);
		}
        
        return pilotLostGuiList;
	}

	
    @Override
    public void finished()
    {
    }
}
