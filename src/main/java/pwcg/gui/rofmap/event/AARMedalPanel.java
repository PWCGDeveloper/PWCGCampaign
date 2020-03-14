package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.AAREventPanel;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class AARMedalPanel extends AAREventPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private AARCoordinator aarCoordinator;
    private SquadronMember referencePlayer;

    public AARMedalPanel(Campaign campaign)
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
       
        HashMap<String, CampaignReportMedalGUI> pilotMedalGuiList = createPilotMedalList() ;
        for (String tabName : pilotMedalGuiList.keySet())
        {
            eventTabPane.addTab(tabName, pilotMedalGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportMedalGUI> createPilotMedalList() throws PWCGException 
	{
        List<MedalEvent> medalsAwarded = aarCoordinator.getAarContext().getUiDebriefData().getMedalPanelData().getMedalsAwarded();
        HashMap<String, CampaignReportMedalGUI> pilotMedalGuiList = new HashMap<String, CampaignReportMedalGUI>();
        for (MedalEvent medalEvent : medalsAwarded)
        {
            if (medalEvent.getSquadronId() == referencePlayer.getSquadronId())
            {
                if (medalEvent.isNewsWorthy())
                {
                    CampaignReportMedalGUI medalGui = new CampaignReportMedalGUI(campaign, medalEvent);
                    String tabName = "Medal Awarded: " + medalEvent.getPilotName();
                    pilotMedalGuiList.put(tabName, medalGui);
                }
            }
        }
        
        return pilotMedalGuiList;
	}

	
    @Override
    public void finished()
    {
    }
}
