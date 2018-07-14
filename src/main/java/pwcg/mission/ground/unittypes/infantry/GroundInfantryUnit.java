package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.ww1.ground.vehicle.Infantry;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.unittypes.GroundDirectFireUnit;
import pwcg.mission.mcu.McuSpawn;

public class GroundInfantryUnit extends GroundDirectFireUnit
{

    public GroundInfantryUnit(TacticalTarget targetType) 
    {
        super (targetType);
    }

    public void initialize (MissionBeginUnit missionBeginUnit, Coordinate position, Coordinate targetCoords, ICountry country) 
    {
        String nationality = country.getNationality();
        String name = nationality + " Infantry";
        super.initialize(missionBeginUnit, name, position, targetCoords,  country);
    }

    public void createUnits() throws PWCGException  
    {
        spawningVehicle = new Infantry(country);
        if (!spawningVehicle.vehicleExists())
        {
            Logger.log (LogLevel.DEBUG, "No infantry model or script found.  Download and install 3rd party objects");
        }
        
        spawningVehicle.setOrientation(new Orientation());
        spawningVehicle.setPosition(position.copy());         
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
    }


    public void createSpawners() throws PWCGException  
    {
        int numInfantry = calcNumUnits();

        // Space defensive infantry a bit further that assaulting infantry
        double infantrySpacing = 40.0;

        // Move towards enemy
        double infantryFacingAngle = MathUtils.calcAngle(position, destinationCoords);
        Orientation infantryOrient = new Orientation();
        infantryOrient.setyOri(infantryFacingAngle);

        // Locate the infantry such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (infantryFacingAngle, 270);     
        Coordinate infantryBaseCoords = MathUtils.calcNextCoord(position, startLocationOrientation, ((numInfantry * infantrySpacing) / 2));       

        // Direction in which subsequent units will be placed
        double placementLineOrientation = MathUtils.adjustAngle (infantryFacingAngle, 90);     

        for (int i = 0; i < numInfantry; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Infantry Spawn " + (i + 1));      
            spawn.setDesc("Infantry Spawn " + (i + 1));

            double placementOffsetOrientation = MathUtils.calcAngle(destinationCoords, position);
            int placementOffsetDistance = RandomNumberGenerator.getRandom(150);
            Coordinate infantryCoords = MathUtils.calcNextCoord(infantryBaseCoords, placementOffsetOrientation, placementOffsetDistance);

            spawn.setPosition(infantryCoords);

            spawners.add(spawn);

            // Calculate the  next gun position
            infantryBaseCoords = MathUtils.calcNextCoord(infantryBaseCoords, placementLineOrientation, infantrySpacing);
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
            minRequested = 4;
            maxRequested = 8;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            minRequested = 6;
            maxRequested = 10;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            minRequested = 8;
            maxRequested = 15;
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
            
            writer.write("  Name = \"Infantry\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Infantry\";");
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

