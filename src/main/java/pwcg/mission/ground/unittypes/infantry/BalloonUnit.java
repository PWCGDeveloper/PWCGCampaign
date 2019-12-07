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
        super.createGroundUnit();
        createWinch();
    }

    private void createWinch() throws PWCGException
    {
        winchUnit = new BalloonWinch(this);
        winchUnit.createWinchUnit();
    }

    @Override
    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
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

    protected void addElements()
    {       
        // No balloon elements
    }
}	

