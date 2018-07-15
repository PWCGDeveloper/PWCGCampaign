package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.ground.vehicle.PillBox;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
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
        spawningVehicle = new PillBox(pwcgGroundUnitInformation.getCountry());
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

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        // How many units
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            minRequested = 1;
            maxRequested = 1;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            minRequested = 1;
            maxRequested = 2;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            minRequested = 2;
            maxRequested = 3;
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
