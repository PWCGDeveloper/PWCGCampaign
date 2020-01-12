package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class VirtualEscortFlightWaypointFactory
{
    private IFlight escortFlight;
    private IFlight escortedFlight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public VirtualEscortFlightWaypointFactory(IFlight escortFlight, IFlight escortedFlight) throws PWCGException
    {
        this.escortFlight = escortFlight;
        this.escortedFlight = escortedFlight;
    }
    
    public IMissionPointSet createWaypoints() throws PWCGException
    {
        List<McuWaypoint> waypoints = copyEscortedFlightWaypoints();
        missionPointSet.addWaypoints(waypoints);
        
        McuWaypoint rtbWP = ReturnToBaseWaypoint.createReturnToBaseWaypoint(escortFlight);
        missionPointSet.addWaypoint(rtbWP);

        return missionPointSet;
    }

    private List<McuWaypoint> copyEscortedFlightWaypoints() 
    {
        List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
        for (McuWaypoint escortedWaypoint : escortedFlight.getFlightData().getWaypointPackage().getAllWaypoints())
        {
            double altitude = escortedWaypoint.getPosition().getYPos() + 400.0;

            McuWaypoint escortWP = escortedWaypoint.copy();
            escortWP.getPosition().setYPos(altitude);
            escortWP.setPriority(WaypointPriority.PRIORITY_LOW);
            
            waypoints.add(escortWP);
        }
        
        return waypoints;
    }
}
