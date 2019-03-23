package pwcg.mission
;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.AAAManager;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AmbientBuilderTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
    }

    @Test
    public void createAmbientBattle () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.STG77_PROFILE);
        Mission mission = new Mission();
        mission.initialize(campaign);
        mission.generate(CampaignCache.buildParticipatingPlayers(SquadrontTestProfile.STG77_PROFILE), FlightTypes.DIVE_BOMB);
        
        AmbientBattleBuilder ambientBattleBuilder = new AmbientBattleBuilder(campaign, mission);
        List<AssaultInformation> battles = ambientBattleBuilder.generateAmbientBattles();
        
        assert (battles.size() < 3);
        for (AssaultInformation battle : battles)
        {
            assert(battle.getAggressor().getSide() != battle.getDefender().getSide());
            assert(battle.getGroundUnitCollection().getAllAlliedGroundUnits().size() > 0);
            assert(battle.getGroundUnitCollection().getAllAxisGroundUnits().size() > 0);
        }
    }

    @Test
    public void createAmbientTrucks () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.STG77_PROFILE);
        Mission mission = new Mission();
        mission.initialize(campaign);
        mission.generate(CampaignCache.buildParticipatingPlayers(SquadrontTestProfile.STG77_PROFILE), FlightTypes.DIVE_BOMB);
        
        AmbientTruckConvoyBuilder ambientTruckConvoyBuilder = new AmbientTruckConvoyBuilder(campaign, mission);
        List<GroundTruckConvoyUnit> ambientTrucks = ambientTruckConvoyBuilder.generateAmbientTrucks();

        assert (ambientTrucks.size() < 6);
        for (GroundTruckConvoyUnit ambientTruck : ambientTrucks)
        {
            assert(ambientTruck.getCountry().getSide() == Side.ALLIED);
            assert(ambientTruck.getSpawners().size() > 1);
        }
    }

    @Test
    public void createAmbientTrains () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.STG77_PROFILE);
        Mission mission = new Mission();
        mission.initialize(campaign);
        mission.generate(CampaignCache.buildParticipatingPlayers(SquadrontTestProfile.STG77_PROFILE), FlightTypes.DIVE_BOMB);
        
        AmbientTrainBuilder ambientTrainBuilder = new AmbientTrainBuilder(campaign, mission);
        List<GroundTrainUnit> ambientTrains = ambientTrainBuilder.generateAmbientTrains();

        assert (ambientTrains.size() < 3);
        for (GroundTrainUnit ambientTrain : ambientTrains)
        {
            assert(ambientTrain.getCountry().getSide() == Side.ALLIED);
            assert(ambientTrain.getSpawners().size() == 1);
        }
    }

    @Test
    public void createAmbientAAA () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.STG77_PROFILE);
        Mission mission = new Mission();
        mission.initialize(campaign);
        mission.generate(CampaignCache.buildParticipatingPlayers(SquadrontTestProfile.STG77_PROFILE), FlightTypes.DIVE_BOMB);
        
        AAAManager aaaManager = new AAAManager(campaign, mission);
        List<GroundUnitSpawning> AAA = aaaManager.getAAAForMission();

        assert (AAA.size() > 10);
        for (GroundUnitSpawning aaaUnit : AAA)
        {
            assert(aaaUnit.getSpawners().size() > 0);
        }
    }
}
