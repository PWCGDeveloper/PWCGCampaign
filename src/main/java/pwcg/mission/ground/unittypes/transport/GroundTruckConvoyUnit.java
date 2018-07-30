package pwcg.mission.ground.unittypes.transport;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.factory.VehicleFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundMovingUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuWaypoint;

public class GroundTruckConvoyUnit extends GroundMovingUnit
{
    private String groupName = "Trucks";

    public GroundTruckConvoyUnit(GroundUnitInformation pwcgGroundUnitInformation) 
    {
        super(pwcgGroundUnitInformation);
        unitSpeed = 10;
    }

    @Override
    public void createUnits() throws PWCGException  
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        IVehicle truck = vehicleFactory.createCargoTruck(pwcgGroundUnitInformation.getCountry());

        truck.setOrientation(new Orientation());
        truck.setPosition(pwcgGroundUnitInformation.getPosition().copy());         
        truck.populateEntity();
        truck.getEntity().setEnabled(1);
        this.spawningVehicle = truck;
    }

    @Override
    public void createSpawners() throws PWCGException  
    {
        // How many trucks
        int numvehicles = calcNumUnits();

        // Place opposite of movement
        double placementOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 180);       
        
        // Get the position of the first truck - off of the bridge
        Coordinate truckCoords = getFirstTruckPosition(placementOrientation);

        for (int i = 0; i < numvehicles; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Truck Spawn " + (i + 1));      
            spawn.setDesc("Truck Spawn " + (i + 1));
            spawn.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
            spawn.setPosition(truckCoords.copy()); 

            spawners.add(spawn);

            // Calculate the  next truck position
            truckCoords = MathUtils.calcNextCoord(truckCoords.copy(), placementOrientation, 15.0);
        }       
    }

    private Coordinate getFirstTruckPosition(double placementOrientation) throws PWCGException
    {
        Coordinate firstTruckCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition().copy(), placementOrientation, 250.0);
        
        return firstTruckCoords;
    }

    protected int calcNumUnits()
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            setMinMaxRequested(1, 2);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            setMinMaxRequested(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(3, 6);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(4, 8);
        }
        
        return calculateForMinMaxRequested();
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {       
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            writer.write("  Name = \"" + groupName + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"" + groupName + "\";");
            writer.newLine();

            pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);

            spawnTimer.write(writer);
            spawningVehicle.write(writer);
            for (McuSpawn spawn : spawners)
            {
                spawn.write(writer);
            }

            waypointTimer.write(writer);

            for (McuWaypoint waypoint : waypoints)
            {
                waypoint.write(writer);
            }

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

}	

