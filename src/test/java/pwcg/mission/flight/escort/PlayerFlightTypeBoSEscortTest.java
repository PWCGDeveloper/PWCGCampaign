package pwcg.mission.flight.escort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.validate.PlayerEscortFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerFlightTypeBoSEscortTest
{
    private static Mission mission;
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void escortFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.ESCORT, MissionProfile.DAY_TACTICAL_MISSION);
        mission.finalizeMission();
        
        PlayerIsEscortFlight flight = (PlayerIsEscortFlight) mission.getFlights().getUnits().get(0);
        flight.finalizeFlight();

        PlayerEscortFlightValidator escortFlightValidator = new PlayerEscortFlightValidator(mission.getFlights());
        escortFlightValidator.validateEscortFlight();
        Assertions.assertTrue (flight.getFlightType() == FlightTypes.ESCORT);
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
    }
}
