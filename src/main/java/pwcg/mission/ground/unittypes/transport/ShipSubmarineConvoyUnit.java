package pwcg.mission.ground.unittypes.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

public class ShipSubmarineConvoyUnit extends GroundUnit
{
    public ShipSubmarineConvoyUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Submarine, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException 
    {
        super.createSpawnTimer();
        List<Coordinate> vehicleStartPositions = createVehicleStartPosition();
        super.createVehicles(vehicleStartPositions);
        List<Coordinate> destinations =  createVehicleDestinationPosition();
        addAspects(destinations);
        super.linkElements();
    }

    private List<Coordinate> createVehicleStartPosition() throws PWCGException 
    {
        return createVehiclePosition(pwcgGroundUnitInformation.getPosition().copy());        
    }

    private List<Coordinate> createVehicleDestinationPosition() throws PWCGException 
    {
        return createVehiclePosition(pwcgGroundUnitInformation.getDestination().copy());        
    }

    private List<Coordinate> createVehiclePosition(Coordinate vehiclePosition) throws PWCGException
    {        
        Coordinate shipCoords = vehiclePosition.copy();
        List<Coordinate> spawnerLocations = new ArrayList<>();
        spawnerLocations.add(shipCoords);
        return spawnerLocations;        
    }

    private void addAspects(List<Coordinate> destinations) throws PWCGException
    {       
        int unitSpeed = 5;
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_ON_WAYPOINT);
    }
 }	

