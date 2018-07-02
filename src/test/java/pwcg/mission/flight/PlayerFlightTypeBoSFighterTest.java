package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.escort.PlayerEscortFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

public class PlayerFlightTypeBoSFighterTest
{
    Mission mission;
    Campaign campaign;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
    }

    public void patrolFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.PATROL);
        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.PATROL);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
    }

    @Test
    public void lowAltPatrolFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.LOW_ALT_PATROL);
        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_PATROL);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
    }

    @Test
    public void lowAltCapFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.LOW_ALT_CAP);
        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_CAP);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
    }

    @Test
    public void interceptFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.INTERCEPT);
        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.INTERCEPT);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0 || plane.getPlanePayload().getSelectedPayloadId() == 4);
        }
    }

    @Test
    public void offensiveFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.OFFENSIVE);
        OffensiveFlight flight = (OffensiveFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert (flight.getFlightType() == FlightTypes.OFFENSIVE);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
    }

    @Test
    public void escortFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.ESCORT);
        PlayerEscortFlight flight = (PlayerEscortFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        PlayerEscortFlightValidator escortFlightValidator = new PlayerEscortFlightValidator();
        escortFlightValidator.validateEscortFlight(flight);
        assert (flight.getFlightType() == FlightTypes.ESCORT);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() == 0);
        }
    }
}
