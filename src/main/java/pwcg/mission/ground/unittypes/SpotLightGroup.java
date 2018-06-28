package pwcg.mission.ground.unittypes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.VehicleFactory;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;

public class SpotLightGroup extends GroundUnitSpawning
{
	protected int numGroups = 3;

	public SpotLightGroup() 
	{
		super(TacticalTarget.TARGET_NONE);
	}

	public void initialize (MissionBeginUnit missionBeginUnit, ICountry country, Coordinate groundPosition, int numGroups) 
	{
		this.numGroups = numGroups;
	
		String countryName = country.getNationality();
		String name = countryName + " Spotlight Battery";
		
        super.initialize (missionBeginUnit, name, groundPosition, groundPosition, country);
	}

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        minRequested = 1;
        maxRequested = 1;
    }

	public void createSpawners() throws PWCGException  
	{
		List<Coordinate> aaaPositionsOuter = getPositions(4000.0, 0.0);
		for (Coordinate aaaPosition : aaaPositionsOuter)
		{
		    createSpawner(aaaPosition);
		}
		
		List<Coordinate> aaaPositionsInner = getPositions(1500.0, 30.0);
		for (Coordinate aaaPosition : aaaPositionsInner)
		{
		    createSpawner(aaaPosition);
		}
	}	

    public void createSpawner(Coordinate spotlightPosition)  
    {
        McuSpawn spawn = new McuSpawn();
        spawn.setName("Spotlight");      
        spawn.setDesc("Spotlight");      
        spawn.setPosition(spotlightPosition);

        spawners.add(spawn);
    }

    public void createUnits() throws PWCGException  
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();

        spawningVehicle = vehicleFactory.createSpotLight(country);
        spawningVehicle.setOrientation(new Orientation());
        spawningVehicle.setPosition(position.copy());         
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
    }

	private List<Coordinate> getPositions(double distance, double offset) throws PWCGException 
	{
		List<Coordinate> positions = new ArrayList<Coordinate>();
		
		for (int i = 0 ; i < numGroups; ++i)
		{
			// Put the groups in a circle
			double movementHeading = ((360 / numGroups) * i) + offset;
			movementHeading = MathUtils.adjustAngle (movementHeading, 0);		
			
			Coordinate position = MathUtils.calcNextCoord(getPosition(), movementHeading, distance);
			positions.add (position);
		}
		
		return positions;
	}


    @Override
    protected void createGroundTargetAssociations() 
    {
        // MBU -> Spawn Timer
        this.missionBeginUnit.linkToMissionBegin(this.spawnTimer.getIndex());

        // Spawn Timer -> Spawns
        for (McuSpawn spawn : spawners)
        {
            spawnTimer.setTarget(spawn.getIndex());
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
            
            writer.write("  Name = \"Spot Lights\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Spot Lights\";");
            writer.newLine();
    
            missionBeginUnit.write(writer);
    
            // This could happen if the user did not install 3rd party infantry
            spawnTimer.write(writer);
            spawningVehicle.write(writer);
            for (McuSpawn spawn : spawners)
            {
                spawn.write(writer);
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

