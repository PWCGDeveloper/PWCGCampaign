package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.EscortMcuSequence;

public class MissionPointEscortWaypointSet extends MissionPointSetMultipleWaypointSet implements IMissionPointSet
{
    private EscortMcuSequence coverSequence;
    private boolean linkToNextTarget = true;

    public MissionPointEscortWaypointSet(IFlight coveringFlight)
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
        
        MissionPoint coverPoint = new MissionPoint(coverSequence.getPosition(), WaypointAction.WP_ACTION_RENDEZVOUS);
        missionPoints.add(coverPoint);
        
        List<MissionPoint> missionPointsAfter = super.getWaypointsAfterAsMissionPoints();
        missionPoints.addAll(missionPointsAfter);

        return missionPoints;
    }
    
    @Override
    public void finalize(PlaneMcu plane) throws PWCGException
    {
        super.finalize(plane);
        linkPointDefense();
    }

    public void setCoverSequence(EscortMcuSequence coverSequence)
    {
        this.coverSequence = coverSequence;
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
    public void write(BufferedWriter writer) throws PWCGException
    {
        validate();
        
        super.write(writer);
        coverSequence.write(writer);
    }

    private void linkPointDefense() throws PWCGException
    {
        McuWaypoint lastWaypointBefore = super.getLastWaypointBefore();
        lastWaypointBefore.setTarget(coverSequence.getCoverEntry());

        McuWaypoint firstWaypointAfter = super.getFirstWaypointAfter();
        coverSequence.setLinkToNextTarget(firstWaypointAfter.getIndex());
    }

    private void validate() throws PWCGException
    {
    }

    @Override
    public IMissionPointSet duplicateWithOffset(IFlightInformation flightInformation, int positionInFormation) throws PWCGException
    {
        throw new PWCGException("Attempt to duplicate escort waypoint set.  Should ever be virtual");
    }
}