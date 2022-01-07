package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightActivateVirtual implements IMissionPointSet, IVirtualActivate
{    
    private MissionBeginUnit missionBeginUnit;
    private McuTimer missionBeginTimer = null;
    private boolean linkToNextTarget = false;
    private MissionPointSetType missionPointSetType;

    public MissionPointFlightActivateVirtual()
    {
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_ACTIVATE;
    }
    
    public void createFlightActivate(McuWaypoint ingressWaypoint) throws PWCGException, PWCGException 
    {
        createFlightMissionBegin(ingressWaypoint);
        createActivation(ingressWaypoint);  
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
    }

    @Override
    public void linkMissionBeginToFirstVirtualWaypoint(int firstVirtualWaypointIndex) throws PWCGException
    {
        missionBeginTimer.setTarget(firstVirtualWaypointIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return missionBeginTimer.getIndex();
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        return new ArrayList<MissionPoint>();
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
        createTargetAssociations();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        missionBeginTimer.write(writer);
    }
    
    private void createFlightMissionBegin(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionBeginUnit = new MissionBeginUnit(ingressWaypoint.getPosition().copy());
        missionBeginUnit.setStartTime(1);
    }

    private void createActivation(McuWaypoint ingressWaypoint) throws PWCGException
    {   
        missionBeginTimer = new McuTimer();
        missionBeginTimer.setName("Mission Begin VWP Timer");
        missionBeginTimer.setDesc("Mission Begin VWP Timer");
        missionBeginTimer.setPosition(ingressWaypoint.getPosition().copy());        
        missionBeginTimer.setTime(1);
    }

    private void createTargetAssociations()
    {
        missionBeginUnit.linkToMissionBegin(missionBeginTimer.getIndex());
    }

    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> allWaypoints = new ArrayList<>();
        return allWaypoints;
    }

    @Override
    public boolean containsWaypoint(long waypointIdToFind)
    {
        return false;
    }

    @Override
    public McuWaypoint getWaypointById(long waypointId) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                
    }

    @Override
    public void updateWaypointFromBriefing(BriefingMapPoint waypoint) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                
    }

    @Override
    public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                        
    }

    @Override
    public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                        
    }

    @Override
    public long addWaypointFromBriefing(BriefingMapPoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                                
    }

    @Override
    public void removeUnwantedWaypoints(List<BriefingMapPoint> waypointsInBriefing) throws PWCGException
    {
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }

    public McuTimer getMissionBeginTimer()
    {
        return missionBeginTimer;
    }    
}
