package pwcg.mission.ground.unittypes.infantry;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundAssaultTankUnit extends GroundUnit
{
    public GroundAssaultTankUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Tank, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException 
    {
        super.createSpawnTimer();
        int numvehicles = calcNumUnits();
        List<Coordinate> vehicleStartPositions = createVehicleStartPositions(numvehicles);
        super.createVehicles(vehicleStartPositions);
        List<Coordinate> destinations =  createVehicleDestinationPositions(numvehicles);
        addAspects(destinations);
        super.linkElements();
    }

    private int calcNumUnits() throws PWCGException
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            return GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(4, 6);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(6, 8);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    private List<Coordinate> createVehicleStartPositions(int numvehicles) throws PWCGException 
    {
        double spacing = 30.0;
        return createWedgeVehiclePositions(pwcgGroundUnitInformation.getPosition().copy(), numvehicles, spacing);        
    }

    private List<Coordinate> createVehicleDestinationPositions(int numvehicles) throws PWCGException 
    {
        double spacing = 30.0;
        Coordinate destination = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getDestination(), pwcgGroundUnitInformation.getOrientation().getyOri(), 3000.0);
        return createWedgeVehiclePositions(destination, numvehicles, spacing);
    }

    private void addAspects(List<Coordinate> destinations) throws PWCGException
    {       
        super.addDirectFireAspect();
        
        int unitSpeed = 10;
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_OFF_ROAD);
    }
}	

