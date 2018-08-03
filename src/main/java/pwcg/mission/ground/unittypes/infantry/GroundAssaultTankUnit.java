package pwcg.mission.ground.unittypes.infantry;

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
import pwcg.mission.ground.unittypes.GroundMovingDirectFireUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAssaultTankUnit extends GroundMovingDirectFireUnit
{
    public GroundAssaultTankUnit(GroundUnitInformation pwcgGroundUnitInformation) 
    {       
        super(pwcgGroundUnitInformation);
        this.unitSpeed = 6;
    }

    protected void createUnits() throws PWCGException 
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        IVehicle tank = vehicleFactory.createTank(pwcgGroundUnitInformation.getCountry());

        tank.setOrientation(new Orientation());
        tank.setPosition(pwcgGroundUnitInformation.getPosition().copy());           
        tank.populateEntity();
        tank.getEntity().setEnabled(1);
        
        this.spawningVehicle = tank;

    }

	protected void createSpawners() throws PWCGException   
	{
        int numTanks = calcNumUnits();

        // Face towards enemy
        double tankFacingAngle = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());
        Orientation tankOrient = new Orientation();
        tankOrient.setyOri(tankFacingAngle);
        
        // Locate the tanks such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (tankFacingAngle, 270);             
        double tankSpacing = 75.0;
        Coordinate tankCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), startLocationOrientation, ((numTanks * tankSpacing) / 2));       
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (tankFacingAngle, 90.0);        
        
        // Create the units
		for (int i = 0; i < numTanks; ++i)
		{	
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Tank Spawn " + (i + 1));      
            spawn.setDesc("Tank Spawn " + (i + 1));
            spawn.setOrientation(tankOrient.copy());
            spawn.setPosition(tankCoords.copy()); 

            spawners.add(spawn);

			// Calculate the  next tank position
			tankCoords = MathUtils.calcNextCoord(tankCoords, placementOrientation, tankSpacing);
		}		
	}

    protected int calcNumUnits()
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            setMinMaxRequested(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            setMinMaxRequested(3, 5);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(4, 7);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(6, 10);
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
            
            writer.write("  Name = \"Tanks\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Tanks\";");
            writer.newLine();
    
            pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);
    
            spawnTimer.write(writer);
            spawningVehicle.write(writer);
            for (McuSpawn spawn : spawners)
            {
                spawn.write(writer);
            }
    
            if (attackTimer != null)
            {
                attackTimer.write(writer);
                attackEntity.write(writer);
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

