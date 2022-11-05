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
        double spacing = 100.0;
        int numATGun = calcNumUnits();
        Coordinate atGunCoords = calculateATGunCentralCoordinate();
        return createLineAcrossVehiclePositions(atGunCoords.copy(), numATGun, spacing);        
	}	
    
    private Coordinate calculateATGunCentralCoordinate() throws PWCGException
    {
        double atGunFacingAngle = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());

        // AT Guns are behind the lines
        double initialPlacementAngle = MathUtils.adjustAngle (atGunFacingAngle, 180.0);      
        Coordinate atGunCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getCampaignMap(), pwcgGroundUnitInformation.getPosition(), initialPlacementAngle, 25.0);
        return atGunCoords;
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
