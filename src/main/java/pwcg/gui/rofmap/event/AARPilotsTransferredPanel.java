package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.debrief.IAAREventPanel;
import pwcg.gui.utils.ImageResizingPanel;

public class AARPilotsTransferredPanel extends ImageResizingPanel implements IAAREventPanel
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private AARCoordinator aarCoordinator;
    private boolean shouldDisplay = false;

    public AARPilotsTransferredPanel(Campaign campaign)
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

            JTabbedPane eventTabPane = createTransferTab();
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

    private JTabbedPane createTransferTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        makeTransferTabs(eventTabPane, aarCoordinator.getAarContext().getUiDebriefData().getTransferPanelData().getTransfers());
        return eventTabPane;
    }

    private void makeTransferTabs(JTabbedPane eventTabPane, List<TransferEvent> transferEventsForSquadron) throws PWCGException
    {
        HashMap<String, AARTransferPanel> pilotTransferredGuiList = createPilotTransferredList(transferEventsForSquadron) ;
        for (String tabName : pilotTransferredGuiList.keySet())
        {
            eventTabPane.addTab(tabName, pilotTransferredGuiList.get(tabName));
            this.shouldDisplay = true;
        }
    }

	private HashMap<String, AARTransferPanel> createPilotTransferredList(List<TransferEvent> transferEventsForSquadron) throws PWCGException 
	{
        HashMap<String, AARTransferPanel> pilotTransferredGuiList = new HashMap<String, AARTransferPanel>();

        for (TransferEvent transferEvent : transferEventsForSquadron)
        {
            SquadronMember referencePlayer = campaign.findReferencePlayer();
            if (transferEvent.getTransferTo() == referencePlayer.getSquadronId() || transferEvent.getTransferFrom() == referencePlayer.getSquadronId())
            {
                AARTransferPanel transferGui = new AARTransferPanel(campaign, transferEvent);
                String tabName = "Pilot Transferred: " + transferEvent.getPilotName();
                pilotTransferredGuiList.put(tabName, transferGui);
            }
        }
        
        return pilotTransferredGuiList;
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
