package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.tabulate.debrief.TransferPanelEventTabulator;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.Silent.class) 
public class AARTransferPanelEventTabulatorTest extends AARTestSetup
{
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        setupAARMocks();
    }

    @Test
    public void testTransferInEvent() throws PWCGException 
    {
        List<TransferRecord> squadronMemberTransferredIn = new ArrayList<>();
        TransferRecord squadronMemberTransfer = new TransferRecord(pilot1, SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        squadronMemberTransferredIn.add(squadronMemberTransfer);        
        Mockito.when(squadronMembersTransferred.getSquadronMembersTransferred()).thenReturn(squadronMemberTransferredIn);

        List<TransferRecord> acesTransferredIn = new ArrayList<>();
        TransferRecord aceTransfer = new TransferRecord(ace1, SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        acesTransferredIn.add(aceTransfer);        
        Mockito.when(acesTransferred.getSquadronMembersTransferred()).thenReturn(acesTransferredIn);
        
        TransferPanelEventTabulator transferPanelEventTabulator = new TransferPanelEventTabulator(campaign, aarContext);
        TransferPanelData transferPanelData = transferPanelEventTabulator.tabulateForAARTransferPanel();
        
        List<TransferEvent> transferEvents = transferPanelData.getTransfers();

        assert (transferEvents.size() == 2);
    }

    @Test
    public void testTransferOutEvent() throws PWCGException 
    {
        List<TransferRecord> squadronMemberTransferredOut = new ArrayList<>();
        TransferRecord squadronMemberTransfer = new TransferRecord(pilot1, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
        squadronMemberTransferredOut.add(squadronMemberTransfer);        
        Mockito.when(squadronMembersTransferred.getSquadronMembersTransferred()).thenReturn(squadronMemberTransferredOut);

        List<TransferRecord> acesTransferredIn = new ArrayList<>();
        TransferRecord aceTransfer = new TransferRecord(ace1, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
        acesTransferredIn.add(aceTransfer);        
        Mockito.when(acesTransferred.getSquadronMembersTransferred()).thenReturn(acesTransferredIn);
        
        TransferPanelEventTabulator transferPanelEventTabulator = new TransferPanelEventTabulator(campaign, aarContext);
        TransferPanelData transferPanelData = transferPanelEventTabulator.tabulateForAARTransferPanel();
        
        List<TransferEvent> transferEvents = transferPanelData.getTransfers();

        assert (transferEvents.size() == 2);
    }
}
