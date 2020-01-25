package pwcg.mission.flight.spy;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class SpyExtractWaypointWaypoints
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public SpyExtractWaypointWaypoints(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        McuWaypoint waypoint = createTargetWaypoint(ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(waypoint);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

	protected McuWaypoint createTargetWaypoint(Coordinate startPosition) throws PWCGException  
	{
	    Coordinate pickupLocation = getSpyExtractLocation(startPosition);
	    McuWaypoint approachWP = createSpyExtractWaypoint(pickupLocation);
        return approachWP;		
	}

    private Coordinate getSpyExtractLocation(Coordinate startPosition) throws PWCGException
    {
		Coordinate pickupLocation = flight.getFlightInformation().getTargetPosition().copy();
        pickupLocation.setYPos(flight.getFlightInformation().getAltitude());
        return pickupLocation;
    }

    private McuWaypoint createSpyExtractWaypoint(Coordinate pickupLocation)
    {
        McuWaypoint approachWP = WaypointFactory.createSpyExtractWaypointType();
        approachWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        approachWP.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
        approachWP.setPosition(pickupLocation);
        approachWP.setTargetWaypoint(true);
        return approachWP;
    }
}
