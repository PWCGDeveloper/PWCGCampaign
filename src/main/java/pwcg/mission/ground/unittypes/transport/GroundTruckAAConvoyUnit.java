package pwcg.mission.ground.unittypes.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundAspectFactory;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.org.IGroundAspect;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.AttackAreaType;

public class GroundTruckAAConvoyUnit extends GroundUnit
{
    static private int TRUCK_AA_ATTACK_AREA = 20000;

    public GroundTruckAAConvoyUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.TruckAAA, pwcgGroundUnitInformation);
    }   

    @Override
    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();
        
        int numvehicles = calcNumUnits();

        // Place opposite of movement
        double placementOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 180);       
        
        Coordinate truckCoords = getFirstTruckPosition(placementOrientation);
        for (int i = 0; i < numvehicles; ++i)
        {   
            spawnerLocations.add(truckCoords);
            truckCoords = MathUtils.calcNextCoord(truckCoords.copy(), placementOrientation, 15.0);
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

    private Coordinate getFirstTruckPosition(double placementOrientation) throws PWCGException
    {
        Coordinate firstTruckCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition().copy(), placementOrientation, 500.0);
        return firstTruckCoords;
    }

    @Override
    protected void addAspects() throws PWCGException
    {       
        IGroundAspect areaFire = GroundAspectFactory.createGroundAspectAreaFire(pwcgGroundUnitInformation, pwcgGroundUnitInformation.getPosition(), vehicle, AttackAreaType.AIR_TARGETS, TRUCK_AA_ATTACK_AREA);
        this.addGroundElement(areaFire);         

        int unitSpeed = 10;
        IGroundAspect movement = GroundAspectFactory.createGroundAspectMovement(pwcgGroundUnitInformation, vehicle, unitSpeed);
        this.addGroundElement(movement);         
    }
}	

