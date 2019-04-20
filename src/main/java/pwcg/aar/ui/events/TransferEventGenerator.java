package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;

public class TransferEventGenerator
{
    private Campaign campaign;
    
    public TransferEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<TransferEvent> createPilotTransferEvents(List<TransferRecord> squadronMembersTransferred) throws PWCGException
    {
        List<TransferEvent>  transferEvents = new ArrayList<>();
        for (TransferRecord transferred : squadronMembersTransferred)
        {
            TransferEvent transferEvent = makeTransferEvent(transferred);
            transferEvents.add(transferEvent);
        }
        
        return transferEvents;
    }
    
    private TransferEvent makeTransferEvent(TransferRecord transferRecord) throws PWCGException  
    {
        TransferEvent transferEvent = new TransferEvent(transferRecord.getTransferTo());
        transferEvent.setPilotName(transferRecord.getSquadronMember().getNameAndRank());
        transferEvent.setDate(campaign.getDate());
        
        transferEvent.setTransferTo(transferRecord.getTransferTo());        
        transferEvent.setTransferFrom(transferRecord.getTransferFrom());

        return transferEvent;
    }
}
