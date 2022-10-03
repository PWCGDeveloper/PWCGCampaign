package pwcg.mission.ground.unittypes.artillery;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

public class RadarUnit extends GroundUnit
{
    public RadarUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Radar, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException 
    {
        super.createSpawnTimer();
        List<Coordinate> vehicleStartPositions = createVehicleStartPositions();
        super.createVehicles(vehicleStartPositions);
        super.linkElements();
    }

    protected List<Coordinate> createVehicleStartPositions() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();
        
        Coordinate spawnPosition = pwcgGroundUnitInformation.getPosition().copy();
        spawnerLocations.add(spawnPosition);
        return spawnerLocations;
    }

    protected int calcNumUnits() throws PWCGException
    {
        return 1;
    }

}
