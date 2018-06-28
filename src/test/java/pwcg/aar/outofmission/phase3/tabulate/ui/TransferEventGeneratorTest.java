package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.outofmission.phase2.transfer.TransferRecord;
import pwcg.aar.ui.events.TransferEventGenerator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class TransferEventGeneratorTest extends AARTestSetup
{

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        setupAARMocks();
    }

    @Test
    public void testTransferInEvent() throws PWCGException 
    {
        List<TransferRecord> transferRecords = new ArrayList<>();
        TransferRecord squadronMemberTransfer1 = new TransferRecord(pilot1, 101048, 101103);
        transferRecords.add(squadronMemberTransfer1);        
        TransferRecord squadronMemberTransfer2 = new TransferRecord(pilot2, 101048, 101103);
        transferRecords.add(squadronMemberTransfer2);        

        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> transferEvents = transferEventGenerator.createPilotTransferEventsIntoSquadron(transferRecords);
        TransferEvent transferEvent1 = (TransferEvent)transferEvents.get(0);
        TransferEvent transferEvent2 = (TransferEvent)transferEvents.get(1);

        assert (transferEvents.size() == 2);
        assert (transferEvent1.getTransferTo() == campaign.getSquadronId());
        assert (transferEvent2.getTransferTo() == campaign.getSquadronId());
    }
    
    
    @Test
    public void testInSquadronAceUpdate() throws PWCGException 
    {
        List<TransferRecord> transferRecords = new ArrayList<>();
        TransferRecord squadronMemberTransfer1 = new TransferRecord(pilot1, 101103, 101048);
        transferRecords.add(squadronMemberTransfer1);        
        TransferRecord squadronMemberTransfer2 = new TransferRecord(pilot2, 101103, 101048);
        transferRecords.add(squadronMemberTransfer2);        

        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> transferEvents = transferEventGenerator.createPilotTransferEventsOutOfSquadron(transferRecords);
        TransferEvent transferEvent1 = (TransferEvent)transferEvents.get(0);
        TransferEvent transferEvent2 = (TransferEvent)transferEvents.get(1);

        assert (transferEvents.size() == 2);
        assert (transferEvent1.getTransferTo() != campaign.getSquadronId());
        assert (transferEvent2.getTransferTo() != campaign.getSquadronId());
    }

}
