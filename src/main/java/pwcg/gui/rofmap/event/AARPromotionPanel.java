package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.PromotionEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARPromotionPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private AARCoordinator aarCoordinator;

    public AARPromotionPanel(Campaign campaign)
	{
        super();

        this.campaign = campaign;
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanel()  
	{
        try
        {
            JTabbedPane eventTabPane = createCrewMemberPromotionTab();
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

    private JTabbedPane createCrewMemberPromotionTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, CampaignReportPromotionGUI> crewMemberPromotionGuiList = createCrewMemberPromotionList() ;
        for (String tabName : crewMemberPromotionGuiList.keySet())
        {
            eventTabPane.addTab(tabName, crewMemberPromotionGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportPromotionGUI> createCrewMemberPromotionList() throws PWCGException 
	{
        HashMap<String, CampaignReportPromotionGUI> crewMemberPromotionGuiList = new HashMap<String, CampaignReportPromotionGUI>();
        List<PromotionEvent> promotionEvents = aarCoordinator.getAarContext().getUiDebriefData().getPromotionPanelData().getPromotionEventsDuringElapsedTime();
        for (PromotionEvent promotionEvent : promotionEvents)
		{
            if (promotionEvent.getSquadronId() == campaign.findReferencePlayer().getCompanyId())
            {
                CampaignReportPromotionGUI promotionGui = new CampaignReportPromotionGUI(campaign, promotionEvent);
                String tabName = "Promotion Awarded: " + promotionEvent.getNewRank() + " " + promotionEvent.getCrewMemberName();
                crewMemberPromotionGuiList.put(tabName, promotionGui);
            }
		}
        
        return crewMemberPromotionGuiList;
	}
}
