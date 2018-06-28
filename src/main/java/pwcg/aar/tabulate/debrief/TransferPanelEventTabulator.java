package pwcg.aar.tabulate.debrief;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase2.transfer.TransferRecord;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.aar.ui.events.TransferEventGenerator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
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
        transferIntoSquadron();
        transferOutOfSquadron();
        
        return transferPanelData;
    }
    
    private void transferIntoSquadron() throws PWCGException
    {
        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> allTransferInEvents = new ArrayList<>();

        List<TransferEvent> transferEventsForSquadronAces = transferInForAces(transferEventGenerator);
        allTransferInEvents.addAll(transferEventsForSquadronAces);
        
        List<TransferEvent> transferEventsForSquadronMembers = transferInForSquadronMembers(transferEventGenerator);
        allTransferInEvents.addAll(transferEventsForSquadronMembers);
        
        transferPanelData.setTransferIntoSquadron(allTransferInEvents);
    }

    private List<TransferEvent> transferInForAces(TransferEventGenerator transferEventGenerator) throws PWCGException
    {
        List <TransferRecord> acesTransferredIn = new ArrayList<>();
        acesTransferredIn.addAll(aarContext.getReconciledOutOfMissionData().getTransferData().getAcesTransferred().getSquadronMembersTransferredToSquadron(campaign.getSquadronId()));
        List<TransferEvent> transferEventsForAces = transferEventGenerator.createPilotTransferEventsIntoSquadron(acesTransferredIn);
        List<TransferEvent> transferEventsForSquadronAces = getTransfersInForCurrentSquadron(transferEventsForAces);
        return transferEventsForSquadronAces;
    }

    private List<TransferEvent> transferInForSquadronMembers(TransferEventGenerator transferEventGenerator) throws PWCGException
    {
        List <TransferRecord> squadronMembersTransferredIn = new ArrayList<>();
        squadronMembersTransferredIn.addAll(aarContext.getReconciledOutOfMissionData().getTransferData().getSquadronTransferData().getSquadronMembersTransferredToSquadron(campaign.getSquadronId()));
        List<TransferEvent> transferEventsForCampaignMembers = transferEventGenerator.createPilotTransferEventsIntoSquadron(squadronMembersTransferredIn);
        List<TransferEvent> transferEventsForSquadronMembers = getTransfersInForCurrentSquadron(transferEventsForCampaignMembers);
        return transferEventsForSquadronMembers;
    }
    
    private void transferOutOfSquadron() throws PWCGException
    {
        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> allTransferInEvents = new ArrayList<>();

        List<TransferEvent> transferEventsForSquadronAces = tranferOutForAces(transferEventGenerator);
        allTransferInEvents.addAll(transferEventsForSquadronAces);
        
        List<TransferEvent> transferEventsForSquadronMembers = tranferOutForSquadronMembers(transferEventGenerator);
        allTransferInEvents.addAll(transferEventsForSquadronMembers);
        
        transferPanelData.setTransferOutOfSquadron(allTransferInEvents);
    }

    private List<TransferEvent> tranferOutForSquadronMembers(TransferEventGenerator transferEventGenerator) throws PWCGException
    {
        List <TransferRecord> transferEventsOut = new ArrayList<>();
        transferEventsOut.addAll(aarContext.getReconciledOutOfMissionData().getTransferData().getSquadronTransferData().getSquadronMembersTransferredFromSquadron(campaign.getSquadronId()));
        List<TransferEvent> transferEventsForCampaignMembers = transferEventGenerator.createPilotTransferEventsOutOfSquadron(transferEventsOut);
        List<TransferEvent> transferEventsForSquadronMembers = getTransfersOutForCurrentSquadron(transferEventsForCampaignMembers);
        return transferEventsForSquadronMembers;
    }

    private List<TransferEvent> tranferOutForAces(TransferEventGenerator transferEventGenerator) throws PWCGException
    {
        List <TransferRecord> acesTransferredOut = new ArrayList<>();
        acesTransferredOut.addAll(aarContext.getReconciledOutOfMissionData().getTransferData().getAcesTransferred().getSquadronMembersTransferredFromSquadron(campaign.getSquadronId()));
        List<TransferEvent> transferEventsForAces = transferEventGenerator.createPilotTransferEventsOutOfSquadron(acesTransferredOut);
        List<TransferEvent> transferEventsForSquadronAces = getTransfersOutForCurrentSquadron(transferEventsForAces);
        return transferEventsForSquadronAces;
    }
    
    private List<TransferEvent> getTransfersInForCurrentSquadron(List<TransferEvent> transferEventsForCampaignMembers) throws PWCGException
    {
        List<TransferEvent> transferEventsForSquadronMembers = new ArrayList<>();
        
        for (TransferEvent transferEvent : transferEventsForCampaignMembers)
        {
            if (transferEvent.getTransferTo() == campaign.getSquadronId())
            {
                transferEventsForSquadronMembers.add(transferEvent);
            }
        }
        return transferEventsForSquadronMembers;
    }
    
    private List<TransferEvent> getTransfersOutForCurrentSquadron(List<TransferEvent> transferEventsForCampaignMembers) throws PWCGException
    {
        List<TransferEvent> transferEventsForSquadronMembers = new ArrayList<>();
        
        for (TransferEvent transferEvent : transferEventsForCampaignMembers)
        {
            if (transferEvent.getTransferFrom() == campaign.getSquadronId())
            {
                transferEventsForSquadronMembers.add(transferEvent);
            }
        }
        return transferEventsForSquadronMembers;
    }

}
