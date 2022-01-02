package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class VictorySorterTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private CrewMembers playerMembers;
    @Mock private CrewMember player;

    private List<CrewMember> players = new ArrayList<>();
    private LogVictoryHelper logVictoryHelper = new LogVictoryHelper();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);   
                
        Mockito.when(personnelManager.getAnyCampaignMember(Mockito.any())).thenReturn(player);
        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        logVictoryHelper = new LogVictoryHelper();
        logVictoryHelper.createPlaneVictory();
        logVictoryHelper.createPlaneVictory();
        logVictoryHelper.createPlaneVictory();
        logVictoryHelper.createBalloonVictory();
        logVictoryHelper.createGroundVictory();
        logVictoryHelper.createGroundVictory();
        logVictoryHelper.createFuzzyPlaneVictory();
        logVictoryHelper.createFuzzyPlaneVictory();
        logVictoryHelper.createFuzzyBalloonVictory();
    }
    
    @Test
    public void testVictorySorting () throws PWCGException
    {
        VictorySorter victorySorter = new VictorySorter();
        victorySorter.sortVictories(logVictoryHelper.getLogVictories());
        
        assert(victorySorter.getFirmBalloonVictories().size() == 1);
        assert(victorySorter.getFirmAirVictories().size() == 3);
        assert(victorySorter.getFirmGroundVictories().size() == 2);
        assert(victorySorter.getFuzzyAirVictories().size() == 2);
        assert(victorySorter.getFuzzyBalloonVictories().size() == 1);
        
        testVictoryReassignment(victorySorter);
    }    
    
    public void testVictoryReassignment (VictorySorter victorySorter) throws PWCGException
    {
        PlayerVictoryReassigner playerVictoryReassigner = new PlayerVictoryReassigner(campaign);
        playerVictoryReassigner.resetUnclamedPlayerVictoriesForAssignmentToOthers(victorySorter);
        
        for (LogVictory victory : victorySorter.getFirmAirVictories())
        {
            assert(victory.getVictor() instanceof LogUnknown);
        }
    }
}
