package pwcg.mission.ground.unittypes.transport;

import java.util.Arrays;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

public class LandingCraftUnit extends GroundUnit
{
    GroundUnitInformation groundUnitInformation;
    
    public LandingCraftUnit(GroundUnitInformation groundUnitInformation)
    {
        super(VehicleClass.ShipLandingCraft, groundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException 
    {
        super.createSpawnTimer();
        List<Coordinate> vehicleStartPositions = Arrays.asList(pwcgGroundUnitInformation.getPosition());
        super.createVehicles(vehicleStartPositions);
        List<Coordinate> destinations =  Arrays.asList(pwcgGroundUnitInformation.getDestination());
        addAspects(destinations);
        super.linkElements();
    }

    private void addAspects(List<Coordinate> destinations) throws PWCGException
    {       
        int unitSpeed = 5;
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_ON_WAYPOINT);
    }
}	

