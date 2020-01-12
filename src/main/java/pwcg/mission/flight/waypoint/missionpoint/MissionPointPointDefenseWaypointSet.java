package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.PointDefenseMcuSequence;

public class MissionPointPointDefenseWaypointSet extends MissionPointSetMultipleWaypointSet implements IMissionPointSet
{
    private PointDefenseMcuSequence pointDefenseSequence;
    private boolean linkToNextTarget = true;

    public MissionPointPointDefenseWaypointSet(IFlight coveringFlight)
    {
    }
    
    public void addWaypointBefore(McuWaypoint waypoint)
    {
        super.addWaypointBefore(waypoint);
    }
    
    public void addWaypointAfter(McuWaypoint waypoint)
    {
        super.addWaypointAfter(waypoint);
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
    public List<MissionPoint> getFlightMissionPoints()
    {
        List<MissionPoint> missionPoints = new ArrayList<>();
        
        List<MissionPoint> missionPointsBefore = super.getWaypointsBeforeAsMissionPoints();
        missionPoints.addAll(missionPointsBefore);
        
        MissionPoint defendPoint = new MissionPoint(pointDefenseSequence.getPosition(), WaypointAction.WP_ACTION_PATROL);
        missionPoints.add(defendPoint);
        
        List<MissionPoint> missionPointsAfter = super.getWaypointsAfterAsMissionPoints();
        missionPoints.addAll(missionPointsAfter);

        return missionPoints;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        super.write(writer);        
        pointDefenseSequence.write(writer);
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
    public void finalize(PlaneMcu plane) throws PWCGException
    {
        super.finalize(plane);
        linkPointDefense();
    }

    public void setCoverSequence(PointDefenseMcuSequence coverSequence)
    {
        this.pointDefenseSequence = coverSequence;
    }

    private void linkPointDefense() throws PWCGException
    {
        McuWaypoint lastWaypointBefore = super.getLastWaypointBefore();
        lastWaypointBefore.setTarget(pointDefenseSequence.getCoverEntry());

        McuWaypoint firstWaypointAfter = super.getFirstWaypointAfter();
        pointDefenseSequence.setLinkToNextTarget(firstWaypointAfter.getIndex());
    }
}