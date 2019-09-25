package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetCategory;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.escort.PlayerEscortFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.scramble.PlayerScrambleFlight;
import pwcg.mission.flight.validate.PatrolFlightValidator;
import pwcg.mission.flight.validate.PlayerEscortFlightValidator;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

public class PlayerFlightTypeBoSFighterTest 
{    
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
    }

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate(FlightTypes.PATROL);
        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.PATROL);
        EscortForPlayerValidator.validateNoEscortForPlayer(flight);
	}

	@Test
	public void interceptFlightTest() throws PWCGException
	{
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate(FlightTypes.INTERCEPT);
        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.INTERCEPT);
        EscortForPlayerValidator.validateNoEscortForPlayer(flight);
        
        validateAiFlightWaypoints(mission);
	}

	@Test
	public void offensiveFlightTest() throws PWCGException
	{
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate(FlightTypes.OFFENSIVE);
        OffensiveFlight flight = (OffensiveFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.OFFENSIVE);
        EscortForPlayerValidator.validateNoEscortForPlayer(flight);
	}

	@Test
	public void escortFlightTest() throws PWCGException
	{
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate(FlightTypes.ESCORT);
        PlayerEscortFlight flight = (PlayerEscortFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PlayerEscortFlightValidator escortFlightValidator = new PlayerEscortFlightValidator(flight);
		escortFlightValidator.validateEscortFlight();
        assert(flight.getFlightType() == FlightTypes.ESCORT);
        EscortForPlayerValidator.validateNoEscortForPlayer(flight);
	}

    @Test
    public void scrambleFlightTest() throws PWCGException
    {
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate(FlightTypes.SCRAMBLE);
        PlayerScrambleFlight flight = (PlayerScrambleFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();
        
        PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
        patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.SCRAMBLE);
        EscortForPlayerValidator.validateNoEscortForPlayer(flight);
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
	{
        assert (targetDefinition.getAttackingCountry() != null);
        assert (targetDefinition.getTargetCountry() != null);
        assert (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        assert (targetDefinition.getTargetType() != TacticalTarget.TARGET_NONE);
	}
    
    private void validateAiFlightWaypoints(Mission mission) throws PWCGException
    {
        Coordinate missionCenter = mission.getMissionBorders().getCenter();

        boolean failed = false;
        for (Flight aiFlight : mission.getMissionFlightBuilder().getAiFlights())
        {
            Coordinate leadPlaneStart = aiFlight.getLeadPlane().getPosition();
            double distanceLeadPlaneToCenter = MathUtils.calcDist(missionCenter, leadPlaneStart);
            if (distanceLeadPlaneToCenter > 75000)
            {
                failed = true;
            }
            
            for (McuWaypoint waypoint : aiFlight.getWaypointPackage().getWaypointsForLeadPlane())
            {
                if (!(waypoint.getName().contains("Landing")))
                {
                    Coordinate waypointPosition = waypoint.getPosition();
                    double distanceWaypointToCenter = MathUtils.calcDist(missionCenter, waypointPosition);
                    if (distanceWaypointToCenter > 50000)
                    {
                        failed = true;
                    }
                }

                double distanceMissioNCenterToTarget = MathUtils.calcDist(missionCenter, aiFlight.getTargetCoords());
                if (distanceMissioNCenterToTarget > 50000)
                {
                    failed = true;
                }

                if ((waypoint.getName().contains("Ingress")))
                {
                    Coordinate waypointPosition = waypoint.getPosition();
                    FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
                    Coordinate closestFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(aiFlight.getTargetCoords(), aiFlight.getSquadron().determineSide());
                    double distanceIngressToTarget = MathUtils.calcDist(closestFrontLinesToTarget, waypointPosition);
                    if (distanceIngressToTarget > 50000)
                    {
                        failed = true;
                    }

                    Coordinate closestFrontLinesToMissionCenter = frontLinesForMap.findClosestFrontCoordinateForSide(missionCenter, aiFlight.getSquadron().determineSide());
                    double distanceIngressToMissionCenter = MathUtils.calcDist(closestFrontLinesToMissionCenter, waypointPosition);
                    if (distanceIngressToMissionCenter > 30000)
                    {
                        failed = true;
                    }

                }
            }
        }
    }
}
