package pwcg.mission.ground.unittypes.infantry;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundAntiTankArtillery extends GroundUnit
{
    public GroundAntiTankArtillery(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.ArtilleryAntiTank, pwcgGroundUnitInformation);
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

    private List<Coordinate> createVehicleStartPositions() throws PWCGException 
	{
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numatGun = calcNumUnits();

		// Face towards enemy
		double atGunFacingAngle = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());
		Orientation atGunOrient = new Orientation();
		atGunOrient.setyOri(atGunFacingAngle);
		
        // MGs are behind the lines
        double initialPlacementAngle = MathUtils.adjustAngle (atGunFacingAngle, 180.0);      
        Coordinate atGunCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), initialPlacementAngle, 25.0);

        // Locate the target such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (atGunFacingAngle, 270);             
        double machingGunSpacing = 100.0;
        atGunCoords = MathUtils.calcNextCoord(atGunCoords, startLocationOrientation, ((numatGun * machingGunSpacing) / 2));       
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (atGunFacingAngle, 90.0);        

        for (int i = 0; i < numatGun; ++i)
        {   
            spawnerLocations.add(atGunCoords);
            atGunCoords = MathUtils.calcNextCoord(atGunCoords, placementOrientation, machingGunSpacing);
        }
        return spawnerLocations;       
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
            return GroundUnitNumberCalculator.calcNumUnits(2, 3);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 4);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    private void addAspects() throws PWCGException
    {
        super.addDirectFireAspect();
    }
}	
