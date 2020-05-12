package pwcg.mission.ground.unittypes.transport;

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

public class ShipCargoConvoyUnit extends GroundUnit
{
    public ShipCargoConvoyUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.ShipCargo, pwcgGroundUnitInformation);
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
            return GroundUnitNumberCalculator.calcNumUnits(3, 5);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(4, 6);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    private List<Coordinate> createVehicleStartPositions(int numvehicles) throws PWCGException 
    {
        return createVehiclePositions(pwcgGroundUnitInformation.getPosition().copy(), numvehicles);        
    }

    private List<Coordinate> createVehicleDestinationPositions(int numvehicles) throws PWCGException 
    {
        return createVehiclePositions(pwcgGroundUnitInformation.getDestination(), numvehicles);
    }

    private List<Coordinate> createVehiclePositions(Coordinate firstVehicleCoordinate, int numvehicles) throws PWCGException
    {        
        double shipMovementOrient = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());        
        double placementOrientation = MathUtils.adjustAngle (shipMovementOrient, -70);      
        Coordinate shipCoords = firstVehicleCoordinate.copy();
        List<Coordinate> vehicleLocations = new ArrayList<>();
        for (int i = 0; i < numvehicles; ++i)
        {   
            vehicleLocations.add(shipCoords);
            shipCoords = MathUtils.calcNextCoord(shipCoords, placementOrientation, 1000.0);
        }       
        return vehicleLocations;        
    }

    private void addAspects(List<Coordinate> destinations) throws PWCGException
    {       
        int unitSpeed = 5;
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_ON_WAYPOINT);
    }
}	

