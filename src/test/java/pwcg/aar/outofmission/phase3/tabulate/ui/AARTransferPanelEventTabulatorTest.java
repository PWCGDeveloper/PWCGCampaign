package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.tabulate.debrief.TransferPanelEventTabulator;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARTransferPanelEventTabulatorTest extends AARTestSetup
{
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();
    }

    @Test
    public void testTransferInEvent() throws PWCGException 
    {
        List<TransferRecord> squadronMemberTransferredIn = new ArrayList<>();
        TransferRecord squadronMemberTransfer = new TransferRecord(crewMember1, SquadronTestProfile.ESC_3_PROFILE.getCompanyId(), SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        squadronMemberTransferredIn.add(squadronMemberTransfer);        
        Mockito.when(squadronMembersTransferred.getCrewMembersTransferred()).thenReturn(squadronMemberTransferredIn);

        List<TransferRecord> acesTransferredIn = new ArrayList<>();
        TransferRecord aceTransfer = new TransferRecord(ace1, SquadronTestProfile.ESC_3_PROFILE.getCompanyId(), SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        acesTransferredIn.add(aceTransfer);        
        Mockito.when(acesTransferred.getCrewMembersTransferred()).thenReturn(acesTransferredIn);
        
        TransferPanelEventTabulator transferPanelEventTabulator = new TransferPanelEventTabulator(campaign, aarContext);
        TransferPanelData transferPanelData = transferPanelEventTabulator.tabulateForAARTransferPanel();
        
        List<TransferEvent> transferEvents = transferPanelData.getTransfers();

        Assertions.assertTrue (transferEvents.size() == 2);
    }

    @Test
    public void testTransferOutEvent() throws PWCGException 
    {
        List<TransferRecord> squadronMemberTransferredOut = new ArrayList<>();
        TransferRecord squadronMemberTransfer = new TransferRecord(crewMember1, SquadronTestProfile.ESC_103_PROFILE.getCompanyId(), SquadronTestProfile.ESC_3_PROFILE.getCompanyId());
        squadronMemberTransferredOut.add(squadronMemberTransfer);        
        Mockito.when(squadronMembersTransferred.getCrewMembersTransferred()).thenReturn(squadronMemberTransferredOut);

        List<TransferRecord> acesTransferredIn = new ArrayList<>();
        TransferRecord aceTransfer = new TransferRecord(ace1, SquadronTestProfile.ESC_103_PROFILE.getCompanyId(), SquadronTestProfile.ESC_3_PROFILE.getCompanyId());
        acesTransferredIn.add(aceTransfer);        
        Mockito.when(acesTransferred.getCrewMembersTransferred()).thenReturn(acesTransferredIn);
        
        TransferPanelEventTabulator transferPanelEventTabulator = new TransferPanelEventTabulator(campaign, aarContext);
        TransferPanelData transferPanelData = transferPanelEventTabulator.tabulateForAARTransferPanel();
        
        List<TransferEvent> transferEvents = transferPanelData.getTransfers();

        Assertions.assertTrue (transferEvents.size() == 2);
    }
}
