package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.end.TerminalWaypointGenerator;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightEnd extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private IFlight flight;

    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;

    public MissionPointFlightEnd(IFlight flight)
    {
        this.flight = flight;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_END;
    }
    
    public void createFlightEnd() throws PWCGException, PWCGException 
    {
        createTerminalWaypoint();  
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints() throws PWCGException
    {
        List<MissionPoint> waypointsCoordinates =  super.getWaypointsAsMissionPoints();
        MissionPoint landPoint = new MissionPoint(flight.getFlightInformation().getHomePosition(), WaypointAction.WP_ACTION_LANDING_APPROACH);
        waypointsCoordinates.add(landPoint);
        return waypointsCoordinates;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.getLastWaypoint().setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return super.getFirstWaypoint().getIndex();
    }    

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;        
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

    @Override
    public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException
    {
        super.finalizeMissionPointSet(flightPlanes);
    }
    
    private void createTerminalWaypoint() throws PWCGException
    {
        McuWaypoint approachWaypoint = TerminalWaypointGenerator.createTerminalWaypoint(flight);
        super.addWaypoint(approachWaypoint);
    }
  
    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypoints.getWaypoints());
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }
}
