package pwcg.mission.flight;

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
import pwcg.mission.flight.groundattack.GroundAttackFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.validate.EscortForPlayerValidator;
import pwcg.mission.flight.validate.GroundAttackFlightValidator;
import pwcg.mission.flight.validate.PlaneRtbValidator;
import pwcg.mission.flight.validate.PositionEvaluator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.group.virtual.VirtualWaypointPackageValidator;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerFlightTypeBoSAttackTest
{
    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_503_PROFILE);
    }

    @Test
    public void groundAttackFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
        GroundAttackFlight flight = (GroundAttackFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert(flight.getFlightType() == FlightTypes.GROUND_ATTACK);

        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            assert(plane.getPlanePayload().getSelectedPayloadId() > 0);
        }
        
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getFlights());
        playerEscortedFlightValidator.validateEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
    {
        Assertions.assertTrue (targetDefinition.getCountry() != null);
        Assertions.assertTrue (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        Assertions.assertTrue (targetDefinition.getTargetType() != TargetType.TARGET_NONE);
    }
}
