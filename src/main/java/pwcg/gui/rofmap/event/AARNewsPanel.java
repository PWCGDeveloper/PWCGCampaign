package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
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
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.IAAREventPanel;
import pwcg.gui.utils.ImageResizingPanel;

public class AARNewsPanel extends ImageResizingPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private Campaign campaign;
    private boolean shouldDisplay = false;

    private HashMap<String, ImageResizingPanel> newsGuiList = new HashMap<String, ImageResizingPanel>();

    public AARNewsPanel(Campaign campaign)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            String imagePath = UiImageResolver.getImageMain("document.png");
            this.setImage(imagePath);

            JTabbedPane eventTabPane = createNewsEventTab();
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
            SquadronMember referencePlayer = campaign.findReferencePlayer();
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
