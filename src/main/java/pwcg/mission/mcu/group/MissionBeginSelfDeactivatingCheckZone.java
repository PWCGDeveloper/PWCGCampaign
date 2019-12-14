package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuValidator;

public class MissionBeginSelfDeactivatingCheckZone
{
    private MissionBeginUnit missionBeginUnit;
    private SelfDeactivatingCheckZone checkZone;
    
    public MissionBeginSelfDeactivatingCheckZone(Coordinate position, int triggerZone)
    {
        missionBeginUnit = new MissionBeginUnit(position.copy());
        checkZone = new SelfDeactivatingCheckZone(position, triggerZone);
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
        if (!McuValidator.hasTarget(checkZone.getCheckZone(), entryPoint))
        {
            throw new PWCGException("Unit not linked to check zone");
        }
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
}
