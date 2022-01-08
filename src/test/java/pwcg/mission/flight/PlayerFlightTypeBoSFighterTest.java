package pwcg.mission.flight;

import java.util.List;

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
import pwcg.mission.flight.escort.PlayerIsEscortFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.scramble.PlayerScrambleFlight;
import pwcg.mission.flight.strategicintercept.StrategicInterceptFlight;
import pwcg.mission.flight.validate.EscortForPlayerValidator;
import pwcg.mission.flight.validate.FlightActivateValidator;
import pwcg.mission.flight.validate.PatrolFlightValidator;
import pwcg.mission.flight.validate.PlaneRtbValidator;
import pwcg.mission.flight.validate.PlayerEscortFlightValidator;
import pwcg.mission.flight.validate.PositionEvaluator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.group.virtual.VirtualWaypointPackageValidator;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.GroundUnitPositionVerifier;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerFlightTypeBoSFighterTest 
{    
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.PATROL, MissionProfile.DAY_TACTICAL_MISSION);
        PatrolFlight flight = (PatrolFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.PATROL);
        FlightActivateValidator.validate(flight);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getFlights().getAiFlights().size() >= 3);
        
        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
	}

    @Test
    public void interceptFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.INTERCEPT, MissionProfile.DAY_TACTICAL_MISSION);
        InterceptFlight flight = (InterceptFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.INTERCEPT);
        FlightActivateValidator.validate(flight);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getFlights().getAiFlights().size() >= 3);

        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
    }

    @Test
    public void strategicInterceptFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.STRATEGIC_INTERCEPT, MissionProfile.DAY_TACTICAL_MISSION);
        StrategicInterceptFlight flight = (StrategicInterceptFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT);
        FlightActivateValidator.validate(flight);
        
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();
        
        assert(mission.getFlights().getAiFlights().size() == 1);

        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
    }

	@Test
	public void offensiveFlightTest() throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.OFFENSIVE, MissionProfile.DAY_TACTICAL_MISSION);
        OffensiveFlight flight = (OffensiveFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.OFFENSIVE);
        
        FlightActivateValidator.validate(flight);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getFlights().getAiFlights().size() >= 3);

        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
	}

	@Test
	public void escortFlightTest() throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.ESCORT, MissionProfile.DAY_TACTICAL_MISSION);
        PlayerIsEscortFlight flight = (PlayerIsEscortFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

		PlayerEscortFlightValidator escortFlightValidator = new PlayerEscortFlightValidator(mission.getFlights());
		escortFlightValidator.validateEscortFlight();
        assert(flight.getFlightType() == FlightTypes.ESCORT);
        FlightActivateValidator.validate(flight);
        PositionEvaluator.evaluateAiFlight(mission);

        List<IFlight> playerEscortedFlight = mission.getFlights().getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORTED);
        assert(playerEscortedFlight != null);        
        assert(flight.getAssociatedFlight() != null);        

        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getFlights().getAiFlights().size() >= 3);

        GroundUnitPositionVerifier.verifyGroundUnitPositionsAndAssert(mission);
	}

    @Test
    public void scrambleFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.SCRAMBLE, MissionProfile.DAY_TACTICAL_MISSION);
        PlayerScrambleFlight flight = (PlayerScrambleFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbEnabled(mission);

        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.SCRAMBLE);        
        FlightActivateValidator.validate(flight);
        EscortForPlayerValidator playerEscortedFlightValidator = new EscortForPlayerValidator(mission.getFlights());
        playerEscortedFlightValidator.validateNoEscortForPlayer();
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();

        assert(mission.getFlights().getAiFlights().size() >= 3);
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
	{
        Assertions.assertTrue (targetDefinition.getCountry() != null);
        Assertions.assertTrue (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        Assertions.assertTrue (targetDefinition.getTargetType() != TargetType.TARGET_NONE);
	}
}
