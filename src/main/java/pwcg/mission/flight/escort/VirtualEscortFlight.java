package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.initialposition.AirStartFormationSetter;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuWaypoint;

public class VirtualEscortFlight extends Flight
{
    private Flight escortedFlight = null;


    public VirtualEscortFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, Flight escortedFlight)
    {
        super (flightInformation, missionBeginUnit);
        this.escortedFlight = escortedFlight;
    }

    public void createEscortPositionCloseToFirstWP() throws PWCGException 
	{
		Coordinate escortedFlightCoords = escortedFlight.getPlanes().get(0).getPosition().copy();
		
        Coordinate escortFlightCoords = new Coordinate();
        
        escortFlightCoords.setXPos(escortedFlightCoords.getXPos() + 100);
        escortFlightCoords.setZPos(escortedFlightCoords.getZPos()+ 100);
        escortFlightCoords.setYPos(escortedFlightCoords.getYPos() + 300);

        AirStartFormationSetter.resetAirStartFormation(this, escortFlightCoords);
	}

    @Override
    protected List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) 
    {
        List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
        for (McuWaypoint escortedWaypoint : escortedFlight.getAllFlightWaypoints())
        {
            double altitude = escortedWaypoint.getPosition().getYPos() + 400.0;

            McuWaypoint escortWP = escortedWaypoint.copy();
            escortWP.getPosition().setYPos(altitude);
            escortWP.setPriority(WaypointPriority.PRIORITY_LOW);
            
            waypoints.add(escortWP);
        }
        
        return waypoints;
    }

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}	

