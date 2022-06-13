package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointHuntSet extends MissionPointSetMultipleWaypointSet implements IMissionPointSet
{
    private McuTimer huntTimer = new McuTimer();
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;
    
    public MissionPointHuntSet()
    {
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_DELAY;
    }
    
    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.setLinkToLastWaypoint(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return getEntryPointAtFirstWaypoint();
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints() throws PWCGException
    {
        List<MissionPoint> missionPoints = new ArrayList<>();
        
        List<MissionPoint> missionPointsBefore = super.getWaypointsBeforeAsMissionPoints();
        missionPoints.addAll(missionPointsBefore);

        List<MissionPoint> missionPointsAfter = super.getWaypointsAfterAsMissionPoints();
        missionPoints.addAll(missionPointsAfter);

        return missionPoints;
    }

    @Override
    public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException
    {
        createHuntTimer();
        
        super.finalizeMissionPointSet(flightPlanes);
    }

    private void createHuntTimer() throws PWCGException
    {
        McuWaypoint firstWaypointAfter = super.getFirstWaypointAfter();

        huntTimer.setName("Ground Unit Hunt Timer");
        huntTimer.setDesc("Ground Unit Hunt Timer");
        huntTimer.setPosition(firstWaypointAfter.getPosition());
        huntTimer.setTimerTarget(firstWaypointAfter.getIndex());        

        McuWaypoint lastWaypointBefore = super.getLastWaypointBefore();
        lastWaypointBefore.setTarget(huntTimer.getIndex());
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        super.write(writer);
        huntTimer.write(writer);
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
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypointsBefore.getWaypoints());
        allFlightPoints.addAll(waypointsAfter.getWaypoints());
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }
}