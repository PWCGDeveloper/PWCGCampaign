package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;



public class SelfDeactivatingCheckZone 
{
	private String name = "Self Deactivating CZ";
	private String desc = "Self Deactivating CZ";
    private int index = IndexGenerator.getInstance().getNextIndex();;
	
    private McuTimer activateCZTimer = new McuTimer();
    private McuCheckZone triggerCheckZone = null;
    
    private McuTimer deactivateCZTimer = new McuTimer();
    private McuDeactivate deactivateCZ = new McuDeactivate();
	
    
	public SelfDeactivatingCheckZone ()
	{    
	}
	

    /**
     * @param coordinate
     * @param planes
     * @param waypoint
     * @
     */
    public void initialize(Coordinate coordinate, Coalition coolition) 
    {
        triggerCheckZone = new McuCheckZone(coolition);
                        
        // set position
        activateCZTimer.setPosition(coordinate.copy());
        triggerCheckZone.setPosition(coordinate.copy());

        deactivateCZTimer.setPosition(coordinate.copy());
        deactivateCZ.setPosition(coordinate.copy());
        
        // set name
        activateCZTimer.setName("CZ Activate Timer");
        triggerCheckZone.setName("CZ");
        
        deactivateCZTimer.setName("CZ Deactivate Timer");
        deactivateCZ.setName("CZ Deactivate");

        // set desc
        activateCZTimer.setDesc("CZ Activate Timer");
        triggerCheckZone.setDesc("CZ");

        deactivateCZTimer.setDesc("VWP CZ Deactivate Timer");
        deactivateCZ.setDesc("CZ Deactivate");
        
        activateCZTimer.setTimer(0);
        deactivateCZTimer.setTimer(0);
    }
    
    /**
     * @param in
     */
    public void linkTargets(BaseFlightMcu activateIn, BaseFlightMcu deactivatIn)
    {
        // 1. Link the incoming MCU to the activate timer
        activateIn.setTarget(activateCZTimer.getIndex());
        activateCZTimer.setTarget(triggerCheckZone.getIndex());

        // 2. If the CZ triggers, deactivate the CZ to avoid repeat triggers
        triggerCheckZone.setTarget(deactivateCZTimer.getIndex());
        deactivateCZTimer.setTarget(deactivateCZ.getIndex());
        
        // Deactivate both the CZ and the ativate timer
        deactivateCZ.setTarget(triggerCheckZone.getIndex());        
        deactivateCZ.setTarget(activateCZTimer.getIndex());    
        
        // We may have an alternative deactivate
        if (deactivatIn != null)
        {
            deactivatIn.setTarget(deactivateCZTimer.getIndex());
        }
    }

    
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"" + name + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"" +  desc + "\";");
            writer.newLine();
            writer.newLine();

            activateCZTimer.write(writer);
            triggerCheckZone.write(writer);
            deactivateCZTimer.write(writer);
            deactivateCZ.write(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    
    /**
     * @param plane
     */
    public void setZone(int zone)
    {
        triggerCheckZone.setZone(zone);
    }

    /**
     * @param plane
     */
    public void setCZTarget(int targetMcuIndex)
    {
        triggerCheckZone.setTarget(targetMcuIndex);
    }

    /**
     * If the CZ fires on object rather than Coalition
     * 
     * @param plane
     */
    public void setCZObject(int objectMcuIndex)
    {
        triggerCheckZone.setObject(objectMcuIndex);
    }

    /**
     * If the CZ fires on object rather than Coalition
     * 
     * @param plane
     */
    public McuCheckZone getCheckZone()
    {
        return triggerCheckZone;
    }

    /**
     * @return
     */
    public McuTimer getDeactivateCZTimer()
    {
        return deactivateCZTimer;
    }
    
    
}
