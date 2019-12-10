package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;

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

    public SelfDeactivatingCheckZone getCheckZone()
    {
        return checkZone;
    }

    public void validate() throws PWCGException
    {
        missionBeginUnit.validate(checkZone.getActivateEntryPoint());
        checkZone.validate();
    }
}
