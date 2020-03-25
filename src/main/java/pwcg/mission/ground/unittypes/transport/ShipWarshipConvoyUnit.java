package pwcg.mission.ground.unittypes.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundAspectFactory;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.org.IGroundAspect;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.AttackAreaType;

public class ShipWarshipConvoyUnit extends GroundUnit
{
    static private int WARSHIP_ATTACK_AREA = 20000;

    public ShipWarshipConvoyUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.ShipWarship, pwcgGroundUnitInformation);
    }   

    @Override
    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numShips = calcNumUnits();
        
        double shipMovementOrient = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());
        Orientation shipOrient = new Orientation();
        shipOrient.setyOri(shipMovementOrient);
        
        double placementOrientation = MathUtils.adjustAngle (shipMovementOrient, -70);      
        Coordinate shipCoords = pwcgGroundUnitInformation.getPosition().copy();

        for (int i = 0; i < numShips; ++i)
        {   
            spawnerLocations.add(shipCoords);
            shipCoords = MathUtils.calcNextCoord(shipCoords, placementOrientation, 1000.0);
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
            return GroundUnitNumberCalculator.calcNumUnits(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(3, 5);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    @Override
    protected void addAspects() throws PWCGException
    {       
        IGroundAspect areaFire = GroundAspectFactory.createGroundAspectAreaFire(pwcgGroundUnitInformation, pwcgGroundUnitInformation.getPosition(), vehicle, AttackAreaType.AIR_TARGETS, WARSHIP_ATTACK_AREA);
        this.addGroundElement(areaFire);
        
        IGroundAspect directFire = GroundAspectFactory.createGroundAspectDirectFire(pwcgGroundUnitInformation, vehicle);
        this.addGroundElement(directFire);         

        int unitSpeed = 5;
        IGroundAspect movement = GroundAspectFactory.createGroundAspectMovement(pwcgGroundUnitInformation, vehicle, unitSpeed);
        this.addGroundElement(movement);        
    }
}	

