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
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.GroundMovingUnit;
import pwcg.mission.ground.vehicle.ITrainLocomotive;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuWaypoint;

public class GroundTrainUnit extends GroundMovingUnit
{
    private Campaign campaign;

	public GroundTrainUnit (Campaign campaign) 
	{
        super(TacticalTarget.TARGET_TRAIN);
        this.campaign = campaign;
        unitSpeed = 12;
	}

     public void initialize (
                     MissionBeginUnitCheckZone missionBeginUnit, 
                     Coordinate location, 
                     Coordinate destinationCoords, 
                     ICountry country)
     {
         String name = "Train";
         super.initialize(missionBeginUnit, name, location,  destinationCoords, country);
     }

	protected void createUnits() throws PWCGException 
	{		
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        ITrainLocomotive locomotive = makeLocomotive(vehicleFactory);
        makeCars(vehicleFactory, locomotive);
		this.spawningVehicle = locomotive;
	}

	private ITrainLocomotive makeLocomotive(IVehicleFactory vehicleFactory)
	{
		ITrainLocomotive locomotive = vehicleFactory.createTrainLocomotive(country);

        locomotive.setPosition(position.copy());
		locomotive.setOrientation(new Orientation());
		locomotive.populateEntity();
		locomotive.getEntity().setEnabled(1);
		return locomotive;
	}

	private void makeCars(IVehicleFactory vehicleFactory, ITrainLocomotive locomotive)
	        throws PWCGException, PWCGException
	{
		int randomTrainCars = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.RandomTrainsKey);
		int numCars = 4 + RandomNumberGenerator.getRandom(randomTrainCars);

		for (int i = 0; i < numCars; ++i)
		{	
	        IVehicle car = vehicleFactory.createTrainCar(country);
			locomotive.addCar(car);
		}
	}

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        minRequested = 1;
        maxRequested = 1;
    }

    @Override
    protected void createSpawners() 
    {        
        McuSpawn spawn = new McuSpawn();
        spawn.setName("Train Spawn");      
        spawn.setDesc("Train Spawn");
        spawn.setOrientation(new Orientation());
        spawn.setPosition(position.copy()); 

        spawners.add(spawn);
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {       
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"Train\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Train\";");
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

