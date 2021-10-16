package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.tabulate.debrief.AceLeavePanelEventTabulator;
import pwcg.aar.ui.display.model.AARAceLeavePanelData;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARAceLeavePanelEventTabulatorTest extends AARTestSetup
{
    
    @Mock
    private Ace ace;

    @Mock
    private TransferRecord historicalAceTransferData;

    private List<TransferRecord> transferRecords = new ArrayList<>();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();
        
        Mockito.when(ace.determineSquadron()).thenReturn(squadronEsc103);

        Mockito.when(acesTransferred.getSquadronMembersTransferred()).thenReturn(transferRecords);
        
        transferRecords.clear();
    }
    
    @Test
    public void oneAceOnLeave () throws PWCGException
    {             
        transferRecords.add(new TransferRecord(ace, SquadronTestProfile.JASTA_11_PROFILE.getSquadronId(), SquadronMemberStatus.STATUS_ON_LEAVE));

        AceLeavePanelEventTabulator aceLeavePanelEventTabulator = new AceLeavePanelEventTabulator(campaign, aarContext);
        AARAceLeavePanelData aceLeavePanelData = aceLeavePanelEventTabulator.tabulateForAARAceLeavePanel();
        Assertions.assertTrue (aceLeavePanelData.getAcesOnLeaveDuringElapsedTime().size() == 1);
    }
    
    @Test
    public void noAcesOnLeave () throws PWCGException
    {             
        transferRecords.add(new TransferRecord(ace, SquadronTestProfile.JASTA_11_PROFILE.getSquadronId(), SquadronTestProfile.JASTA_16_PROFILE.getSquadronId()));

        AceLeavePanelEventTabulator aceLeavePanelEventTabulator = new AceLeavePanelEventTabulator(campaign, aarContext);
        AARAceLeavePanelData aceLeavePanelData = aceLeavePanelEventTabulator.tabulateForAARAceLeavePanel();
        Assertions.assertTrue (aceLeavePanelData.getAcesOnLeaveDuringElapsedTime().size() == 0);
    }
}
