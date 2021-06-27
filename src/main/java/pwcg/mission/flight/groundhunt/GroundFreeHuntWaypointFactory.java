package pwcg.mission.flight.groundhunt;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointHuntSet;
import pwcg.mission.mcu.McuWaypoint;

public class GroundFreeHuntWaypointFactory
{
    private IFlight flight;
    private MissionPointHuntSet missionPointSet = new MissionPointHuntSet();

    public GroundFreeHuntWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }
    
    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypointBefore(ingressWaypoint);
        createTargetWaypoints();

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypointAfter(egressWaypoint);
                
        return missionPointSet;
    }

    private void createTargetWaypoints() throws PWCGException  
	{
        GroundFreeHuntWaypointHelper freeHuntWaypointHelper = new GroundFreeHuntWaypointHelper(flight);
        McuWaypoint huntBeginWaypoint = freeHuntWaypointHelper.createBeginHuntWaypoint();
        missionPointSet.addWaypointBefore(huntBeginWaypoint);
	}
}
