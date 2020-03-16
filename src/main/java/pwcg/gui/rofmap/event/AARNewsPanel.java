package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.aar.ui.events.model.NewspaperEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class AARNewsPanel extends AAREventPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private Campaign campaign;
    private SquadronMember referencePlayer;

    private HashMap<String, ImageResizingPanel> newsGuiList = new HashMap<String, ImageResizingPanel>();

    public AARNewsPanel(Campaign campaign)
	{
        super();
        this.campaign = campaign;
        this.aarCoordinator = AARCoordinator.getInstance();
        this.referencePlayer = PWCGContext.getInstance().getReferencePlayer();
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            JTabbedPane eventTabPane = createNewsEventTab();
            createPostCombatReportTabs(eventTabPane);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
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

    private JTabbedPane createNewsEventTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, ImageResizingPanel> newsGuiList = createPilotNewsList() ;
        for (String tabName : newsGuiList.keySet())
        {
            eventTabPane.addTab(tabName, newsGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, ImageResizingPanel> createPilotNewsList() throws PWCGException 
	{
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        makeHistoricalNewspaperEvents();
        makeHistoricalAceEvents(campaign);
        makeEndOfWarEvents(campaign);
        
        return newsGuiList;
	}

    private void makeEndOfWarEvents(Campaign campaign) throws PWCGException
    {
        CampaignReportEndOfWarGUI endOfWar = null;
        Date theEnd = DateUtils.getEndOfWar();
        if (campaign.getDate().after(theEnd))
        {
             endOfWar = new CampaignReportEndOfWarGUI();
             newsGuiList.put("The War is Over!", endOfWar);
        }
    }

    private void makeHistoricalAceEvents(Campaign campaign) throws PWCGException
    {
        List<AceKilledEvent> aceKilledEvents = aarCoordinator.getAarContext().getUiDebriefData().getNewsPanelData().getAcesKilledDuringElapsedTime();
        for (AceKilledEvent aceKilledEvent : aceKilledEvents)
        {
            CampaignReportAceNewspaperGUI newspaperGui = new CampaignReportAceNewspaperGUI(aceKilledEvent, campaign);
            String tabName = "News: " + aceKilledEvent.getPilotName();
            newsGuiList.put(tabName, newspaperGui);
        }
    }

    private void makeHistoricalNewspaperEvents() throws PWCGException
    {
        List<NewspaperEvent> newspaperEvents = aarCoordinator.getAarContext().getUiDebriefData().getNewsPanelData().getNewspaperEventsDuringElapsedTime();
        for (NewspaperEvent newspaperEvent : newspaperEvents)
		{
        	if (referencePlayer.determineCountry(campaign.getDate()).getSide() == newspaperEvent.getSide())
        	{
	            CampaignReportNewspaperGUI newspaperGui = new CampaignReportNewspaperGUI(newspaperEvent);
	            String tabName = "News from the front: " + DateUtils.getDateStringPretty(newspaperEvent.getDate());
	            newsGuiList.put(tabName, newspaperGui);
        	}
		}
    }
	
    @Override
    public void finished()
    {
    }
}
