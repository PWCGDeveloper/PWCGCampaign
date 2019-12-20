package pwcg.mission.flight.seapatrolscout;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.egress.EgressWaypointGenerator;
import pwcg.mission.mcu.McuWaypoint;

public class SeaPlaneOpposingWaypoints
{
    private Flight flight;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public SeaPlaneOpposingWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        McuWaypoint ingressWaypoint = createIngressWaypoint();
        waypoints.add(ingressWaypoint);
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
        waypoints.addAll(targetWaypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        return waypoints;
    }

    private McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Double angleToTarget = MathUtils.calcAngle(flight.getPosition(), flight.getTargetPosition());
        Orientation orientation = new Orientation();
        orientation.setyOri(angleToTarget);

        double angleFromTarget = MathUtils.adjustAngle(angleToTarget, 180);
        Coordinate scrambleOpposeIngressPosition =  MathUtils.calcNextCoord(flight.getTargetPosition(), angleFromTarget, 20000.0);
        scrambleOpposeIngressPosition.setYPos(flight.getFlightAltitude());

        McuWaypoint scrambleOpposeIngressWP = WaypointFactory.createPatrolWaypointType();
        scrambleOpposeIngressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        scrambleOpposeIngressWP.setSpeed(flight.getFlightCruisingSpeed());
        scrambleOpposeIngressWP.setPosition(scrambleOpposeIngressPosition);    
        scrambleOpposeIngressWP.setTargetWaypoint(false);
        scrambleOpposeIngressWP.setOrientation(orientation);
        return scrambleOpposeIngressWP;
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
		targetApproachWP.setDesc(flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()), WaypointType.PATROL_WAYPOINT.getName());
		targetApproachWP.setSpeed(flight.getFlightCruisingSpeed());
		targetApproachWP.setTargetWaypoint(false);
		targetApproachWP.setPosition(coord);
						
		return targetApproachWP;
	}
	
	private McuWaypoint createTargetWaypoint() throws PWCGException  
	{
 		Coordinate coord = flight.getTargetPosition().copy();
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint targetWP = WaypointFactory.createPatrolWaypointType();
		targetWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		targetWP.setDesc(flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()),WaypointType.PATROL_WAYPOINT.getName());
		targetWP.setSpeed(flight.getFlightCruisingSpeed());
		targetWP.setPosition(coord);	
		targetWP.setTargetWaypoint(true);
		
        return targetWP;
	}	
}
