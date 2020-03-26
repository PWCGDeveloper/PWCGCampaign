package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

public class BalloonUnit extends GroundUnit
{
    private BalloonWinch winchUnit;
    
    public BalloonUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Balloon, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createSpawnTimer();
        List<Coordinate> vehicleStartPositions = createVehicleStartPositions();
        super.createVehicles(vehicleStartPositions);
        super.linkElements();
        createWinch();
    }

    private void createWinch() throws PWCGException
    {
        winchUnit = new BalloonWinch(this, pwcgGroundUnitInformation);
        winchUnit.createWinchUnit();
    }

    protected List<Coordinate> createVehicleStartPositions() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();        
        Coordinate balloonCoords = pwcgGroundUnitInformation.getPosition().copy();
        spawnerLocations.add(balloonCoords);
        return spawnerLocations;        
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {       
        super.write(writer);
        winchUnit.write(writer);
    }
}	

