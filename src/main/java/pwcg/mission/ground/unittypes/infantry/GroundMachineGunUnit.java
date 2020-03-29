package pwcg.mission.ground.unittypes.infantry;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
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
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numMachineGun = calcNumUnits();
        
        double initialPlacementAngle = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 180.0);      
        Coordinate machineGunCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), initialPlacementAngle, 25.0);

        double startLocationOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 270);             
        double machingGunSpacing = 100.0;
        machineGunCoords = MathUtils.calcNextCoord(machineGunCoords, startLocationOrientation, ((numMachineGun * machingGunSpacing) / 2));       
        
        double placementOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 90.0);        

        for (int i = 0; i < numMachineGun; ++i)
        {   
            spawnerLocations.add(machineGunCoords);
            machineGunCoords = MathUtils.calcNextCoord(machineGunCoords, placementOrientation, machingGunSpacing);
        }       
        return spawnerLocations;       
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
            return GroundUnitNumberCalculator.calcNumUnits(3, 6);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(5, 10);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    private void addAspects() throws PWCGException
    {
        super.addDirectFireAspect();
    }
}	
