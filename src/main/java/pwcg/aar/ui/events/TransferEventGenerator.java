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

    public List<TransferEvent> createCrewMemberTransferEvents(List<TransferRecord> squadronMembersTransferred) throws PWCGException
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
        boolean isNewsworthy = true;
        int leaveTimeInDays = 0;
        int crewMemberSerialNumber = transferRecord.getCrewMember().getSerialNumber();
        TransferEvent transferEvent = new TransferEvent(campaign, transferRecord.getTransferFrom(), transferRecord.getTransferTo(), leaveTimeInDays, crewMemberSerialNumber, campaign.getDate(), isNewsworthy);
        return transferEvent;
    }
}
