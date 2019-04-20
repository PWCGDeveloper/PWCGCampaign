package pwcg.aar.tabulate.debrief;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.aar.ui.events.TransferEventGenerator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;

public class TransferPanelEventTabulator
{
    private Campaign campaign;
    private AARContext aarContext;

    private TransferPanelData transferPanelData = new TransferPanelData();

    public TransferPanelEventTabulator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
        
    public TransferPanelData tabulateForAARTransferPanel() throws PWCGException
    {
        createTransferEvents();        
        return transferPanelData;
    }
    
    private void createTransferEvents() throws PWCGException
    {
        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> allTransferInEvents = new ArrayList<>();

        List<TransferEvent> transferEventsForSquadronAces = transferAces(transferEventGenerator);
        allTransferInEvents.addAll(transferEventsForSquadronAces);
        
        List<TransferEvent> transferEventsForSquadronMembers = transferSquadronMembers(transferEventGenerator);
        allTransferInEvents.addAll(transferEventsForSquadronMembers);
        
        transferPanelData.setTransfers(allTransferInEvents);
    }

    private List<TransferEvent> transferAces(TransferEventGenerator transferEventGenerator) throws PWCGException
    {
        List <TransferRecord> acesTransferredIn = aarContext.getReconciledOutOfMissionData().getResupplyData().getAcesTransferred().getSquadronMembersTransferred();
        List<TransferEvent> transferEventsForAces = transferEventGenerator.createPilotTransferEvents(acesTransferredIn);
        return transferEventsForAces;
    }

    private List<TransferEvent> transferSquadronMembers(TransferEventGenerator transferEventGenerator) throws PWCGException
    {
        List <TransferRecord> squadronMembersTransferredIn = aarContext.getReconciledOutOfMissionData().getResupplyData().getSquadronTransferData().getSquadronMembersTransferred();
        List<TransferEvent> transferEventsForSquadronMembers = transferEventGenerator.createPilotTransferEvents(squadronMembersTransferredIn);
        return transferEventsForSquadronMembers;
    }
}
