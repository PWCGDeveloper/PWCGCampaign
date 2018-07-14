package pwcg.mission.ground.unittypes.transport;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.ww1.ground.vehicle.Drifter;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.unittypes.GroundMovingDirectFireUnit;
import pwcg.mission.mcu.McuSpawn;

public class GroundBargeConvoyUnit extends GroundMovingDirectFireUnit
{
    private Orientation orientation;

    public GroundBargeConvoyUnit(boolean isPlayerFlight) 
    {
        super (TacticalTarget.TARGET_DRIFTER);
    }   

    public void initialize (MissionBeginUnit missionBeginUnit, Coordinate position, Orientation orientation, ICountry country) 
    {
        String nationality = country.getNationality();
        String name = nationality + " Drifter";
        this.orientation = orientation;
        
        // Drifters don't move so start and destination are the same
        super.initialize(missionBeginUnit, name, position, position, country);
    }

    protected void createUnits() throws PWCGException  
    {
        spawningVehicle = new Drifter(country);
        spawningVehicle.setOrientation(new Orientation());
        spawningVehicle.setPosition(position.copy());         
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
    }   

    public void createSpawners() throws PWCGException  
    {
        int numDrifter = calcNumUnits();

        // Face towards orientation
        double drifterFacingAngle = MathUtils.adjustAngle(orientation.getyOri(), 180.0);
        Orientation drifterOrient = new Orientation();
        drifterOrient.setyOri(drifterFacingAngle);
        
        Coordinate drifterCoords = position.copy();

        double drifterSpacing = 30.0;
        
        // Direction in which subsequent units will be placed
        double placementOrientation = orientation.getyOri();        

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

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        // How many guns
        Campaign campaign =     PWCGContextManager.getInstance().getCampaign();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            minRequested = 2;
            maxRequested = 3;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            minRequested = 2;
            maxRequested = 4;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            minRequested = 3;
            maxRequested = 6;
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
            
            writer.write("  Name = \"Drifter\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Drifter\";");
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

