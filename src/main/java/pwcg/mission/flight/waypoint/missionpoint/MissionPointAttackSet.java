package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.IAirGroundAttackAreaMcuSequence;

public class MissionPointAttackSet extends MissionPointSetMultipleWaypointSet implements IMissionPointSet
{
    private IAirGroundAttackAreaMcuSequence attackSequence;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;
    
    public MissionPointAttackSet()
    {
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_ATTACK;
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
        
        MissionPoint attackPoint = new MissionPoint(attackSequence.getPosition(), WaypointAction.WP_ACTION_ATTACK);
        missionPoints.add(attackPoint);
        
        List<MissionPoint> missionPointsAfter = super.getWaypointsAfterAsMissionPoints();
        missionPoints.addAll(missionPointsAfter);

        return missionPoints;
    }

    @Override
    public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException
    {
        super.finalizeMissionPointSet(flightPlanes);
        attackSequence.setAttackToTriggerOnPlane(flightPlanes.getPlanes());
         
        McuWaypoint firstWaypointAfter = super.getFirstWaypointAfter();
        attackSequence.setLinkToNextTarget(firstWaypointAfter.getIndex());        
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        super.write(writer);
        attackSequence.write(writer);
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

    public void setAttackSequence(IAirGroundAttackAreaMcuSequence attackSequence)
    {
        this.attackSequence = attackSequence;
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypointsBefore.getWaypoints());
        allFlightPoints.add(attackSequence.getAttackAreaMcu());
        allFlightPoints.addAll(waypointsAfter.getWaypoints());
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }

    public IAirGroundAttackAreaMcuSequence getAttackSequence()
    {
        return attackSequence;
    }
}