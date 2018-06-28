package pwcg.mission;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuBehavior;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;

public class MissionBeginUnitBehavior extends MissionBeginUnit
{
    protected SelfDeactivatingCheckZone spawnCheckZone = null;
    protected SelfDeactivatingCheckZone behaviorCheckZone = null;

    protected McuBehavior behavior = null;

	/**
	 * Constructor
	 * 
	 * @param mission
	 */
	public MissionBeginUnitBehavior() 
	{
		super();
	}

	public void initialize(Coordinate unitPosition) throws PWCGException 
	{
		super.initialize(unitPosition);		
        createCheckZone();
	}
	
	   /**
     * Timer->Proximity (Player Plane)->Activate->Spawn
	 * @throws PWCGException 
     * 
     */
    protected void createCheckZone() throws PWCGException 
    {       
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        Coalition playerCoalition  = Coalition.getFriendlyCoalition(campaign.determineCountry());
        
        // Create the proximity entity
        spawnCheckZone = new SelfDeactivatingCheckZone();
        spawnCheckZone.initialize(position.copy(), playerCoalition);
        spawnCheckZone.setZone(5000);
        
        // Create the behavior change entity
        behaviorCheckZone = new SelfDeactivatingCheckZone();
        behaviorCheckZone.initialize(position.copy(), playerCoalition);
        behaviorCheckZone.setZone(750);

        behavior = new McuBehavior();
        behavior.setName("Behavior");
        behavior.setDesc("Behavior");
        behavior.setPosition(position.copy());
        
        // Link the MB timer to the Spawn CZ
        spawnCheckZone.linkTargets(missionBeginTimer, null);
        
        // Link the the Spawn CZ to the behavior CZ       
        behaviorCheckZone.linkTargets(spawnCheckZone.getCheckZone(), null);
        behaviorCheckZone.setCZTarget(behavior.getIndex());
    }

    
    /**
     *  This MB links to check zone instead of unit timer
     */
    @Override
    public void linkToMissionBegin (int targetIndex) 
    {
        spawnCheckZone.setCZTarget(targetIndex);
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
            spawnCheckZone.write(writer);
            
            behaviorCheckZone.write(writer);
            
            behavior.write(writer);

            super.write(writer);
        }
	}

    /**
     * @param object
     */
    public void linkToBehavior(int object)
    {
        behavior.setObject(object);
    }
    

    /**
     * @return
     */
    public SelfDeactivatingCheckZone getSpawnCheckZone()
    {
        return this.spawnCheckZone;
    }

    /**
     * @return
     */
    public SelfDeactivatingCheckZone getBehaviorCheckZone()
    {
        return this.behaviorCheckZone;
    }
}	

