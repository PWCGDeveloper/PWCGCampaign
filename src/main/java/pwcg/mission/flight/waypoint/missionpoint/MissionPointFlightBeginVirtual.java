package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightBeginVirtual extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private IFlight flight;
    private McuWaypoint ingressWaypoint;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;

    public MissionPointFlightBeginVirtual(IFlight flight, McuWaypoint ingressWaypoint)
    {
        this.flight = flight;
        this.ingressWaypoint = ingressWaypoint;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_BEGIN_VIRTUAL;
    }
    
    public void createFlightBegin() throws PWCGException, PWCGException 
    {
        createAirStartWaypoint();  
    }


    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.getLastWaypoint().setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        throw new PWCGException("Attempt to link virtual waypoint begin");
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        return super.getWaypointsAsMissionPoints();
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

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
    }

    private void createAirStartWaypoint() throws PWCGException
    {
        McuWaypoint airStartWaypoint = AirStartWaypointFactory.createAirStart(flight, ingressWaypoint);
        super.addWaypoint(airStartWaypoint);
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
