package pwcg.mission.flight.escort;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.escort.PlayerEscortFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.validate.GroundUnitValidator;
import pwcg.mission.flight.validate.PlayerEscortFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

public class PlayerFlightTypeBoSEscortTest
{
    Mission mission;
    Campaign campaign;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
    }

    @Test
    public void escortFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.ESCORT);
        PlayerEscortFlight flight = (PlayerEscortFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        PlayerEscortFlightValidator escortFlightValidator = new PlayerEscortFlightValidator(flight);
        escortFlightValidator.validateEscortFlight();
        assert (flight.getFlightType() == FlightTypes.ESCORT);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);        
    }
}
