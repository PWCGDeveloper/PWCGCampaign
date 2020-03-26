package pwcg.mission.ground.unittypes.artillery;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundAspectAreaFire;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitElement;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.org.IGroundAspect;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundArtilleryBattery extends GroundUnit
{
    public GroundArtilleryBattery(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.ArtilleryHowitzer, pwcgGroundUnitInformation);
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

        int numArtillery = calcNumUnits();

        double startLocationOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 270);             
        double gunSpacing = 30.0;
        Coordinate gunCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), startLocationOrientation, ((numArtillery * gunSpacing) / 2));       
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 90.0);        

        for (int i = 0; i < numArtillery; ++i)
        {   
            gunCoords = MathUtils.calcNextCoord(gunCoords, placementOrientation, gunSpacing);
            spawnerLocations.add(gunCoords);
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
            return GroundUnitNumberCalculator.calcNumUnits(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(3, 6);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(4, 8);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    private void addAspects() throws PWCGException
    {
        super.addIndirectFireAspect();
     }

    public void setTargetPosition(Coordinate targetPosition)
    {
        for (GroundUnitElement groundElement : this.getGroundElements())
        {
            for (IGroundAspect aspect : groundElement.getAspectsOfGroundUnit())
            {
                if (aspect instanceof GroundAspectAreaFire)
                {
                    GroundAspectAreaFire areaFireElement = (GroundAspectAreaFire)aspect;
                    areaFireElement.setTargetPosition(targetPosition);
                }
            }
        }
    }
}	

