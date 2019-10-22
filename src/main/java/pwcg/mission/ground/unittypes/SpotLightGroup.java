package pwcg.mission.ground.unittypes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;
import pwcg.mission.mcu.McuSpawn;

public class SpotLightGroup extends GroundUnitSpawning
{
	protected int numGroups = 3;

	public SpotLightGroup(GroundUnitInformation pwcgGroundUnitInformation, int numGroups) 
	{
        super(pwcgGroundUnitInformation);
        this.numGroups = numGroups;
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
        spawningVehicle = VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.SearchLight);
        spawningVehicle.setOrientation(new Orientation());
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());         
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
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
			
			Coordinate spotlightPosition = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), movementHeading, distance);
			positions.add (spotlightPosition);
		}
		
		return positions;
	}

    protected int calcNumUnits() throws PWCGException 
    {
        setMinMaxRequested(1, 1);        
        return calculateForMinMaxRequested();
    }

    @Override
    protected void createGroundTargetAssociations() 
    {
        // MBU -> Spawn Timer
        pwcgGroundUnitInformation.getMissionBeginUnit().linkToMissionBegin(this.spawnTimer.getIndex());

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
    
            pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);
    
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

