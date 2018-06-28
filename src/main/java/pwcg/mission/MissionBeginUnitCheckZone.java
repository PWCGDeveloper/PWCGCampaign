package pwcg.mission;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;

public class MissionBeginUnitCheckZone extends MissionBeginUnit
{
    protected SelfDeactivatingCheckZone checkZone = null;

	/**
	 * Constructor
	 * 
	 * @param mission
	 */
	public MissionBeginUnitCheckZone() 
	{
		super();
	}

    /**
     * @param unitPosition
     * @param checkZoneRange
     * @throws PWCGException 
     * @
     */
    public void initialize(Coordinate unitPosition, int checkZoneRange, Coalition coalition) throws PWCGException 
    {
        super.initialize(unitPosition);     
        createCheckZone(checkZoneRange, coalition);
    }
	
	/**
     * Timer->Proximity (Player Plane)->Activate->Spawn
     * 
     */
    protected void createCheckZone(int checkZoneRange, Coalition coalition) 
    {       
        // Create the proximity entity
        checkZone = new SelfDeactivatingCheckZone();
        checkZone.initialize(position.copy(), coalition);
        checkZone.setZone(checkZoneRange);

        // Link the MB timer to the CZ
        checkZone.linkTargets(missionBeginTimer, null);
    }

    /**
     *  This MB links to check zone instead of unit timer
     */
    @Override
    public void linkToMissionBegin (int targetIndex) 
    {
        checkZone.setCZTarget(targetIndex);
    }

	
	/**
	 * Write the mission to a file
	 * 
	 * @param writer
	 * @throws PWCGException 
	 * @
	 */
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

    public SelfDeactivatingCheckZone getCheckZone()
    {
        return this.checkZone;
    }
}	

