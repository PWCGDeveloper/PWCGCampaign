package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;

import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.PromotionEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class AARPromotionPanel extends AAREventPanel
{
    private static final long serialVersionUID = 1L;

    private AARCoordinator aarCoordinator;

    public AARPromotionPanel()
	{
        super();
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanel()  
	{
        try
        {
            JTabbedPane eventTabPane = createPilotPromotionTab();
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

    private JTabbedPane createPilotPromotionTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        // Add panes for all of the other categories
        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        // Personnel events for this squadron and former squadrons
        HashMap<String, CampaignReportPromotionGUI> pilotPromotionGuiList = createPilotPromotionList() ;
        for (String tabName : pilotPromotionGuiList.keySet())
        {
            eventTabPane.addTab(tabName, pilotPromotionGuiList.get(tabName));
            
            // If we have something then we should display
            shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportPromotionGUI> createPilotPromotionList() throws PWCGException 
	{
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();

        HashMap<String, CampaignReportPromotionGUI> pilotPromotionGuiList = new HashMap<String, CampaignReportPromotionGUI>();

        for (PromotionEvent promotionEvent: aarCoordinator.getAarContext().getUiDebriefData().getPromotionPanelData().getPromotionEventsDuringElapsedTime())
		{
            SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(promotionEvent.getPilot().getSerialNumber());
            if (squadronMember.getPilotActiveStatus() > SquadronMemberStatus.STATUS_CAPTURED)
            {
                CampaignReportPromotionGUI promotionGui = new CampaignReportPromotionGUI(promotionEvent);
                String tabName = "Promotion Awarded: " + promotionEvent.getNewRank() + " " + promotionEvent.getPilot().getName();
                pilotPromotionGuiList.put(tabName, promotionGui);
            }
		}
        
        return pilotPromotionGuiList;
	}

	
    @Override
    public void finished()
    {
    }
}
