package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.TransferEvent;
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

public class AARPilotsTransferredPanel extends AAREventPanel
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private AARCoordinator aarCoordinator;
    private SquadronMember referencePlayer;

    public AARPilotsTransferredPanel(Campaign campaign)
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
            JTabbedPane eventTabPane = createTransferTab();
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
}
