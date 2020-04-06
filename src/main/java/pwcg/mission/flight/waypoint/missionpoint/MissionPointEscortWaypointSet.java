package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.EscortMcuSequence;

// Ingress -> Rendezvous WP -> Cover Timer -> Cover -> force Complete Timer -> Egress Wp
public class MissionPointEscortWaypointSet extends MissionPointSetMultipleWaypointSet implements IMissionPointSet
{
    public enum EscortSequenceConnect
    {
        CONNECT_ESCORT_SEQUENCE,
        DO_NOT_CONNECT_ESCORT_SEQUENCE;
    }
    
    private EscortMcuSequence escortSequence;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;
    private EscortSequenceConnect escortSequenceConnect;
    
    public MissionPointEscortWaypointSet(EscortSequenceConnect escortSequenceConnect)
    {
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_ESCORT;
        this.escortSequenceConnect = escortSequenceConnect;
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
        
        MissionPoint coverPoint = new MissionPoint(escortSequence.getPosition(), WaypointAction.WP_ACTION_RENDEZVOUS);
        missionPoints.add(coverPoint);
        
        List<MissionPoint> missionPointsAfter = super.getWaypointsAfterAsMissionPoints();
        missionPoints.addAll(missionPointsAfter);

        return missionPoints;
    }
    
    @Override
    public void finalize(PlaneMcu plane) throws PWCGException
    {
        super.finalize(plane);
        linkEscortSequenceToWaypoints();
        escortSequence.finalize();
    }

    public void setCoverSequence(EscortMcuSequence coverSequence)
    {
        this.escortSequence = coverSequence;
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
        escortSequence.write(writer);
    }

    private void linkEscortSequenceToWaypoints() throws PWCGException
    {
        if (escortSequenceConnect == EscortSequenceConnect.CONNECT_ESCORT_SEQUENCE)
        {
            McuWaypoint lastWaypointBefore = super.getLastWaypointBefore();
            lastWaypointBefore.setTarget(escortSequence.getCoverEntry());
        }
        
        McuWaypoint firstWaypointAfter = super.getFirstWaypointAfter();
        escortSequence.setLinkToNextTarget(firstWaypointAfter.getIndex());
    }

    private void validate() throws PWCGException
    {
    }

    @Override
    public IMissionPointSet duplicateWithOffset(IFlight flight, int positionInFormation) throws PWCGException
    {
        throw new PWCGException("Attempt to duplicate escort waypoint set.  Should ever be virtual");
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

    // test purposes
    public EscortMcuSequence getEscortSequence()
    {
        return escortSequence;
    }
}