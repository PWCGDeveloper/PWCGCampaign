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
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARCrewMembersTransferredPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;
    private Campaign campaign;
    private AARCoordinator aarCoordinator;

    public AARCrewMembersTransferredPanel(Campaign campaign)
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
        HashMap<String, CampaignReportTransferPanel> crewMemberTransferredGuiList = createCrewMemberTransferredList(transferEventsForSquadron) ;
        for (String tabName : crewMemberTransferredGuiList.keySet())
        {
            eventTabPane.addTab(tabName, crewMemberTransferredGuiList.get(tabName));
            this.shouldDisplay = true;
        }
    }

	private HashMap<String, CampaignReportTransferPanel> createCrewMemberTransferredList(List<TransferEvent> transferEventsForSquadron) throws PWCGException 
	{
        HashMap<String, CampaignReportTransferPanel> crewMemberTransferredGuiList = new HashMap<String, CampaignReportTransferPanel>();

        for (TransferEvent transferEvent : transferEventsForSquadron)
        {
            CrewMember referencePlayer = campaign.findReferencePlayer();
            if (transferEvent.getTransferTo() == referencePlayer.getCompanyId() || transferEvent.getTransferFrom() == referencePlayer.getCompanyId())
            {
                CampaignReportTransferPanel transferGui = new CampaignReportTransferPanel(campaign, transferEvent);
                String tabName = "CrewMember Transferred: " + transferEvent.getCrewMemberName();
                crewMemberTransferredGuiList.put(tabName, transferGui);
            }
        }
        
        return crewMemberTransferredGuiList;
	}
}
