package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuValidator;



public class SelfDeactivatingCheckZone 
{
	private String name = "Self Deactivating CZ";
	private String desc = "Self Deactivating CZ";
    private int index = IndexGenerator.getInstance().getNextIndex();;
	
    private McuTimer activateCZTimer = new McuTimer();
    private McuCheckZone checkZone = new McuCheckZone();
    
    private McuTimer deactivateCZTimer = new McuTimer();
    private McuDeactivate deactivateCZ = new McuDeactivate();
	

    public SelfDeactivatingCheckZone (Coordinate coordinate, int zone)
    {
        initialize(coordinate, zone);
        linkTargets();
    }

    private void initialize(Coordinate coordinate, int zone) 
    {
        checkZone.setZone(zone);
                        
        activateCZTimer.setPosition(coordinate.copy());
        checkZone.setPosition(coordinate.copy());

        deactivateCZTimer.setPosition(coordinate.copy());
        deactivateCZ.setPosition(coordinate.copy());
        
        activateCZTimer.setName("CZ Activate Timer");
        checkZone.setName("CZ");
        
        deactivateCZTimer.setName("CZ Deactivate Timer");
        deactivateCZ.setName("CZ Deactivate");

        activateCZTimer.setDesc("CZ Activate Timer");
        checkZone.setDesc("CZ");

        deactivateCZTimer.setDesc("VWP CZ Deactivate Timer");
        deactivateCZ.setDesc("CZ Deactivate");
        
        activateCZTimer.setTimer(0);
        deactivateCZTimer.setTimer(0);
    }

    private void linkTargets()
    {
        activateCZTimer.setTarget(checkZone.getIndex());

        // 2. If the CZ triggers, deactivate the CZ to avoid repeat triggers
        checkZone.setTarget(deactivateCZTimer.getIndex());
        deactivateCZTimer.setTarget(deactivateCZ.getIndex());
        
        // Deactivate both the CZ and the activate timer
        deactivateCZ.setTarget(checkZone.getIndex());        
        deactivateCZ.setTarget(activateCZTimer.getIndex());    
    }

    public void setAdditionalDeactivate(BaseFlightMcu additionalDeactivate)
    {
        additionalDeactivate.setTarget(deactivateCZTimer.getIndex());
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
            checkZone.write(writer);
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
    
    public int getActivateEntryPoint()
    {
        return activateCZTimer.getIndex();
    }

    public int getDeactivateEntryPoint()
    {
        return deactivateCZTimer.getIndex();
    }

    public void setCheckZoneTarget(int targetMcuIndex)
    {
        checkZone.setTarget(targetMcuIndex);
    }

    public void setCheckZoneObject(int objectMcuIndex)
    {
        checkZone.setObject(objectMcuIndex);
    }

    public void setCheckZoneCoalition(Coalition coalition)
    {
        checkZone.triggerCheckZoneByCoalition(coalition);
    }

    public void setCheckZoneCoalitions(List<Coalition> coalitions)
    {
        checkZone.triggerCheckZoneByCoalitions(coalitions);
    }

    public McuCheckZone getCheckZone()
    {
        return checkZone;
    }

    public void validate() throws PWCGException
    {
        if (!McuValidator.hasTarget(activateCZTimer, checkZone.getIndex()))
        {
            throw new PWCGException("SelfDeactivatingCheckZone: activate timer not linked to activate");
        }
        
        if (!McuValidator.hasTarget(checkZone, deactivateCZTimer.getIndex()))
        {
            throw new PWCGException("SelfDeactivatingCheckZone: check zone not linked to deactivate");
        }
        
        if (McuValidator.getNumTargets(checkZone) < 2)
        {
            throw new PWCGException("SelfDeactivatingCheckZone: check zone not linked to external entity");
        }
        
        if (!McuValidator.hasTarget(deactivateCZTimer, deactivateCZ.getIndex()))
        {
            throw new PWCGException("SelfDeactivatingCheckZone: deactivate timer not linked to deactivate");
        }
        
        if (!McuValidator.hasTarget(deactivateCZ, activateCZTimer.getIndex()))
        {
            throw new PWCGException("SelfDeactivatingCheckZone: deactivate timer not linked to activate timer");
        }
    }
}
