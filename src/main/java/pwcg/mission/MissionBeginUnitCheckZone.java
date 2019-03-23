package pwcg.mission;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;

public class MissionBeginUnitCheckZone extends MissionBeginUnit
{
    protected SelfDeactivatingCheckZone checkZone = null;

    public MissionBeginUnitCheckZone (Coordinate unitPosition, int checkZoneRange)
    {
        super();
        checkZone = new SelfDeactivatingCheckZone(unitPosition, checkZoneRange);
        checkZone.linkTargets(missionBeginTimer, null);
    }

    @Override
    public void linkToMissionBegin (int targetIndex) 
    {
        checkZone.setCZTarget(targetIndex);
    }

	public void write(BufferedWriter writer) throws PWCGException 
	{	
        if (!hasBeenWritten)
        {
            super.write(writer);
    	    checkZone.write(writer);    		
        }
        else
        {
            Logger.log(LogLevel.DEBUG, "Multiple Users");
        }
	}

    public SelfDeactivatingCheckZone getSelfDeactivatingCheckZone()
    {
        return this.checkZone;
    }
}	

