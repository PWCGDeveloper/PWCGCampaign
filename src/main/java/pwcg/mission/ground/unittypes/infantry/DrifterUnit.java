package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundDirectFireUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.McuSpawn;

public class DrifterUnit extends GroundDirectFireUnit
{
    public DrifterUnit(GroundUnitInformation pwcgGroundUnitInformation) throws PWCGException
    {
        super (pwcgGroundUnitInformation);        
    }   

	protected void createUnits() throws PWCGException  
	{
        spawningVehicle = pwcg.mission.ground.vehicle.VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.Drifter);
        spawningVehicle.setOrientation(new Orientation());
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());         
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
	}	

    public void createSpawners() throws PWCGException  
    {
        int numDrifter = calcNumUnits();

        // Face towards orientation
        double drifterFacingAngle = MathUtils.adjustAngle(pwcgGroundUnitInformation.getOrientation().getyOri(), 180.0);
        Orientation drifterOrient = new Orientation();
        drifterOrient.setyOri(drifterFacingAngle);
        
        Coordinate drifterCoords = pwcgGroundUnitInformation.getPosition().copy();

        double drifterSpacing = 30.0;
        
        // Direction in which subsequent units will be placed
        double placementOrientation = pwcgGroundUnitInformation.getOrientation().getyOri();        

        for (int i = 0; i < numDrifter; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Drifter Spawn " + (i + 1));      
            spawn.setDesc("Drifter Spawn " + (i + 1));
            spawn.setPosition(drifterCoords);

            spawners.add(spawn);

            // Calculate the  next gun position
            drifterCoords = MathUtils.calcNextCoord(drifterCoords, placementOrientation, drifterSpacing);
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
            setMinMaxRequested(2, 3);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(3, 6);
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
            
            writer.write("  Name = \"Drifter\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Drifter\";");
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
