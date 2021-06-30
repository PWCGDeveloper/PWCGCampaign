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
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARPilotsTransferredPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private AARCoordinator aarCoordinator;

    public AARPilotsTransferredPanel(Campaign campaign)
	{
        super();

        this.campaign = campaign;
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            JTabbedPane eventTabPane = createTransferTab();
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
        HashMap<String, CampaignReportTransferPanel> pilotTransferredGuiList = createPilotTransferredList(transferEventsForSquadron) ;
        for (String tabName : pilotTransferredGuiList.keySet())
        {
            eventTabPane.addTab(tabName, pilotTransferredGuiList.get(tabName));
            this.shouldDisplay = true;
        }
    }

	private HashMap<String, CampaignReportTransferPanel> createPilotTransferredList(List<TransferEvent> transferEventsForSquadron) throws PWCGException 
	{
        HashMap<String, CampaignReportTransferPanel> pilotTransferredGuiList = new HashMap<String, CampaignReportTransferPanel>();

        for (TransferEvent transferEvent : transferEventsForSquadron)
        {
            SquadronMember referencePlayer = campaign.findReferencePlayer();
            if (transferEvent.getTransferTo() == referencePlayer.getSquadronId() || transferEvent.getTransferFrom() == referencePlayer.getSquadronId())
            {
                CampaignReportTransferPanel transferGui = new CampaignReportTransferPanel(campaign, transferEvent);
                String tabName = "Pilot Transferred: " + transferEvent.getPilotName();
                pilotTransferredGuiList.put(tabName, transferGui);
            }
        }
        
        return pilotTransferredGuiList;
	}
}
