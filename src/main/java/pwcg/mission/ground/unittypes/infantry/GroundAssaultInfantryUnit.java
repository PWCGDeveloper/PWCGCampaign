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

public class GroundAssaultInfantryUnit extends GroundUnit
{
    public GroundAssaultInfantryUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Infantry, pwcgGroundUnitInformation);
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
            return GroundUnitNumberCalculator.calcNumUnits(10, 20);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(20, 30);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(30, 40);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    private List<Coordinate> createVehicleStartPositions(int numvehicles) throws PWCGException 
    {
        double spacing = 15.0;
        return createLineAcrossVehiclePositions(pwcgGroundUnitInformation.getPosition().copy(), numvehicles, spacing);        
    }

    private List<Coordinate> createVehicleDestinationPositions(int numvehicles) throws PWCGException 
    {
        double spacing = 15.0;
        Coordinate destination = MathUtils.calcNextCoord(
                pwcgGroundUnitInformation.getCampaignMap(), pwcgGroundUnitInformation.getDestination(), pwcgGroundUnitInformation.getOrientation().getyOri(), 2000.0);
        return createWedgeVehiclePositions(destination, numvehicles, spacing);
    }

    private void addAspects(List<Coordinate> destinations) throws PWCGException
    {       
        super.addDirectFireAspect();
        
        int unitSpeed = 4;
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_OFF_ROAD);
    }
}	

