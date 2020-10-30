package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.mcu.McuMissionStart;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;

public class MissionBeginUnit
{
	protected McuMissionStart missionBegin = new McuMissionStart();
	protected McuTimer missionBeginTimer = new McuTimer();
    protected Coordinate position = null;
    
    boolean hasBeenWritten = false;

	protected ArrayList<MissionBeginUnit> linkedUnits = new ArrayList<MissionBeginUnit>();

	public MissionBeginUnit(Coordinate position)
	{
	    this.position = position.copy();
        createUnitTimer();
	}

	protected void createUnitTimer()
	{
		missionBegin.setName("Mission Begin");		
		missionBegin.setDesc("Mission Begin");
		missionBegin.setPosition(position.copy());
		
		missionBeginTimer.setName("Mission Begin Timer");		
		missionBeginTimer.setDesc("Mission Begin Timer");
		missionBeginTimer.setPosition(position.copy());
		
		missionBegin.setTarget(missionBeginTimer.getIndex());
	}

	public void write(BufferedWriter writer) throws PWCGException
	{	
	    if (!hasBeenWritten)
	    {
	        if (missionBegin.getTargets().size() == 0)
	        {
	            throw new PWCGMissionGenerationException ("Uninitialized Mission Begin");
	        }
	        
	        missionBegin.write(writer);
	        missionBeginTimer.write(writer);
	    }
	    else
	    {
	        PWCGLogger.log(LogLevel.DEBUG, "Multiple Users");
	    }
	    
	    hasBeenWritten = true;
	}

	public void linkToMissionBegin (int targetIndex) 
	{
		missionBeginTimer.setTarget(targetIndex);
	}

    public void setStartTime(int time)
    {
        missionBeginTimer.setTime(time);
    }

    public int getStartTimeindex()
    {
        return missionBeginTimer.getIndex();
    }
    
    public void validate(int expectedTarget) throws PWCGException
    {
        if (!McuValidator.hasTarget(missionBeginTimer, expectedTarget))
        {
            throw new PWCGException("MissionBeginUnit: mission begin not linked to activate");
        }
    }

}	

