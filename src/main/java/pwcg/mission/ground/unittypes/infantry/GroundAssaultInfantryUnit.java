package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.ww1.ground.vehicle.MovingInfantry;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundMovingDirectFireUnit;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAssaultInfantryUnit extends GroundMovingDirectFireUnit
{    
	public GroundAssaultInfantryUnit(GroundUnitInformation pwcgGroundUnitInformation) 
	{	    
	    super(pwcgGroundUnitInformation);
        this.unitSpeed = 3;
	}

	protected void createUnits() throws PWCGException  
	{
	    MovingInfantry infantry = new MovingInfantry();
	    infantry.makeRandomVehicleFromSet(pwcgGroundUnitInformation.getCountry());
        if (!infantry.vehicleExists())
        {
            Logger.log (LogLevel.DEBUG, "No infantry model or script found.  Download and install 3rd party objects");
        }

		infantry.setOrientation(new Orientation());
		infantry.setPosition(pwcgGroundUnitInformation.getPosition().copy());			
		infantry.populateEntity();
		infantry.getEntity().setEnabled(1);
		
		this.spawningVehicle = infantry;
	}

    @Override
    protected void createSpawners() throws PWCGException 
    {        
        int numInfantry = calcNumUnits();
        
        double infantrySpacing = 20.0;
                
        // Move towards enemy
        double infantryFacingAngle = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());
        Orientation infantryOrient = new Orientation();
        infantryOrient.setyOri(infantryFacingAngle);
        
        // Locate the infantry such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (infantryFacingAngle, 270);     
        Coordinate infantryCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), startLocationOrientation, ((numInfantry * infantrySpacing) / 2));
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (infantryFacingAngle, 90);     

        for (int i = 0; i < numInfantry; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Infantry Spawn " + (i + 1));      
            spawn.setDesc("Infantry Spawn " + (i + 1));
            spawn.setOrientation(infantryOrient.copy());
            spawn.setPosition(infantryCoords.copy()); 

            spawners.add(spawn);

            // Calculate the  next infantry position
            infantryCoords = MathUtils.calcNextCoord(infantryCoords, placementOrientation, infantrySpacing);
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
            setMinMaxRequested(4, 6);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(6, 12);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(8, 16);
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
            
            writer.write("  Name = \"Assaulting Infantry\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Assaulting Infantry\";");
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

