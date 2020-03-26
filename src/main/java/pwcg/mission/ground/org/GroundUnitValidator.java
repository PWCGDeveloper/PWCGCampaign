package pwcg.mission.ground.org;

import pwcg.core.exception.PWCGException;
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
        validateAspects();
        validateElementLinkage();
    }

    private void validateVehicle() throws PWCGException
    {
        if (groundUnit.getVehicles() == null)
        {
            throw new PWCGException("GroundUnitValidator: no vehicle created");
        }
    }

    private void validateSpawners() throws PWCGException
    {
        for (GroundUnitElement groundUnitElement : groundUnit.getGroundElements())
        {
            if (groundUnitElement.getSpawn() == null)
            {
                throw new PWCGException("GroundUnitValidator: no spawners created");
            }

            if (!McuValidator.hasTarget(groundUnitElement.getSpawnTimer(), groundUnitElement.getSpawn().getIndex()))
            {
                throw new PWCGException("GroundUnitValidator: spawnTimer not linked to spawn");
            }

            if (!McuValidator.hasObject(groundUnitElement.getSpawn(), groundUnitElement.getVehicle().getEntity().getIndex()))
            {
                throw new PWCGException("GroundUnitValidator: spawn not linked to vehicle");
            }
        }
    }

    private void validateAspects() throws PWCGException
    {
        for (GroundUnitElement groundUnitElement : groundUnit.getGroundElements())
        {
            for (IGroundAspect aspect : groundUnitElement.getAspectsOfGroundUnit())
            {
                aspect.validate();
            }
        }
    }

    private void validateElementLinkage() throws PWCGException
    {
        for (GroundUnitElement groundUnitElement : groundUnit.getGroundElements())
        {
            if (!McuValidator.hasTarget(groundUnit.getSpawnUnitTimer(), groundUnitElement.getEntryPoint()))
            {
                throw new PWCGException("GroundUnit: spawn timer not linked to first element entry point");
            }
            
            validateElementAspects(groundUnitElement);
        }
    }

    private void validateElementAspects(GroundUnitElement groundUnitElement) throws PWCGException
    {
      
      IGroundAspect previousAspect = null;
      for (IGroundAspect aspect : groundUnitElement.getAspectsOfGroundUnit())
      {
          if (previousAspect == null)
          {
              if (!McuValidator.hasTarget(groundUnitElement.getSpawnTimer(), aspect.getEntryPoint()))
              {
                  throw new PWCGException("GroundUnitElement: spawn timer not linked to first aspect");
              }
          }
          else
          {
              if (!McuValidator.hasTarget(previousAspect.getEntryPointMcu(), aspect.getEntryPoint()))
              {
                  throw new PWCGException("GroundUnitElement: previous aspect not linked to next aspect");
              }
          }
          previousAspect = aspect;
      }
  }        
}
