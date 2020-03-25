package pwcg.mission.ground.org;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuValidator;

public class GroundUnitValidator
{    
    private GroundUnit groundUnit;
    
    public GroundUnitValidator(GroundUnit groundUnit)
    {
        this.groundUnit = groundUnit;
    }
    
    public void validate() throws PWCGException
    {
        validateVehicle();
        validateSpawners();
        validateElements();
        validateElementLinkage();
    }

    private void validateVehicle() throws PWCGException
    {
        if (groundUnit.getVehicle() == null)
        {
            throw new PWCGException("GroundUnitSpawning: no vehicle created");
        }
    }

    private void validateSpawners() throws PWCGException
    {
        if (groundUnit.getSpawners().size() == 0)
        {
            throw new PWCGException("GroundUnitSpawning: no spawners created");
        }
        
        for (McuSpawn spawn : groundUnit.getSpawners())
        {
            if (!McuValidator.hasTarget(groundUnit.getSpawnTimer(), spawn.getIndex()))
            {
                throw new PWCGException("GroundUnitSpawning: spawnTimer not linked to spawn");
            }

            spawn.setObject(groundUnit.getVehicle().getEntity().getIndex());
            if (!McuValidator.hasObject(spawn, groundUnit.getVehicle().getEntity().getIndex()))
            {
                throw new PWCGException("GroundUnitSpawning: spawn not linked to vehicle");
            }
        }
    }
    

    private void validateElements() throws PWCGException
    {
        for (IGroundAspect element : groundUnit.getGroundElements())
        {
            element.validate();
        }
    }

    private void validateElementLinkage() throws PWCGException
    {
        IGroundAspect previousElement = null;
        for (IGroundAspect element : groundUnit.getGroundElements())
        {
            if (previousElement == null)
            {
                if (!McuValidator.hasTarget(groundUnit.getSpawnTimer(), element.getEntryPoint()))
                {
                    throw new PWCGException("GroundUnit: spawn timer not linked to first element entry point");
                }
            }
            else
            {
                if (!McuValidator.hasTarget(previousElement.getEntryPointMcu(), element.getEntryPoint()))
                {
                    throw new PWCGException("GroundUnit: spawn timer not linked to first element entry point");
                }
            }
            previousElement = element;
        }
    }
}	

