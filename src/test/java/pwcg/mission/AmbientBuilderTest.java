package pwcg.mission
;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.ambient.AmbientBattleBuilder;
import pwcg.mission.ambient.AmbientTrainBuilder;
import pwcg.mission.ambient.AmbientTruckConvoyBuilder;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.AAAManager;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

@RunWith(MockitoJUnitRunner.class)
public class AmbientBuilderTest
{    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void createAmbientBattle () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, participatingPlayers, missionBorders);
        mission.generate(FlightTypes.ANY);
        
        AmbientBattleBuilder ambientBattleBuilder = new AmbientBattleBuilder(campaign, mission);
        List<IGroundUnitCollection> battles = ambientBattleBuilder.generateAmbientBattles();
        
        assert (battles.size() < 3);
        for (IGroundUnitCollection battle : battles)
        {
            assert(battle.getGroundUnitsForSide(Side.ALLIED).size() > 0);
            assert(battle.getGroundUnitsForSide(Side.AXIS).size() > 0);
        }
    }

    @Test
    public void createAmbientTrucks () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, participatingPlayers, missionBorders);
        mission.generate(FlightTypes.ANY);
        
        AmbientTruckConvoyBuilder ambientTruckConvoyBuilder = new AmbientTruckConvoyBuilder(campaign, mission);
        List<IGroundUnitCollection> ambientTrucks = ambientTruckConvoyBuilder.generateAmbientTrucks();

        assert (ambientTrucks.size() < 6);
        for (IGroundUnitCollection ambientTruckUnit : ambientTrucks)
        {
            for (IGroundUnit groundUnit : ambientTruckUnit.getGroundUnits())
            {
                assert(groundUnit.getCountry().getSide() == Side.ALLIED);
                assert(groundUnit.getSpawners().size() > 1);
            }
        }
    }

    @Test
    public void createAmbientTrains () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, participatingPlayers, missionBorders);
        mission.generate(FlightTypes.ANY);
        
        AmbientTrainBuilder ambientTrainBuilder = new AmbientTrainBuilder(campaign, mission);
        List<IGroundUnitCollection> ambientTrains = ambientTrainBuilder.generateAmbientTrains();

        assert (ambientTrains.size() < 3);
        for (IGroundUnitCollection ambientTrain : ambientTrains)
        {
            for (IGroundUnit groundUnit : ambientTrain.getGroundUnits())
            {
                assert(groundUnit.getCountry().getSide() == Side.ALLIED);
                assert(groundUnit.getSpawners().size() == 1);
            }
        }
    }

    @Test
    public void createAmbientAAA () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, participatingPlayers, missionBorders);
        mission.generate(FlightTypes.ANY);
        
        AAAManager aaaManager = new AAAManager(campaign, mission);
        List<IGroundUnitCollection> AAA = aaaManager.getAAAForMission();

        assert (AAA.size() > 10);
        for (IGroundUnitCollection aaaUnit : AAA)
        {
            for (IGroundUnit groundUnit : aaaUnit.getGroundUnits())
            {
                assert(groundUnit.getSpawners().size() > 0);
            }
        }
    }
}
