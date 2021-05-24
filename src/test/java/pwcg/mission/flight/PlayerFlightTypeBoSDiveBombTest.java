package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.divebomb.DiveBombingFlight;
import pwcg.mission.flight.validate.EscortForPlayerValidator;
import pwcg.mission.flight.validate.FlightActivateValidator;
import pwcg.mission.flight.validate.GroundAttackFlightValidator;
import pwcg.mission.flight.validate.PositionEvaluator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.ground.GroundUnitPositionVerifier;
import pwcg.mission.mcu.group.virtual.VirtualWaypointPackageValidator;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class PlayerFlightTypeBoSDiveBombTest
{
    private Campaign campaign;
    private GroundUnitPositionVerifier groundUnitPositionVerifier = new GroundUnitPositionVerifier();

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);
    }

    @Test
    public void diveBombFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.DIVE_BOMB, MissionProfile.DAY_TACTICAL_MISSION);
        DiveBombingFlight flight = (DiveBombingFlight) mission.getMissionFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        assert (targetMissionPoint != null);

        FlightActivateValidator.validate(flight);

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert (flight.getFlightType() == FlightTypes.DIVE_BOMB);
        
        PositionEvaluator.evaluateAiFlight(mission);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getMissionFlights());
        playerEscortedFlightValidator.validateEscortForPlayer();
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();
        
        groundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
    {
        assert (targetDefinition.getCountry() != null);
        assert (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        assert (targetDefinition.getTargetType() != TargetType.TARGET_NONE);
    }
}
