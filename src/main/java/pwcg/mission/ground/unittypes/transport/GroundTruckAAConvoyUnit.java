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

public class GroundTruckAAConvoyUnit extends GroundUnit
{
    static private int TRUCK_AA_ATTACK_AREA = 20000;

    public GroundTruckAAConvoyUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.TruckAAA, pwcgGroundUnitInformation);
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
            return GroundUnitNumberCalculator.calcNumUnits(1, 2);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 2);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 3);
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
        double placementOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 180);
        List<Coordinate> vehiclePositions = new ArrayList<>();
        Coordinate vehicleCoordinate = MathUtils.calcNextCoord(firstVehicleCoordinate, placementOrientation, 300.0);
        for (int i = 0; i < numvehicles; ++i)
        {   
            vehicleCoordinate = MathUtils.calcNextCoord(vehicleCoordinate.copy(), placementOrientation, 15.0);
            vehiclePositions.add(vehicleCoordinate);
        }       
        return vehiclePositions;
    }

    private void addAspects(List<Coordinate> destinations) throws PWCGException
    {       
        super.addAAAFireAspect(TRUCK_AA_ATTACK_AREA);

        int unitSpeed = 10;
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_ON_ROAD);
    }
}	

