package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;

import javax.swing.JTabbedPane;

import pwcg.aar.ui.events.model.AceLeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class AARPilotLeavePanel extends AAREventPanel
{
    private static final long serialVersionUID = 1L;

    public AARPilotLeavePanel()
	{
        super();
	}

    public void makePanel() throws PWCGException  
	{
        try
        {
            JTabbedPane eventTabPane = createTab();
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
            
            shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportAceLeaveGUI> createPilotLeaveList() throws PWCGException 
	{
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();

        HashMap<String, CampaignReportAceLeaveGUI> pilotLeaveGuiList = new HashMap<String, CampaignReportAceLeaveGUI>();

        for (AceLeaveEvent aceLeaveEvent : aarContext.getUiDebriefData().getAceLeavePanelData().getAcesOnLeaveDuringElapsedTime())
		{
            CampaignReportAceLeaveGUI leaveGui = new CampaignReportAceLeaveGUI(aceLeaveEvent, campaign);
            String tabName = "Leave: " + aceLeaveEvent.getPilot().getNameAndRank();
            
            pilotLeaveGuiList.put(tabName, leaveGui);
		}
        
        return pilotLeaveGuiList;
	}

	
    @Override
    public void finished()
    {
    }
}
