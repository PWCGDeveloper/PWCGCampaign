package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.ui.events.TransferEventGenerator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransferEventGeneratorTest extends AARTestSetup
{

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();
    }

    @Test
    public void testTransferInEvent() throws PWCGException 
    {
        List<TransferRecord> transferRecords = new ArrayList<>();
        TransferRecord squadronMemberTransfer1 = new TransferRecord(pilot1, SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        transferRecords.add(squadronMemberTransfer1);        
        TransferRecord squadronMemberTransfer2 = new TransferRecord(pilot2, SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        transferRecords.add(squadronMemberTransfer2);        

        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> transferEvents = transferEventGenerator.createPilotTransferEvents(transferRecords);
        TransferEvent transferEvent1 = (TransferEvent)transferEvents.get(0);
        TransferEvent transferEvent2 = (TransferEvent)transferEvents.get(1);

        assert (transferEvents.size() == 2);
        assert (transferEvent1.getTransferTo() == SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        assert (transferEvent2.getTransferTo() == SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
    }
    
    
    @Test
    public void testInSquadronAceUpdate() throws PWCGException 
    {
        List<TransferRecord> transferRecords = new ArrayList<>();
        TransferRecord squadronMemberTransfer1 = new TransferRecord(pilot1, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
        transferRecords.add(squadronMemberTransfer1);        
        TransferRecord squadronMemberTransfer2 = new TransferRecord(pilot2, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
        transferRecords.add(squadronMemberTransfer2);        

        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> transferEvents = transferEventGenerator.createPilotTransferEvents(transferRecords);
        TransferEvent transferEvent1 = (TransferEvent)transferEvents.get(0);
        TransferEvent transferEvent2 = (TransferEvent)transferEvents.get(1);

        assert (transferEvents.size() == 2);
        assert (transferEvent1.getTransferTo() != SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        assert (transferEvent2.getTransferTo() != SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
    }

}
