package pwcg.mission.ground.unittypes.transport;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.VehicleFactory;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.GroundMovingUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuWaypoint;

public class GroundTruckConvoyUnit extends GroundMovingUnit
{
    private String groupName = "Trucks";
    private Campaign campaign;
    private Orientation orientation;

    public GroundTruckConvoyUnit(Campaign campaign) 
    {
        super(TacticalTarget.TARGET_TRANSPORT);
        this.campaign = campaign;
    }

    public void initialize (
                    MissionBeginUnitCheckZone missionBeginUnit, 
                    Coordinate startCoords, 
                    Coordinate destinationCoords, 
                    Orientation orientation, 
                    ICountry country) 
    {
        String name = "Truck Convoy";
        
        this.orientation = orientation;
        
        // Alternative coordinates because the trucks drive into the river
        Coordinate alternativeCoords = createAlternativeDestination();
        
        super.initialize(missionBeginUnit, name, startCoords,  alternativeCoords, country);
    }

    @Override
    public void createUnits() throws PWCGException  
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        IVehicle truck = vehicleFactory.createCargoTruck(country);

        truck.setOrientation(new Orientation());
        truck.setPosition(position.copy());         
        truck.populateEntity();
        truck.getEntity().setEnabled(1);
        this.spawningVehicle = truck;
    }

    @Override
    public void createSpawners() throws PWCGException  
    {
        // How many trucks
        int numvehicles = calcNumUnits();

        // Move along bridge
        Orientation truckMovementOrient = orientation.copy();

        // Place opposite of movement
        double placementOrientation = MathUtils.adjustAngle (truckMovementOrient.getyOri(), 180);       
        
        // Get the position of the first truck - off of the bridge
        Coordinate truckCoords = getFirstTruckPosition(placementOrientation);

        for (int i = 0; i < numvehicles; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Truck Spawn " + (i + 1));      
            spawn.setDesc("Truck Spawn " + (i + 1));
            spawn.setOrientation(truckMovementOrient.copy());
            spawn.setPosition(truckCoords.copy()); 

            spawners.add(spawn);

            // Calculate the  next truck position
            truckCoords = MathUtils.calcNextCoord(truckCoords.copy(), placementOrientation, 15.0);
        }       
    }


    /**
     * Because trucks drive into the river
     * 
     * @throws PWCGException 
     */
    public Coordinate createAlternativeDestination()  
    {
        Coordinate destinationCoords = position.copy();
                        
        try
        {
            // Move along bridge
            Orientation truckMovementOrient = orientation.copy();
    
            // Place opposite of movement
            double placementOrientation = MathUtils.adjustAngle (truckMovementOrient.getyOri(), 180);       
            
            // Get the position of the first truck - off of the bridge
            Coordinate truckCoords;
            truckCoords = getFirstTruckPosition(placementOrientation);

            // Get the position of the first truck - off of the bridge
            destinationCoords = MathUtils.calcNextCoord(truckCoords, truckMovementOrient.getyOri(), 100);
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }

        return destinationCoords;
    }

    private Coordinate getFirstTruckPosition(double placementOrientation) throws PWCGException
    {
        Coordinate firstTruckCoords = MathUtils.calcNextCoord(position.copy(), placementOrientation, 250.0);
        
        return firstTruckCoords;
    }

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        // How many units
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            minRequested = 3;
            maxRequested = 6;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            minRequested = 4;
            maxRequested = 8;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            minRequested = 6;
            maxRequested = 12;
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

            writer.write("  Name = \"" + groupName + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"" + groupName + "\";");
            writer.newLine();

            missionBeginUnit.write(writer);

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

