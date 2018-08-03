package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.ww1.ground.vehicle.Infantry;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundDirectFireUnit;
import pwcg.mission.mcu.McuSpawn;

public class GroundInfantryUnit extends GroundDirectFireUnit
{

    public GroundInfantryUnit(GroundUnitInformation pwcgGroundUnitInformation) 
    {
        super(pwcgGroundUnitInformation);
    }

    public void createUnits() throws PWCGException  
    {
        spawningVehicle = new Infantry(pwcgGroundUnitInformation.getCountry());
        if (!spawningVehicle.vehicleExists())
        {
            Logger.log (LogLevel.DEBUG, "No infantry model or script found.  Download and install 3rd party objects");
        }
        
        spawningVehicle.setOrientation(new Orientation());
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());         
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
    }


    public void createSpawners() throws PWCGException  
    {
        int numInfantry = calcNumUnits();

        // Space defensive infantry a bit further that assaulting infantry
        double infantrySpacing = 40.0;

        // Move towards enemy
        double infantryFacingAngle = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());
        Orientation infantryOrient = new Orientation();
        infantryOrient.setyOri(infantryFacingAngle);

        // Locate the infantry such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (infantryFacingAngle, 270);     
        Coordinate infantryBaseCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), startLocationOrientation, ((numInfantry * infantrySpacing) / 2));       

        // Direction in which subsequent units will be placed
        double placementLineOrientation = MathUtils.adjustAngle (infantryFacingAngle, 90);     

        for (int i = 0; i < numInfantry; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Infantry Spawn " + (i + 1));      
            spawn.setDesc("Infantry Spawn " + (i + 1));

            double placementOffsetOrientation = MathUtils.calcAngle(pwcgGroundUnitInformation.getDestination(), pwcgGroundUnitInformation.getPosition());
            int placementOffsetDistance = RandomNumberGenerator.getRandom(150);
            Coordinate infantryCoords = MathUtils.calcNextCoord(infantryBaseCoords, placementOffsetOrientation, placementOffsetDistance);

            spawn.setPosition(infantryCoords);

            spawners.add(spawn);

            // Calculate the  next gun position
            infantryBaseCoords = MathUtils.calcNextCoord(infantryBaseCoords, placementLineOrientation, infantrySpacing);
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
            setMinMaxRequested(3, 6);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(4, 8);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(6, 10);
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
            
            writer.write("  Name = \"Infantry\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Infantry\";");
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

