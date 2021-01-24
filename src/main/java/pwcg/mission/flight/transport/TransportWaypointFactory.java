package pwcg.mission.flight.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class TransportWaypointFactory
{
    private Airfield fromAirfield;
    private Airfield toAirfield;

    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public TransportWaypointFactory(IFlight flight, Airfield fromAirfield, Airfield toAirfield) throws PWCGException
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
        Mission mission = flight.getMission();
        double angleFromTargetToHomeAirfield = MathUtils.calcAngle(fromAirfield.getLandingLocation(mission).getPosition(), toAirfield.getLandingLocation(mission).getPosition());
        double distanceBetweenAirfields = MathUtils.calcDist(fromAirfield.getTakeoffLocation(mission).getPosition(), toAirfield.getLandingLocation(mission).getPosition());
        distanceBetweenAirfields = distanceBetweenAirfields / 2;
        
        Coordinate midPointCoords = MathUtils.calcNextCoord(fromAirfield.getTakeoffLocation(mission).getPosition(), angleFromTargetToHomeAirfield, distanceBetweenAirfields);
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
        Mission mission = flight.getMission();
        double angleFromTargetToHomeAirfield = MathUtils.calcAngle(toAirfield.getLandingLocation(mission).getPosition(), lastWp.getPosition());
        
        Coordinate destinationCoords = MathUtils.calcNextCoord(toAirfield.getLandingLocation(mission).getPosition(), angleFromTargetToHomeAirfield, 10000.0);
        destinationCoords.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint destinationWP = WaypointFactory.createMoveToWaypointType();
        destinationWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        destinationWP.setSpeed(flight.getFlightCruisingSpeed());
        destinationWP.setPosition(destinationCoords);
        destinationWP.setName("Destination");
        return destinationWP;
	}
}
