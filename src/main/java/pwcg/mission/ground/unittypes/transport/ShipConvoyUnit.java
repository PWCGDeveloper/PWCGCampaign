package pwcg.mission.ground.unittypes.transport;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.VehicleFactory;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.ground.unittypes.GroundMovingDirectFireUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class ShipConvoyUnit extends GroundMovingDirectFireUnit
{
    public enum ShipConvoyTypes
    {
        SUBMARINE,
        WARSHIP,
        MERCHANT
    };
    
    private ShipConvoyTypes shipConvoyType = ShipConvoyTypes.MERCHANT;
    private Campaign campaign;

	public ShipConvoyUnit(Campaign campaign, ShipConvoyTypes shipConvoyType) 
	{
        super(TacticalTarget.TARGET_SHIPPING);
        unitSpeed = 6;
        this.shipConvoyType = shipConvoyType;
        this.campaign = campaign;
	}

    public void initialize (MissionBeginUnit missionBeginUnit, Coordinate startCoords, ICountry country) throws PWCGException 
	{
        String name = createUnitName();
        
        // Pick a random spot 50 KM away.  We're not going to make 50KM in a mission.
        // A target coord just gets them  moving.
        int angle = RandomNumberGenerator.getRandom(360);
        Coordinate endCoords = MathUtils.calcNextCoord(startCoords, angle, 50000);
                        
        super.initialize(missionBeginUnit, name, startCoords,  endCoords, country);
	}

    private String createUnitName()
    {
        if (shipConvoyType == ShipConvoyTypes.SUBMARINE)
        {
            return "Submarine";
        }
        
        if (shipConvoyType == ShipConvoyTypes.WARSHIP)
        {
            return "Warship";
        }

        return "Merchant";
    }

    @Override
    protected void createTargetWaypoint() throws PWCGException  
    {
        McuWaypoint waypoint = WaypointFactory.createMoveToWaypointType();
        waypoint.setTriggerArea(0);
        waypoint.setDesc(name + " WP");
        waypoint.setSpeed(unitSpeed);

        double angle = MathUtils.calcAngle(position, destinationCoords);
        Coordinate firstPosition = MathUtils.calcNextCoord(position, angle, 2000.0);
        
        // Try to keep subs on the surface
        if (shipConvoyType == ShipConvoyTypes.SUBMARINE)
        {
            firstPosition.setYPos(0.5);
        }
        waypoint.setPosition(firstPosition);
        
        waypoint.setTargetWaypoint(true);
        waypoints.add(waypoint);

        // Now the second, longer leg
        McuWaypoint waypoint2 = waypoint.copy();
        waypoint2.setPosition(destinationCoords.copy());
        waypoint.setTargetWaypoint(true);
        waypoint.getPosition().setYPos(0.0);
        if (shipConvoyType == ShipConvoyTypes.SUBMARINE)
        {
            waypoint.getPosition().setYPos(-5.0);
        }
        waypoints.add(waypoint2);
        
        waypointTimer = new McuTimer();
        waypointTimer.setName("WP Timer for " + name);
        waypointTimer.setDesc("WP for " + name);
        waypointTimer.setPosition(position.copy());
    }

    public Coordinate createDestinationPosition (Coordinate startCoords) throws PWCGException 
    {
        int angle = RandomNumberGenerator.getRandom(360);
        Coordinate targetCoords = MathUtils.calcNextCoord(startCoords, angle, 30000.0);
        
        return targetCoords;
    }

	protected void createUnits() throws PWCGException  
	{
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        IVehicle ship = vehicleFactory.createShip(country, shipConvoyType);

        ship.setOrientation(new Orientation());
        ship.setPosition(position.copy());         
        ship.populateEntity();
        ship.getEntity().setEnabled(1);

        this.spawningVehicle = ship;
	}
	
    @Override
    protected void createSpawners() throws PWCGException 
    {        
        // How many ships
        int numShips = calcNumUnits();
                
        // Towards the destination
        double shipMovementOrient = MathUtils.calcAngle(position, destinationCoords);
        Orientation shipOrient = new Orientation();
        shipOrient.setyOri(shipMovementOrient);
        
        // Offset 70 degrees from the previous ship
        double placementOrientation = MathUtils.adjustAngle (shipMovementOrient, -70);      
        Coordinate shipCoords = position.copy();

        for (int i = 0; i < numShips; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Ship Spawn " + (i + 1));      
            spawn.setDesc("Ship Spawn " + (i + 1));
            spawn.setOrientation(shipOrient.copy());
            spawn.setPosition(shipCoords.copy()); 

            spawners.add(spawn);

            // Calculate the  next infantry position
            shipCoords = MathUtils.calcNextCoord(shipCoords, placementOrientation, 1000.0);
        }       
    }

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        // How many ships
        if (shipConvoyType != ShipConvoyTypes.SUBMARINE)
        {
            int randomShips = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.RandomShipsKey);

            minRequested = 3;
            maxRequested = 3 + randomShips;
        }
        else
        {
            minRequested = 1;
            maxRequested = 1;
        }
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {       
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"Ships\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Ships\";");
            writer.newLine();

            missionBeginUnit.write(writer);

            // This could happen if the user did not install 3rd party infantry
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

    public ShipConvoyTypes getShipConvoyType()
    {
        return this.shipConvoyType;
    }
}	

