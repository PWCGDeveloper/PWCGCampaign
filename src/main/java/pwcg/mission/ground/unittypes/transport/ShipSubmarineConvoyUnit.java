package pwcg.mission.ground.unittypes.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundElementFactory;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.IGroundElement;
import pwcg.mission.ground.vehicle.VehicleClass;

public class ShipSubmarineConvoyUnit extends GroundUnit
{
    public ShipSubmarineConvoyUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Submarine, pwcgGroundUnitInformation);
    }   

    @Override
    protected void addElements()
    {       
        int unitSpeed = 5;
        IGroundElement movement = GroundElementFactory.createGroundElementMovement(pwcgGroundUnitInformation, vehicle, unitSpeed);
        this.addGroundElement(movement);        
    }

    @Override
    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numShips = calcNumUnits();
        
        double shipMovementOrient = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());
        Orientation shipOrient = new Orientation();
        shipOrient.setyOri(shipMovementOrient);
        
        double placementOrientation = MathUtils.adjustAngle (shipMovementOrient, -70);      
        Coordinate shipCoords = pwcgGroundUnitInformation.getPosition().copy();

        for (int i = 0; i < numShips; ++i)
        {   
            spawnerLocations.add(shipCoords);
            shipCoords = MathUtils.calcNextCoord(shipCoords, placementOrientation, 1000.0);
        }       
        return spawnerLocations;        
    }

    protected int calcNumUnits() throws PWCGException
    {
        return 1;
    }
}	

