package pwcg.mission.ground.unittypes.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundElementFactory;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.IGroundElement;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundTrainUnit extends GroundUnit
{
    public GroundTrainUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.TrainLocomotive, pwcgGroundUnitInformation);
    }   

    @Override
    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();
        spawnerLocations.add(pwcgGroundUnitInformation.getPosition().copy());
        return spawnerLocations;        
    }

    @Override
    protected void addElements() throws PWCGException
    {       
        int unitSpeed = 12;
        IGroundElement movement = GroundElementFactory.createGroundElementMovement(pwcgGroundUnitInformation, vehicle, unitSpeed);
        this.addGroundElement(movement);        
    }
}	

