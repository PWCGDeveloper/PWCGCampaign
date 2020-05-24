package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();
    
    public ScrambleWaypointFactory(IFlight flight)
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> waypoints = createTargetWaypoints();
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints() throws PWCGException  
	{
        List<McuWaypoint> waypoints = new ArrayList<>();

		McuWaypoint scrambleTargetWP = createTargetScrambleWaypoint();
		waypoints.add(scrambleTargetWP);

        return waypoints;
	}

    private McuWaypoint createTargetScrambleWaypoint() throws PWCGException
    {
        double angleToTarget = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getTargetDefinition().getPosition());
        Orientation wpOrientation = new Orientation();
        wpOrientation.setyOri(angleToTarget);
        
        Coordinate scrambleTargetCoords =  flight.getTargetDefinition().getPosition();
        scrambleTargetCoords.setYPos(flight.getFlightInformation().getAltitude());
         
        McuWaypoint scrambleTargetWP = WaypointFactory.createPatrolWaypointType();
		scrambleTargetWP.setPosition(scrambleTargetCoords);	
		scrambleTargetWP.setOrientation(wpOrientation.copy());
		scrambleTargetWP.setTargetWaypoint(true);
		scrambleTargetWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		scrambleTargetWP.setSpeed(flight.getFlightCruisingSpeed());
        return scrambleTargetWP;
    }
}
