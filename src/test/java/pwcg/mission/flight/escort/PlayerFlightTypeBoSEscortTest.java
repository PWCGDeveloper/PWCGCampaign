package pwcg.mission.flight.escort;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.validate.GroundUnitValidator;
import pwcg.mission.flight.validate.PlayerEscortFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

public class PlayerFlightTypeBoSEscortTest
{
    Mission mission;
    Campaign campaign;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void escortFlightTest() throws PWCGException
    {
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate(FlightTypes.ESCORT);
        mission.finalizeMission();
        
        PlayerEscortFlight flight = (PlayerEscortFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
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
