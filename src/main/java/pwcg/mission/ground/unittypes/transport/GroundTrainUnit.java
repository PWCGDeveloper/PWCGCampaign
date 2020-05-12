package pwcg.mission.ground.unittypes.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundTrainUnit extends GroundUnit
{
    public GroundTrainUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.TrainLocomotive, pwcgGroundUnitInformation);
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

    protected List<Coordinate> createVehicleStartPositions() throws PWCGException 
    {
        List<Coordinate> vehicleStartPositions = new ArrayList<>();
        vehicleStartPositions.add(pwcgGroundUnitInformation.getPosition().copy());
        return vehicleStartPositions;        
    }

    protected void addAspects() throws PWCGException
    {        
        int unitSpeed = 12;
        List<Coordinate> destinations = new ArrayList<>();
        destinations.add(pwcgGroundUnitInformation.getDestination());
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_ON_WAYPOINT);
    }
}	

