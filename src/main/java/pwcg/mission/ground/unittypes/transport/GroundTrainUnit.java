package pwcg.mission.ground.unittypes.transport;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundMovingUnit;
import pwcg.mission.ground.vehicle.ITrainLocomotive;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuWaypoint;

public class GroundTrainUnit extends GroundMovingUnit
{
	public GroundTrainUnit (GroundUnitInformation pwcgGroundUnitInformation) 
	{
	    super(pwcgGroundUnitInformation);
        unitSpeed = 12;
	}

	protected void createUnits() throws PWCGException 
	{		
	    ITrainLocomotive locomotive = makeLocomotive();
        makeCoalCar(locomotive);
        makeAAACar(locomotive);
        makeCars(locomotive);
        makeAAACar(locomotive);
		this.spawningVehicle = locomotive;
	}

    private void makeCoalCar(ITrainLocomotive locomotive) throws PWCGException
    {
        IVehicle coalCar = VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.TrainCoalCar);
        locomotive.addCar(coalCar);
    }

    private void makeAAACar(ITrainLocomotive locomotive) throws PWCGException
    {
        IVehicle aaaCar = VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.TrainCarAAA);
        locomotive.addCar(aaaCar);
    }

    private ITrainLocomotive makeLocomotive() throws PWCGException
	{
        ITrainLocomotive locomotive = VehicleFactory.createLocomotive(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate());

        locomotive.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		locomotive.setOrientation(new Orientation());
		locomotive.getEntity().setEnabled(1);
		return locomotive;
	}

	private void makeCars(ITrainLocomotive locomotive)
	        throws PWCGException, PWCGException
	{
		int numCars = calcNumCars();
		for (int i = 0; i < numCars; ++i)
		{	
	        IVehicle car = VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.TrainCar);
			locomotive.addCar(car);
		}
	}

    protected int calcNumUnits()
    {
        setMinMaxRequested(1, 1);        
        return calculateForMinMaxRequested();
    }

    private int calcNumCars()
    {
        int baseTrainCars = 3;
        int randomTrainCars = 3;

        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            baseTrainCars = 1;
            randomTrainCars = 1;
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            baseTrainCars = 3;
            randomTrainCars = 3;
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            baseTrainCars = 4;
            randomTrainCars = 5;
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            baseTrainCars = 5;
            randomTrainCars = 7;
        }
        
        int numUnits = baseTrainCars + (RandomNumberGenerator.getRandom(randomTrainCars - baseTrainCars));
        return numUnits;
    }

    @Override
    protected void createSpawners() 
    {        
        McuSpawn spawn = new McuSpawn();
        spawn.setName("Train Spawn");      
        spawn.setDesc("Train Spawn");
        spawn.setOrientation(new Orientation());
        spawn.setPosition(pwcgGroundUnitInformation.getPosition().copy()); 

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

