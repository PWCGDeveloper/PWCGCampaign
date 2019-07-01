package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class VictorySorterTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private SquadronMembers playerMembers;
    @Mock private SquadronMember player;

    private List<SquadronMember> players = new ArrayList<>();
    private LogVictoryHelper logVictoryHelper = new LogVictoryHelper();

    @Before
    public void setup() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);   
        Mockito.when(personnelManager.getAllPlayers()).thenReturn(playerMembers);   
        Mockito.when(playerMembers.getSquadronMemberList()).thenReturn(players);   

        Mockito.when(player.getCountry()).thenReturn(Country.FRANCE);        
                
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
