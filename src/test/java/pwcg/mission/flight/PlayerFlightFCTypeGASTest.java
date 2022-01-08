package pwcg.mission.flight;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.balloonBust.BalloonBustFlight;
import pwcg.mission.flight.balloondefense.BalloonDefenseFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.validate.PatrolFlightValidator;
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

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerFlightFCTypeGASTest 
{    
    private Campaign campaign;    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
    }

    @Test
    public void balloonBustFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.BALLOON_BUST, MissionProfile.DAY_TACTICAL_MISSION);
        BalloonBustFlight flight = (BalloonBustFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbDisabled(mission);

        assert(flight.getFlightType() == FlightTypes.BALLOON_BUST);
        
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();
    }

    @Test
    public void balloonDefenseFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.BALLOON_DEFENSE, MissionProfile.DAY_TACTICAL_MISSION);
        BalloonDefenseFlight flight = (BalloonDefenseFlight) mission.getFlights().getUnits().get(0);
        mission.finalizeMission();
        MissionPoint targetMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (targetMissionPoint != null);
        PlaneRtbValidator.verifyPlaneRtbDisabled(mission);

        assert(flight.getFlightType() == FlightTypes.BALLOON_DEFENSE);
        
        PositionEvaluator.evaluateAiFlight(mission);
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();
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
        PlaneRtbValidator.verifyPlaneRtbDisabled(mission);

		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.INTERCEPT);
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
