package pwcg.product.bos.ground.vehicle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleSetBuilderComprehensiveTest
{
    private Campaign campaign;    

    @BeforeAll 
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19411103"));
    }

    @Test
    public void testVehicleCreation() throws PWCGException
    {
        VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();
        vehicleSetBuilder.makeOneOfEachType();
        assert (vehicleSetBuilder.getAllVehicles().size() > 0);
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
    	MissionGenerator missionGenerator = new MissionGenerator(campaign);
    	Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(participatingPlayers, FlightTypes.PATROL, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generateAllGroundUnitTypesForTest();
        mission.finalizeMission();
    }

}
