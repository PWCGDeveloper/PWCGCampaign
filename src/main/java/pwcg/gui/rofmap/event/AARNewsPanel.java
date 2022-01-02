package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.newspapers.Newspaper;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.campaign.activity.NewspaperAceLostUI;
import pwcg.gui.campaign.activity.NewspaperEndOfWarUI;
import pwcg.gui.campaign.activity.NewspaperUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARNewsPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private JTabbedPane eventTabPane;

    private HashMap<String, JPanel> newsGuiList = new HashMap<>();

    public AARNewsPanel()
	{
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            createEventTab();
            createNewsEventTab();
            createPostCombatReportTabs();
            this.add(eventTabPane, BorderLayout.WEST);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
	
    private void createPostCombatReportTabs()
    {
        JPanel postCombatPanel = new JPanel(new BorderLayout());
        postCombatPanel.setOpaque(false);

        postCombatPanel.add(eventTabPane, BorderLayout.CENTER);
        this.add(postCombatPanel, BorderLayout.CENTER);
    }

    private void createEventTab() throws PWCGException
    {
        eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(ColorMap.NEWSPAPER_BACKGROUND);
        eventTabPane.setOpaque(false);
    }
    
    private void createNewsEventTab() throws PWCGException
    {
        HashMap<String, JPanel> newsGuiList = createCrewMemberNewsList() ;
        for (String tabName : newsGuiList.keySet())
        {
            eventTabPane.addTab(tabName, newsGuiList.get(tabName));
            this.shouldDisplay = true;
        }
    }

	private HashMap<String, JPanel> createCrewMemberNewsList() throws PWCGException 
	{
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        makeHistoricalNewspaperEvents();
        makeHistoricalAceEvents(campaign);
        makeEndOfWarEvents(campaign);
        
        return newsGuiList;
	}

    private void makeEndOfWarEvents(Campaign campaign) throws PWCGException
    {
        NewspaperEndOfWarUI endOfWar = null;
        Date theEnd = DateUtils.getEndOfWar();
        if (!campaign.getDate().before(theEnd))
        {
             endOfWar = new NewspaperEndOfWarUI();
             endOfWar.makePanels();
             newsGuiList.put("The War is Over!", endOfWar);
        }
    }

    private void makeHistoricalAceEvents(Campaign campaign) throws PWCGException
    {
        List<AceKilledEvent> aceKilledEvents = aarCoordinator.getAarContext().getUiDebriefData().getNewsPanelData().getAcesKilledDuringElapsedTime();
        for (AceKilledEvent aceKilledEvent : aceKilledEvents)
        {
            CrewMember deadAce = campaign.getPersonnelManager().getAnyCampaignMember(aceKilledEvent.getCrewMemberSerialNumber());
            if (deadAce != null)
            {
                NewspaperAceLostUI newspaperGui = new NewspaperAceLostUI(deadAce);
                newspaperGui.makePanels();
                String tabName = "News: " + aceKilledEvent.getCrewMemberName();
                newsGuiList.put(tabName, newspaperGui);
            }
        }
    }

    private void makeHistoricalNewspaperEvents() throws PWCGException
    {
        List<Newspaper> newspapers = aarCoordinator.getAarContext().getUiDebriefData().getNewsPanelData().getNewspaperEventsDuringElapsedTime();
        for (Newspaper newspaper : newspapers)
		{
            NewspaperUI newspaperPage = new NewspaperUI(newspaper);
            newspaperPage.makePanels();
            String tabName = "News from the front: " + DateUtils.getDateStringPretty(newspaper.getNewspaperEventDate());
            newsGuiList.put(tabName, newspaperPage);
		}
    }
}
