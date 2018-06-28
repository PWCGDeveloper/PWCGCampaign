package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.ww1.ground.vehicle.PillBox;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.GroundDirectFireUnit;
import pwcg.mission.mcu.McuFlare;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.group.FlareSequence;

public class GroundPillBoxFlareUnit extends GroundDirectFireUnit
{
    private FlareSequence flares = new FlareSequence();

	public GroundPillBoxFlareUnit() 
	{
	    super (TacticalTarget.TARGET_DEFENSE);
	}	

    public void initialize (MissionBeginUnitCheckZone missionBeginUnit, Coordinate position, Coordinate destinationCoords, ICountry country) 
    {
        String nationality = country.getNationality();
        name = nationality + " Pillbox";
        
        super.initialize (missionBeginUnit, name, position, destinationCoords, country);
    }

	protected void createUnits() throws PWCGException  
	{
		// Face towards enemy
		double pillBoxFacingAngle = MathUtils.calcAngle(position, destinationCoords);
		Orientation pillBoxOrient = new Orientation();
		pillBoxOrient.setyOri(pillBoxFacingAngle);
		
        Coordinate pillBoxCoords = position.copy();
        
        PillBox pillBox = new PillBox(country);
        
        pillBox.setOrientation(pillBoxOrient.copy());
        pillBox.setPosition(pillBoxCoords.copy());           
        pillBox.populateEntity();
        pillBox.getEntity().setEnabled(1);

       this.spawningVehicle = pillBox;

       createFlares();
	}

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        minRequested = 1;
        maxRequested = 2;
    }

    protected void createSpawners() throws PWCGException  
    {
        // Face towards enemy
        double pillBoxFacingAngle = MathUtils.calcAngle(position, destinationCoords);
        Orientation pillBoxOrient = new Orientation();
        pillBoxOrient.setyOri(pillBoxFacingAngle);
        
        McuSpawn spawn = new McuSpawn();
        spawn.setName("Pillbox Spawn");      
        spawn.setDesc("Pillbox Spawn");
        spawn.setPosition(position.copy());

        spawners.add(spawn);
    }   

    public void createFlares() throws PWCGException 
    {
        int flareColor = McuFlare.FLARE_COLOR_RED;        
        if (country.getSide() == Side.ALLIED)
        {
            flareColor = McuFlare.FLARE_COLOR_GREEN;
        }
        
        // Pop flares for friendly planes        
        flares = new FlareSequence();
        flares.setFlare(position.copy(), flareColor, spawningVehicle.getEntity().getIndex());
    }

    /**
     * Write the mission to a file
     * 
     * @param writer
     * @throws PWCGException 
     * @
     */
    public void write(BufferedWriter writer) throws PWCGException 
    {       
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"Pillbox With Flares\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Pillbox With Flares\";");
            writer.newLine();

            missionBeginUnit.write(writer);

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
            
            flares.write(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }


    /**
     * @return
     */
    public FlareSequence getFlares()
    {
        return flares;
    }

    
}	
