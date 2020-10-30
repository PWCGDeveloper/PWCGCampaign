package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.IAirGroundAttackMcuSequence;

public class MissionPointAttackSet extends MissionPointSetMultipleWaypointSet implements IMissionPointSet
{
    private IAirGroundAttackMcuSequence attackSequence;
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
    public void finalizeMissionPointSet(PlaneMcu plane) throws PWCGException
    {
        super.finalizeMissionPointSet(plane);
        attackSequence.setAttackToTriggerOnPlane(plane.getLinkTrId());
        
        McuWaypoint linkToAttack = super.getLastWaypointBefore();
        attackSequence.setLinkToAttack(linkToAttack);        
        
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

    public void setAttackSequence(IAirGroundAttackMcuSequence attackSequence)
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

    public IAirGroundAttackMcuSequence getAttackSequence()
    {
        return attackSequence;
    }
}