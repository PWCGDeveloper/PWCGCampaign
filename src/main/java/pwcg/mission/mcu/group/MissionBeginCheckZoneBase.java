package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.mcu.Coalition;

public class MissionBeginCheckZoneBase
{
    protected MissionBeginUnit missionBeginUnit;
    protected ICheckZone checkZone;

    public MissionBeginCheckZoneBase(Coordinate position, ICheckZone checkZone)
    {
        this.checkZone = checkZone;
        missionBeginUnit = new MissionBeginUnit(position.copy());
        missionBeginUnit.linkToMissionBegin(checkZone.getActivateEntryPoint());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        missionBeginUnit.write(writer);
        checkZone.write(writer);
    }

    public void validate() throws PWCGException
    {
        missionBeginUnit.validate(checkZone.getActivateEntryPoint());
        checkZone.validate();
    }

    public void validateTarget(int entryPoint) throws PWCGException
    {
        checkZone.validateTarget(entryPoint);
    }

    public void linkCheckZoneTarget(int outTarget)
    {
        checkZone.setCheckZoneTarget(outTarget);
    }

    public void setCheckZoneTriggerObject(int triggerObject)
    {
        checkZone.setCheckZoneTriggerObject(triggerObject);
    }

    public void setCheckZoneCoalitions(List<Coalition> triggerCoalitions)
    {
        checkZone.setCheckZoneTriggerCoalitions(triggerCoalitions);
    }

    public void setCheckZoneCoalition(Coalition triggerCoalition)
    {
        checkZone.setCheckZoneTriggerCoalition(triggerCoalition);
    }

    public void triggerOnPlayerProximity(Mission mission) throws PWCGException
    {
        checkZone.triggerOnPlayerProximity(mission);
    }

}
