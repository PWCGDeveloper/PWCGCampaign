package pwcg.mission
;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.AAAManager;
import pwcg.mission.ground.MissionBattleBuilder;
import pwcg.mission.ground.MissionTrainBuilder;
import pwcg.mission.ground.MissionTruckConvoyBuilder;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

@RunWith(MockitoJUnitRunner.class)
public class MissionGroundUnitBuilderTest
{    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void createBattle () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, MissionProfile.DAY_TACTICAL_MISSION, participatingPlayers, missionBorders);
        mission.generate(Arrays.asList(FlightTypes.DIVE_BOMB));
        
        MissionBattleBuilder battleBuilder = new MissionBattleBuilder(campaign, mission);
        List<IGroundUnitCollection> battles = battleBuilder.generateBattles();
        
        assert (battles.size() < 3);
        for (IGroundUnitCollection battle : battles)
        {
            assert(battle.getGroundUnitsForSide(Side.ALLIED).size() > 0);
            assert(battle.getGroundUnitsForSide(Side.AXIS).size() > 0);
        }
    }

    @Test
    public void createTrucks () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, MissionProfile.DAY_TACTICAL_MISSION, participatingPlayers, missionBorders);
        mission.generate(Arrays.asList(FlightTypes.DIVE_BOMB));
        
        MissionTruckConvoyBuilder truckConvoyBuilder = new MissionTruckConvoyBuilder(campaign, mission);
        List<IGroundUnitCollection> trucks = truckConvoyBuilder.generateMissionTrucks();

        assert (trucks.size() < 6);
        for (IGroundUnitCollection truckUnit : trucks)
        {
            for (IGroundUnit groundUnit : truckUnit.getGroundUnits())
            {
                assert(groundUnit.getCountry().getSide() == Side.ALLIED);
                assert(groundUnit.getVehicles().size() > 1);
            }
        }
    }

    @Test
    public void createTrains () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, MissionProfile.DAY_TACTICAL_MISSION, participatingPlayers, missionBorders);
        mission.generate(Arrays.asList(FlightTypes.DIVE_BOMB));
        
        MissionTrainBuilder trainBuilder = new MissionTrainBuilder(campaign, mission);
        List<IGroundUnitCollection> trains = trainBuilder.generateMissionTrains();

        assert (trains.size() <= 4);
        for (IGroundUnitCollection train : trains)
        {
            for (IGroundUnit groundUnit : train.getGroundUnits())
            {
                assert(groundUnit.getCountry().getSide() == Side.ALLIED);
                assert(groundUnit.getVehicles().size() == 1);
            }
        }
    }

    @Test
    public void createAAA () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, MissionProfile.DAY_TACTICAL_MISSION, participatingPlayers, missionBorders);
        mission.generate(Arrays.asList(FlightTypes.DIVE_BOMB));
        
        AAAManager aaaManager = new AAAManager(campaign, mission);
        List<IGroundUnitCollection> AAA = aaaManager.getAAAForMission();

        assert (AAA.size() > 10);
        for (IGroundUnitCollection aaaUnit : AAA)
        {
            for (IGroundUnit groundUnit : aaaUnit.getGroundUnits())
            {
                assert(groundUnit.getVehicles().size() > 0);
            }
        }
    }
}
