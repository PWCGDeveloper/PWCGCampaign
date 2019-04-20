package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.PromotionEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
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

    private Campaign campaign;
    private AARCoordinator aarCoordinator;
    private SquadronMember referencePlayer;

    public AARPromotionPanel(Campaign campaign)
	{
        super();
        this.campaign = campaign;
        this.aarCoordinator = AARCoordinator.getInstance();
        this.referencePlayer = PWCGContextManager.getInstance().getReferencePlayer();
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
        HashMap<String, CampaignReportPromotionGUI> pilotPromotionGuiList = new HashMap<String, CampaignReportPromotionGUI>();
        List<PromotionEvent> promotionEvents = aarCoordinator.getAarContext().getAarTabulatedData().getUiDebriefData().getPromotionPanelData().getPromotionEventsDuringElapsedTime();
        for (PromotionEvent promotionEvent : promotionEvents)
		{
            if (promotionEvent.getSquadronId() == referencePlayer.getSquadronId())
            {
                CampaignReportPromotionGUI promotionGui = new CampaignReportPromotionGUI(campaign, promotionEvent);
                String tabName = "Promotion Awarded: " + promotionEvent.getNewRank() + " " + promotionEvent.getPilotName();
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
