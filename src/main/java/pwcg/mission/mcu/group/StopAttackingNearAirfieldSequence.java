package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class StopAttackingNearAirfieldSequence
{
    private MissionBeginUnit missionBeginUnit;
    private McuTimer checkZoneTimer = new McuTimer();
    private McuCheckZone activateCheckZone;
    private McuTimer goAwayTimer = new McuTimer();

    public void makeThePlaneGoAway(PlaneMcu plane, McuWaypoint goAwayWaypoint, Coordinate triggerPosition) throws PWCGException
    {
        missionBeginUnit = new MissionBeginUnit(triggerPosition.copy());

        buildActivate(triggerPosition, plane);
        buildTimers(triggerPosition);
        
        setTargetAssociations(goAwayWaypoint);
        setObjectAssociations(plane, goAwayWaypoint);
    }

    private void buildActivate(Coordinate position, PlaneMcu plane) throws PWCGException
    {
        activateCheckZone = new McuCheckZone("CheckZone Go Away");
        activateCheckZone.setCloser(1);
        activateCheckZone.setZone(10000);
        activateCheckZone.setPosition(position.copy());
        activateCheckZone.triggerCheckZoneBySingleObject(plane.getLinkTrId());
    }

    private void buildTimers(Coordinate position)
    {
        checkZoneTimer.setTime(1);
        checkZoneTimer.setName("Go Away CZ Timer");
        checkZoneTimer.setPosition(position.copy());

        goAwayTimer.setTime(90);
        goAwayTimer.setName("Go Away Triggered Timer");
        goAwayTimer.setPosition(position.copy());
    }

    private void setTargetAssociations(McuWaypoint goAwayWaypoint)
    {

        missionBeginUnit.linkToMissionBegin(checkZoneTimer.getIndex());
        checkZoneTimer.setTimerTarget(activateCheckZone.getIndex());
        activateCheckZone.setCheckZoneTarget(goAwayTimer.getIndex());
        goAwayTimer.setTimerTarget(goAwayWaypoint.getIndex());
    }

    private void setObjectAssociations(PlaneMcu plane, McuWaypoint goAwayWaypoint)
    {
        goAwayWaypoint.setObject(plane.getLinkTrId());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            missionBeginUnit.write(writer);
            checkZoneTimer.write(writer);
            activateCheckZone.write(writer);
            goAwayTimer.write(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
}
