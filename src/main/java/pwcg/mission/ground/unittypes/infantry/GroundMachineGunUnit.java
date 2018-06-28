package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.ww1.ground.vehicle.MachineGun;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.GroundDirectFireUnit;
import pwcg.mission.mcu.McuSpawn;

public class GroundMachineGunUnit extends GroundDirectFireUnit
{
    private Campaign campaign;

    public GroundMachineGunUnit(Campaign campaign) throws PWCGException
    {
        super (TacticalTarget.TARGET_INFANTRY);
        
        this.campaign = campaign;
    }   

    public void initialize (MissionBeginUnitCheckZone missionBeginUnit, Coordinate position, Coordinate destinationCoords, ICountry country) 
    {
        String nationality = country.getNationality();
        String name = nationality + " Machine Gun";
        super.initialize(missionBeginUnit, name,  position, destinationCoords, country);
    }

    public void createUnits()  
    {
        spawningVehicle = new MachineGun(country);
        spawningVehicle.setOrientation(new Orientation());
        spawningVehicle.setPosition(position.copy());         
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
    }

	protected void createSpawners() throws PWCGException  
	{
        int numMachineGun = calcNumUnits();

		// Face towards enemy
		double machineGunFacingAngle = MathUtils.calcAngle(position, destinationCoords);
		Orientation machineGunOrient = new Orientation();
		machineGunOrient.setyOri(machineGunFacingAngle);
		
        // MGs are behind the lines
        double initialPlacementAngle = MathUtils.adjustAngle (machineGunFacingAngle, 180.0);      
        Coordinate machineGunCoords = MathUtils.calcNextCoord(position, initialPlacementAngle, 25.0);

        double startLocationOrientation = MathUtils.adjustAngle (machineGunFacingAngle, 270);             
        double machingGunSpacing = 100.0;
        machineGunCoords = MathUtils.calcNextCoord(machineGunCoords, startLocationOrientation, ((numMachineGun * machingGunSpacing) / 2));       
        
        double placementOrientation = MathUtils.adjustAngle (machineGunFacingAngle, 90.0);        

        for (int i = 0; i < numMachineGun; ++i)
        {   
            McuSpawn spawn = new McuSpawn();
            spawn.setName("Machine Gun Spawn " + (i + 1));      
            spawn.setDesc("Machine Gun Spawn " + (i + 1));
            spawn.setPosition(machineGunCoords);

            spawners.add(spawn);

            machineGunCoords = MathUtils.calcNextCoord(machineGunCoords, placementOrientation, machingGunSpacing);
        }       
	}	

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            minRequested = 1;
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
            maxRequested = 5;
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
            
            writer.write("  Name = \"Machine Gun\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Machine Gun\";");
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
