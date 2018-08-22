package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.ww1.country.RoFCountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class VictorySorterTest
{
    @Mock private Campaign campaign;
    @Mock private SquadronMember player;
    @Mock private CampaignPersonnelManager personnelManager;

    private List<LogVictory> logVictories = new ArrayList<>();
    private List<SquadronMember> players = new ArrayList<>();

    @Before
    public void setup() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(campaign.determineCountry()).thenReturn(new RoFCountry(Country.FRANCE));
        Mockito.when(campaign.getPlayers()).thenReturn(players);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAnyCampaignMember(Mockito.any())).thenReturn(player);
        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        createPlaneVictory();
        createPlaneVictory();
        createPlaneVictory();
        createBalloonVictory();
        createGroundVictory();
        createGroundVictory();
        createFuzzyPlaneVictory();
        createFuzzyPlaneVictory();
        createFuzzyBalloonVictory();
    }
    
    @Test
    public void testVictorySorting () throws PWCGException
    {
        VictorySorter victorySorter = new VictorySorter(campaign);
        victorySorter.sortVictories(logVictories);
        
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


    private void createPlaneVictory()
    {
        LogPlane victor = makeVictor();
        
        LogPlane victim = new LogPlane(1);
        victim.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        victim.setVehicleType("albatrosd3");
        victim.setCountry(new RoFCountry(Country.GERMANY));
        victim.setSquadronId(501011);
        victim.intializePilot(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);

        makeVictory(victor, victim);
    }

    private void createFuzzyPlaneVictory()
    {
        LogUnknown victor = new LogUnknown();
        
        LogPlane victim = new LogPlane(1);
        victim.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        victim.setVehicleType("albatrosd3");
        victim.setCountry(new RoFCountry(Country.GERMANY));
        victim.setSquadronId(501011);
        victim.intializePilot(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);

        makeVictory(victor, victim);
    }

    private void createBalloonVictory()
    {
        LogPlane victor = makeVictor();
        
        LogBalloon victim = new LogBalloon(10000);
        victim.setVehicleType("drachen");
        victim.setCountry(new RoFCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    private void createFuzzyBalloonVictory()
    {
        LogUnknown victor = new LogUnknown();
        
        LogBalloon victim = new LogBalloon(10000);
        victim.setVehicleType("drachen");
        victim.setCountry(new RoFCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    private void createGroundVictory()
    {
        LogPlane victor = makeVictor();
        
        LogGroundUnit victim = new LogGroundUnit(1000);
        victim.setVehicleType("tank");
        victim.setCountry(new RoFCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    private LogPlane makeVictor()
    {
        LogPlane victor = new LogPlane(1);
        victor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setVehicleType("spad7");
        victor.setCountry(new RoFCountry(Country.FRANCE));
        victor.setSquadronId(101103);
        victor.intializePilot(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        return victor;
    }

    private void makeVictory(LogAIEntity victor, LogAIEntity victim)
    {
        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setLocation(new Coordinate(100.0, 0.0, 100.0));
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        logVictories.add(resultVictory);
    }
}
