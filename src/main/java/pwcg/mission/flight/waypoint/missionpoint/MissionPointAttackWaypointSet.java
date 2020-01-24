package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.FormationGenerator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequence;

public class MissionPointAttackWaypointSet extends MissionPointSetMultipleWaypointSet implements IMissionPointSet
{
    private AirGroundAttackMcuSequence attackSequence;
    private boolean linkToNextTarget = true;
    
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
    public void finalize(PlaneMcu plane) throws PWCGException
    {
        super.finalize(plane);
        attackSequence.finalize(plane);
        
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

    public void setAttackSequence(AirGroundAttackMcuSequence attackSequence)
    {
        this.attackSequence = attackSequence;
    }

    @Override
    public IMissionPointSet duplicateWithOffset(IFlightInformation flightInformation, int positionInFormation) throws PWCGException
    {
        MissionPointAttackWaypointSet duplicate = new MissionPointAttackWaypointSet();
        duplicate.waypointsBefore = super.duplicateBeginWaypoints(positionInFormation);
        duplicate.waypointsAfter = super.duplicateAfterWaypoints(positionInFormation);

        duplicate.attackSequence = new AirGroundAttackMcuSequence(flightInformation);
        duplicate.attackSequence.createAttackArea(180, FlightTypes.getAttackAreaTypeByFlightyType(flightInformation.getFlightType()));
        Coordinate newPosition = FormationGenerator.generatePositionForPlaneInFormation(new Orientation(), attackSequence.getPosition(), positionInFormation);
        duplicate.attackSequence.changeAttackAreaPosition (newPosition);
        
        return duplicate;
    }
}