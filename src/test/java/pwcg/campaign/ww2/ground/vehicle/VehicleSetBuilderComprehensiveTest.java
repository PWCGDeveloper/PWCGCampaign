package pwcg.campaign.ww2.ground.vehicle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

@RunWith(MockitoJUnitRunner.class)
public class VehicleSetBuilderComprehensiveTest
{
    Mission mission;
    Campaign campaign;

    @Before 
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19411103"));
    }

    @Test
    public void testVehicleCreation() throws PWCGException
    {
        VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();
        vehicleSetBuilder.makeOneOfEachType();
        
        for (IVehicle vehicle : vehicleSetBuilder.getAllVehicles())
        {
            assert (vehicle.getAllVehicleDefinitions().size() > 0);
        }
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
    	MissionGenerator missionGenerator = new MissionGenerator(campaign);
    	mission = missionGenerator.makeMissionFromFlightType(participatingPlayers, FlightTypes.PATROL);
        mission.generateAllGroundUnitTypesForTest();
        mission.finalizeMission();
    }

}
