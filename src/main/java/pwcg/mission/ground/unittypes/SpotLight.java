package pwcg.mission.ground.unittypes;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;
import pwcg.mission.mcu.McuSpawn;

public class SpotLight extends GroundUnitSpawning
{
	public SpotLight(GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super(pwcgGroundUnitInformation);
	}

	public void createSpawners() throws PWCGException  
	{
        createSpawner(pwcgGroundUnitInformation.getPosition());
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

    protected int calcNumUnits() throws PWCGException 
    {
        return 1;
    }

    @Override
    protected void createGroundTargetAssociations() 
    {
        pwcgGroundUnitInformation.getMissionBeginUnit().linkToMissionBegin(this.spawnTimer.getIndex());
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

