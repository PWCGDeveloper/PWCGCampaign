package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.outofmission.phase2.resupply.TransferRecord;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class TransferEventGenerator
{
    private Campaign campaign;
    
    public TransferEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<TransferEvent> createPilotTransferEventsIntoSquadron(List<TransferRecord> squadronMembersTransferredIn) throws PWCGException
    {
        List<TransferEvent> transferEventsIntoSquadron = new ArrayList<>();
        
        for (TransferRecord transferredeIn : squadronMembersTransferredIn)
        {
            if (transferredeIn.getTransferTo() == campaign.getSquadronId())
            {
                TransferEvent transferEvent = makeTransferEventIntoSquadron(transferredeIn);
                transferEventsIntoSquadron.add(transferEvent);
            }
        }
        
        return transferEventsIntoSquadron;
    }
    
    public List<TransferEvent> createPilotTransferEventsOutOfSquadron(List<TransferRecord> squadronMembersTransferredOut) throws PWCGException
    {
        List<TransferEvent> transferEventsOutOfSquadron = new ArrayList<>();
        
        for (TransferRecord transferredeOut : squadronMembersTransferredOut)
        {
            if (transferredeOut.getTransferFrom() == campaign.getSquadronId())
            {
                TransferEvent transferEvent = makeTransferEventOutOfSquadron(transferredeOut.getSquadronMember());
                transferEventsOutOfSquadron.add(transferEvent);
            }
        }
        
        return transferEventsOutOfSquadron;
    }

    
    private TransferEvent makeTransferEventIntoSquadron(TransferRecord transferRecord) throws PWCGException  
    {
        TransferEvent transferEvent = new TransferEvent();
        transferEvent.setPilot(transferRecord.getSquadronMember());
        transferEvent.setTransferIn(true);
        transferEvent.setDate(campaign.getDate());
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(transferRecord.getTransferTo());
        transferEvent.setSquadron(squadron.determineDisplayName(campaign.getDate()));
        transferEvent.setTransferTo(transferRecord.getTransferTo());
        
        return transferEvent;
    }

    private TransferEvent makeTransferEventOutOfSquadron(SquadronMember pilot) throws PWCGException  
    {
        TransferEvent transferEvent = new TransferEvent();
        transferEvent.setPilot(pilot);
        transferEvent.setTransferIn(false);
        transferEvent.setDate(campaign.getDate());
        Squadron squadron = pilot.determineSquadron();
        if (squadron != null)
        {
            transferEvent.setSquadron(squadron.determineDisplayName(campaign.getDate()));
        }
        
        transferEvent.setTransferFrom(campaign.getSquadronId());

        if (pilot.getSquadronId() != campaign.getSquadronId())
        {
            transferEvent.setTransferTo(pilot.getSquadronId());
        }
        else
        {
            SquadronManager squadManager = PWCGContextManager.getInstance().getSquadronManager();
            int transferTo = squadManager.chooseSquadronForTransfer(campaign, pilot);
            transferEvent.setTransferTo(transferTo);
        }
        
        return transferEvent;
    }
}
