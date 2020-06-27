package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.IAAREventPanel;
import pwcg.gui.utils.ImageResizingPanel;

public class AARMedalPanel extends ImageResizingPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private AARCoordinator aarCoordinator;
    private boolean shouldDisplay = false;

    public AARMedalPanel(Campaign campaign)
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

            JTabbedPane eventTabPane = createTab();
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
            SquadronMember referencePlayer = campaign.findReferencePlayer();
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
