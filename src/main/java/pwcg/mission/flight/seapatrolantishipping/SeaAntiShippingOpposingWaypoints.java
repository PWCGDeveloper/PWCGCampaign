package pwcg.mission.flight.seapatrolantishipping;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.egress.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.initial.InitialWaypointGenerator;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingOpposingWaypoints
{
    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public SeaAntiShippingOpposingWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
        this.campaign = flight.getCampaign();
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        InitialWaypointGenerator climbWaypointGenerator = new InitialWaypointGenerator(flight);
        List<McuWaypoint> initialWPs = climbWaypointGenerator.createInitialFlightWaypoints();
        waypoints.addAll(initialWPs);

        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
        waypoints.addAll(targetWaypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, flight.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        return waypoints;
    }

	protected List<McuWaypoint> createTargetWaypoints() throws PWCGException  
	{		
        List<McuWaypoint> targetWaypoints = new ArrayList<>();

        McuWaypoint targetApproachWP = createTargetApproachWaypoint();
        targetWaypoints.add(targetApproachWP);    

        McuWaypoint targetWP = createTargetWaypoint();
		targetWaypoints.add(targetWP);
        return targetWaypoints;
	}

	protected McuWaypoint createTargetApproachWaypoint() throws PWCGException  
	{
		double angle = 80.0;
		double distance = 4000.0;
		Coordinate coord = MathUtils.calcNextCoord(flight.getTargetPosition(), angle, distance);
		coord.setYPos(flight.getFlightAltitude());
		
		McuWaypoint targetApproachWP = WaypointFactory.createPatrolWaypointType();
		targetApproachWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		targetApproachWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), WaypointType.PATROL_WAYPOINT.getName());
		targetApproachWP.setSpeed(flight.getFlightCruisingSpeed());
		targetApproachWP.setPosition(coord);
		targetApproachWP.setTargetWaypoint(false);
						
		return targetApproachWP;
	}

	private McuWaypoint createTargetWaypoint() throws PWCGException  
	{
 		Coordinate coord = flight.getTargetPosition().copy();
        coord.setYPos(flight.getFlightAltitude());

		McuWaypoint targetWP = WaypointFactory.createPatrolWaypointType();
		targetWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		targetWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()),WaypointType.PATROL_WAYPOINT.getName());
		targetWP.setSpeed(flight.getFlightCruisingSpeed());
		targetWP.setPosition(coord);	
		targetWP.setTargetWaypoint(true);

		return targetWP;
	}
}
