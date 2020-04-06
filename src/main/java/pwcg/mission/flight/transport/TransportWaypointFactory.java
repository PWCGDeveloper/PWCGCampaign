package pwcg.mission.flight.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class TransportWaypointFactory
{
    private IAirfield fromAirfield;
    private IAirfield toAirfield;

    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public TransportWaypointFactory(IFlight flight, IAirfield fromAirfield, IAirfield toAirfield) throws PWCGException
    {
        this.flight = flight;
        this.toAirfield = toAirfield;
        this.fromAirfield = fromAirfield;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);

        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
        missionPointSet.addWaypoints(targetWaypoints);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight, toAirfield);
        missionPointSet.addWaypoint(approachWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints() throws PWCGException
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        
        McuWaypoint midPointWP = createMidWaypoint();
        targetWaypoints.add(midPointWP);
        
        McuWaypoint destinationWP = createDestinationWaypoint(midPointWP);
        targetWaypoints.add(destinationWP);
        
        return targetWaypoints;
    }

    private McuWaypoint createMidWaypoint() throws PWCGException  
    {
        double angleFromTargetToHomeAirfield = MathUtils.calcAngle(fromAirfield.getLandingLocation().getPosition(), toAirfield.getLandingLocation().getPosition());
        double distanceBetweenAirfields = MathUtils.calcDist(fromAirfield.getTakeoffLocation().getPosition(), toAirfield.getLandingLocation().getPosition());
        distanceBetweenAirfields = distanceBetweenAirfields / 2;
        
        Coordinate midPointCoords = MathUtils.calcNextCoord(fromAirfield.getTakeoffLocation().getPosition(), angleFromTargetToHomeAirfield, distanceBetweenAirfields);
        midPointCoords.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint midPointWP = WaypointFactory.createMoveToWaypointType();
        midPointWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        midPointWP.setSpeed(flight.getFlightCruisingSpeed());
        midPointWP.setPosition(midPointCoords);
        midPointWP.setName("Mid Waypoint");
        return midPointWP;
    }
			
    private McuWaypoint createDestinationWaypoint(McuWaypoint lastWp) throws PWCGException  
	{
        double angleFromTargetToHomeAirfield = MathUtils.calcAngle(toAirfield.getLandingLocation().getPosition(), lastWp.getPosition());
        
        Coordinate destinationCoords = MathUtils.calcNextCoord(toAirfield.getLandingLocation().getPosition(), angleFromTargetToHomeAirfield, 10000.0);
        destinationCoords.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint destinationWP = WaypointFactory.createMoveToWaypointType();
        destinationWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        destinationWP.setSpeed(flight.getFlightCruisingSpeed());
        destinationWP.setPosition(destinationCoords);
        destinationWP.setName("Destination");
        return destinationWP;
	}
}
