package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetCategory;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.bomb.StrategicBombingFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.validate.GroundAttackFlightValidator;
import pwcg.mission.flight.validate.GroundUnitValidator;
import pwcg.mission.flight.validate.PositionEvaluator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

public class PlayerFlightTypeBoSBombTest
{
    Campaign campaign;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.KG53_PROFILE);
    }

    @Test
    public void bombFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.BOMB);
        BombingFlight flight = (BombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert (flight.getFlightType() == FlightTypes.BOMB);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() >= 0);
        }
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        EscortForPlayerValidator.validateEscortForPlayer(flight);
        PositionEvaluator.evaluateAiFlight(mission);
    }

    @Test
    public void lowAltBombFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.LOW_ALT_BOMB);
        BombingFlight flight = (BombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert (flight.getFlightType() == FlightTypes.LOW_ALT_BOMB);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() >= 0);
        }
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        EscortForPlayerValidator.validateEscortForPlayer(flight);
        PositionEvaluator.evaluateAiFlight(mission);
    }

    @Test
    public void strategicBombFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.STRATEGIC_BOMB);
        StrategicBombingFlight flight = (StrategicBombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert (flight.getFlightType() == FlightTypes.STRATEGIC_BOMB);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() >= 0);
        }
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
    {
        assert (targetDefinition.getAttackingCountry() != null);
        assert (targetDefinition.getTargetCountry() != null);
        assert (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        assert (targetDefinition.getTargetType() != TacticalTarget.TARGET_NONE);
    }
}
