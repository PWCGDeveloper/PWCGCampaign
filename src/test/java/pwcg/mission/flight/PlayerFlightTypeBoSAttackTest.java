package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.flight.attack.GroundAttackFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.validate.GroundAttackFlightValidator;
import pwcg.mission.flight.validate.GroundUnitValidator;
import pwcg.mission.flight.validate.PositionEvaluator;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

public class PlayerFlightTypeBoSAttackTest
{
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_503_PROFILE);
    }

    @Test
    public void groundAttackFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK);
        GroundAttackFlight flight = (GroundAttackFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert (flight.getFlightType() == FlightTypes.GROUND_ATTACK);
        for (PlaneMCU plane : flight.getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() > 0);
        }
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        EscortForPlayerValidator.validateEscortForPlayer(mission, flight);
        PositionEvaluator.evaluateAiFlight(mission);
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
    {
        assert (targetDefinition.getAttackingCountry() != null);
        assert (targetDefinition.getTargetCountry() != null);
        assert (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        assert (targetDefinition.getTargetType() != TacticalTarget.TARGET_NONE);
    }
}
