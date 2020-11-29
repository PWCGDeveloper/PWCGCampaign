package pwcg.mission;

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
import pwcg.testutils.TestMissionBuilderUtility;

@RunWith(MockitoJUnitRunner.class)
public class MissionGroundUnitBuilderTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void createBattle() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        
        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(Arrays.asList(FlightTypes.DIVE_BOMB));

        MissionBattleBuilder battleBuilder = new MissionBattleBuilder(campaign, mission);
        List<IGroundUnitCollection> battles = battleBuilder.generateBattles();

        assert (battles.size() < 3);
        for (IGroundUnitCollection battle : battles)
        {
            assert (battle.getGroundUnitsForSide(Side.ALLIED).size() > 0);
            assert (battle.getGroundUnitsForSide(Side.AXIS).size() > 0);
        }
    }

    @Test
    public void createTrucks() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(Arrays.asList(FlightTypes.DIVE_BOMB));

        MissionTruckConvoyBuilder truckConvoyBuilder = new MissionTruckConvoyBuilder(campaign, mission);
        List<IGroundUnitCollection> trucks = truckConvoyBuilder.generateMissionTrucks();

        assert (trucks.size() >= 2);
        assert (trucks.size() <= 12);

        boolean alliedTrainFound = false;
        boolean axisTrainFound = false;
        for (IGroundUnitCollection truckUnit : trucks)
        {
            for (IGroundUnit groundUnit : truckUnit.getGroundUnits())
            {
                if (groundUnit.getCountry().getSide() == Side.ALLIED)
                {
                    alliedTrainFound = true;
                }
                else
                {
                    axisTrainFound = true;
                }
                assert (groundUnit.getVehicles().size() >= 1);
            }
        }
        assert (alliedTrainFound);
        assert (axisTrainFound);
    }

    @Test
    public void createTrains() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(Arrays.asList(FlightTypes.DIVE_BOMB));

        MissionTrainBuilder trainBuilder = new MissionTrainBuilder(campaign, mission);
        List<IGroundUnitCollection> trains = trainBuilder.generateMissionTrains();

        assert (trains.size() >= 2);
        assert (trains.size() <= 8);

        boolean alliedTrainFound = false;
        boolean axisTrainFound = false;
        for (IGroundUnitCollection train : trains)
        {
            for (IGroundUnit groundUnit : train.getGroundUnits())
            {
                if (groundUnit.getCountry().getSide() == Side.ALLIED)
                {
                    alliedTrainFound = true;
                }
                else
                {
                    axisTrainFound = true;
                }
                assert (groundUnit.getVehicles().size() == 1);
            }
        }
        assert (alliedTrainFound);
        assert (axisTrainFound);
    }

    @Test
    public void createAAA() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(Arrays.asList(FlightTypes.DIVE_BOMB));

        AAAManager aaaManager = new AAAManager(campaign, mission);
        List<IGroundUnitCollection> AAA = aaaManager.getAAAForMission();

        assert (AAA.size() > 10);
        for (IGroundUnitCollection aaaUnit : AAA)
        {
            for (IGroundUnit groundUnit : aaaUnit.getGroundUnits())
            {
                assert (groundUnit.getVehicles().size() > 0);
            }
        }
    }
}
