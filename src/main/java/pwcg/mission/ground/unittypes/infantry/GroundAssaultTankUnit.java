package pwcg.mission.ground.unittypes.infantry;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundElementFactory;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.org.IGroundElement;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundAssaultTankUnit extends GroundUnit
{
    public GroundAssaultTankUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Tank, pwcgGroundUnitInformation);
    }   

    @Override
    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();
        int numTanks = calcNumUnits();

        // Face towards enemy
        double tankFacingAngle = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());
        Orientation tankOrient = new Orientation();
        tankOrient.setyOri(tankFacingAngle);
        
        // Locate the tanks such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (tankFacingAngle, 270);             
        double tankSpacing = 75.0;
        Coordinate tankCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), startLocationOrientation, ((numTanks * tankSpacing) / 2));       
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (tankFacingAngle, 90.0);        
        
		for (int i = 0; i < numTanks; ++i)
		{	
            spawnerLocations.add(tankCoords);
			tankCoords = MathUtils.calcNextCoord(tankCoords, placementOrientation, tankSpacing);
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
            return GroundUnitNumberCalculator.calcNumUnits(3, 5);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(4, 7);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(6, 10);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    @Override
    protected void addElements() throws PWCGException
    {       
        IGroundElement areaFire = GroundElementFactory.createGroundElementDirectFire(pwcgGroundUnitInformation, vehicle);
        this.addGroundElement(areaFire);         

        int unitSpeed = 6;
        IGroundElement movement = GroundElementFactory.createGroundElementMovement(pwcgGroundUnitInformation, vehicle, unitSpeed);
        this.addGroundElement(movement);         
    }
}	

