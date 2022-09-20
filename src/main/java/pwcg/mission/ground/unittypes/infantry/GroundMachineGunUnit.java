package pwcg.mission.ground.unittypes.infantry;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundMachineGunUnit extends GroundUnit
{
    public GroundMachineGunUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.MachineGun, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createSpawnTimer();
        List<Coordinate> vehicleStartPositions = createVehicleStartPositions();
        super.createVehicles(vehicleStartPositions);
        addAspects();
        super.linkElements();
    }

    protected List<Coordinate> createVehicleStartPositions() throws PWCGException 
    {
        double spacing = 100.0;
        int numMachineGun = calcNumUnits();
        return createLineAcrossVehiclePositions(pwcgGroundUnitInformation.getPosition().copy(), numMachineGun, spacing);        
    }   

    protected int calcNumUnits() throws PWCGException
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            return GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 3);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(3, 5);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(4, 6);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    private void addAspects() throws PWCGException
    {
        super.addDirectFireAspect();
    }
}	
