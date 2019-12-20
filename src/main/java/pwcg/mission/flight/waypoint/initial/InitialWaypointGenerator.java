package pwcg.mission.flight.waypoint.initial;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class InitialWaypointGenerator 
{
    protected Flight flight = null;
    protected int waypointSpeed = 250;

	public InitialWaypointGenerator(Flight flight) throws PWCGException 
	{
        this.flight = flight;
	}

    public List<McuWaypoint> createInitialFlightWaypoints() throws PWCGException 
    {
        if (flight.isPlayerFlight())
        {
            return createPlayerInitialAirStartWaypoints();
        }
        else
        {
            return createAiInitialAirStartWaypoints();
        }        
    }

    private List<McuWaypoint> createPlayerInitialAirStartWaypoints() throws PWCGException
    {
        List<McuWaypoint> initialWPs = new ArrayList<McuWaypoint>();
        if (!flight.isAirStart())
        {
            ClimbWaypointBuilder climbWaypointGenerator = new ClimbWaypointBuilder(flight);
            initialWPs = climbWaypointGenerator.createClimbWaypointsForPlayerFlight();
        }
        else
        {
            McuWaypoint airStartWP = AirStartNearIngressWaypointBuilder.buildAirStartNearIngress(flight);
            initialWPs.add(airStartWP);
        }
        return initialWPs;
    }  

    private List<McuWaypoint> createAiInitialAirStartWaypoints() throws PWCGException
    {
        List<McuWaypoint> initialWPs = new ArrayList<McuWaypoint>();
        if (flight.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            McuWaypoint airStartWP = AirStartNearIngressWaypointBuilder.buildAirStartNearIngress(flight);
            initialWPs.add(airStartWP);
        }
        else if (flight.getFlightType() == FlightTypes.SCRAMBLE_OPPOSE)
        {
            McuWaypoint airStartWP = AirStartNearIngressWaypointBuilder.buildAirStartNearIngress(flight);
            initialWPs.add(airStartWP);
        }
        else
        {
            McuWaypoint airStartWP = AirStartNearAirfieldBuilder.buildAirStartNearAirfield(flight);
            initialWPs.add(airStartWP);
        }
        return initialWPs;
    }
 }
