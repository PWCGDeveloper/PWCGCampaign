package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.ww1.ground.vehicle.PillBox;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundDirectFireUnit;
import pwcg.mission.mcu.McuSpawn;

public class GroundPillBoxUnit extends GroundDirectFireUnit
{

	public GroundPillBoxUnit(GroundUnitInformation pwcgGroundUnitInformation) 
	{
	    super (pwcgGroundUnitInformation);
	}	

	protected void createUnits() throws PWCGException  
	{
        spawningVehicle = new PillBox();
        spawningVehicle.makeRandomVehicleFromSet(pwcgGroundUnitInformation.getCountry());

        spawningVehicle.setOrientation(new Orientation());
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());         
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
	}	

    public void createSpawners() throws PWCGException  
    {
        int numPillBox = calcNumUnits();

        Coordinate pillBoxCoords = pwcgGroundUnitInformation.getPosition().copy();

        // Locate the infantry such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 270);             
        double pillBoxSpacing = 1000 / numPillBox;
        
        // The +20 prevents the center pillbox from spawning on top of the flare pilllbox
        pillBoxCoords = MathUtils.calcNextCoord(pillBoxCoords, startLocationOrientation, ((numPillBox * pillBoxSpacing) / 2) + 20);       
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 90.0);        

        for (int i = 0; i < numPillBox; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Pillbox Spawn " + (i + 1));      
            spawn.setDesc("Pillbox Spawn " + (i + 1));
            spawn.setPosition(pillBoxCoords);

            spawners.add(spawn);

            // Calculate the  next gun position
            pillBoxCoords = MathUtils.calcNextCoord(pillBoxCoords, placementOrientation, pillBoxSpacing);
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
            setMinMaxRequested(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(1, 2);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(2, 3);
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
            
            writer.write("  Name = \"Pillbox\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Pillbox\";");
            writer.newLine();

            pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);

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
