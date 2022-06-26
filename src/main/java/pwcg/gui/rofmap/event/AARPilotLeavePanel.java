package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.AceLeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARPilotLeavePanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private Campaign campaign;

    public AARPilotLeavePanel(Campaign campaign)
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
       
        HashMap<String, CampaignReportAceLeaveGUI> pilotLeaveGuiList = createPilotLeaveList() ;
        for (String tabName : pilotLeaveGuiList.keySet())
        {
            eventTabPane.addTab(tabName, pilotLeaveGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportAceLeaveGUI> createPilotLeaveList() throws PWCGException 
	{
        HashMap<String, CampaignReportAceLeaveGUI> pilotLeaveGuiList = new HashMap<String, CampaignReportAceLeaveGUI>();

        List<AceLeaveEvent> aceLeaveEvents = aarCoordinator.getAarContext().getUiDebriefData().getAceLeavePanelData().getAcesOnLeaveDuringElapsedTime();
        for (AceLeaveEvent aceLeaveEvent : aceLeaveEvents)
		{
            if (aceLeaveEvent.getSquadronId() == campaign.findReferencePlayer().getSquadronId())
            {
                CampaignReportAceLeaveGUI leaveGui = new CampaignReportAceLeaveGUI(aceLeaveEvent, campaign);
                String tabName = "Leave: " + aceLeaveEvent.getPilotName();
                pilotLeaveGuiList.put(tabName, leaveGui);
            }
		}
        
        return pilotLeaveGuiList;
	}
}
