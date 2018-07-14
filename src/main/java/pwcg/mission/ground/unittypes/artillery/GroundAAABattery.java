package pwcg.mission.ground.unittypes.artillery;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.VehicleFactory;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.GroundRepeatSpawningUnit;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;

public class GroundAAABattery extends GroundRepeatSpawningUnit
{
    static private int MG_CHECK_ZONE = 1000;
    static private int MG_ATTACK_AREA = 500;
    static private int ARTY_CHECK_ZONE = 6000;
    static private int ARTY_ATTACK_AREA = 5000;
 
    private Campaign campaign;

    protected boolean isMg = true;
    
    protected boolean hasBeenWritten = false;

    public GroundAAABattery(Campaign campaign) throws PWCGException
    {
        super (TacticalTarget.TARGET_ARTILLERY);
        this.campaign = campaign;
    }   


    public void initialize(MissionBeginUnitCheckZone missionBeginUnit, Coordinate position, ICountry country, boolean isMg) 
                    throws PWCGException 
    {
        this.position = position;
        this.country = country;
        this.isMg = isMg;
        
        super.initialize(missionBeginUnit, "AAA Battery", position.copy(), position.copy(), country);
    }

    public void createUnits() throws PWCGException 
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        
        if (isMg)
        {
            spawningVehicle = vehicleFactory.createAAAMachineGun(country);
            checkZoneMeters = MG_CHECK_ZONE;
            checkAttackAreaMeters = MG_ATTACK_AREA;
        }
        else
        {
            spawningVehicle = vehicleFactory.createAAAArtillery(country);
            checkZoneMeters = ARTY_CHECK_ZONE;
            checkAttackAreaMeters = ARTY_ATTACK_AREA;
        }

        spawningVehicle.setPosition(position.copy());
        spawningVehicle.setCountry(country);
        
        spawningVehicle.populateEntity();       
        
        super.createUnits();
    }

    protected void createSpawners() throws PWCGException 
    {
        int numAAA = calcNumUnits();
        
        for (int i = 0; i < numAAA; ++i)
        {
            McuSpawn spawn = new McuSpawn();
            
            Coordinate spawnPosition = position.copy();
            if (numAAA > 1)
            {
                if (i == 0)
                {
                    spawnPosition.setXPos(spawnPosition.getXPos() + 100);
                }
                else if (i == 1)
                {
                    spawnPosition.setXPos(spawnPosition.getXPos() - 100);
                }
                else if (i == 2)
                {
                    spawnPosition.setZPos(spawnPosition.getZPos() - 100);             
                }
                else if (i == 3)
                {
                    spawnPosition.setZPos(spawnPosition.getZPos() + 100);             
                }
            }

            spawn = new McuSpawn();
            spawn.setName("AAA Spawn " + (i + 1));      
            spawn.setDesc("AAA Spawn " + (i + 1));
            spawn.setPosition(spawnPosition);

            spawners.add(spawn);
        }
    }

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            minRequested = 1;
            maxRequested = 2;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            minRequested = 1;
            maxRequested = 3;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            minRequested = 2;
            maxRequested = 4;
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
            
            writer.write("  Name = \"AAA Battery\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"AAA Battery\";");
            writer.newLine();

            missionBeginUnit.write(writer);

            spawnTimer.write(writer);
            spawningVehicle.write(writer);
            for (McuSpawn spawn : spawners)
            {
                spawn.write(writer);
            }

            checkZoneTimer.write(writer);
            checkZone.write(writer);
            processTimer.write(writer);
            
            addVehicleTimer.write(writer);
            attackAreaTimer.write(writer);
            attackArea.write(writer);

            hideTimer.write(writer);
            deleteTimer.write(writer);
            deleteEntity.write(writer);
            
            deactivateAAAEntity.write(writer);
            reactivateTimer.write(writer);
            reactivateEntity.write(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public void setAiLevel(AiSkillLevel aiLevel)
    {
        spawningVehicle.setAiLevel(aiLevel);
    }

    public boolean isMg()
    {
        return isMg;
    }
}
