package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.ww1.ground.vehicle.MovingInfantry;
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
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.AssaultGenerator.BattleSize;
import pwcg.mission.ground.unittypes.GroundMovingDirectFireUnit;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAssaultInfantryUnit extends GroundMovingDirectFireUnit
{
    private BattleSize battleSize;
    
	public GroundAssaultInfantryUnit(BattleSize battleSize) 
	{	    
        super(TacticalTarget.TARGET_INFANTRY);
        this.battleSize = battleSize;
        this.unitSpeed = 3;
	}


    public void initialize (MissionBeginUnitCheckZone missionBeginUnit, Coordinate startCoords, Coordinate destinationCoords, ICountry country) 
    {
        String name = "Infantry";
        super.initialize (missionBeginUnit, name, startCoords, destinationCoords, country);
    }

	protected void createUnits() throws PWCGException  
	{
	    MovingInfantry infantry = new MovingInfantry(country);
        if (!infantry.vehicleExists())
        {
            Logger.log (LogLevel.DEBUG, "No infantry model or script found.  Download and install 3rd party objects");
        }

		infantry.setOrientation(new Orientation());
		infantry.setPosition(position.copy());			
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
        double infantryFacingAngle = MathUtils.calcAngle(position, destinationCoords);
        Orientation infantryOrient = new Orientation();
        infantryOrient.setyOri(infantryFacingAngle);
        
        // Locate the infantry such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (infantryFacingAngle, 270);     
        Coordinate infantryCoords = MathUtils.calcNextCoord(position, startLocationOrientation, ((numInfantry * infantrySpacing) / 2));
        
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

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        // How many units
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (battleSize == BattleSize.BATTLE_SIZE_TINY)
        {
            minRequested = 1;
            maxRequested = 2;
        }
        else
        {
            if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
            {
                minRequested = 5;
                maxRequested = 10;
            }
            else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
            {
                minRequested = 10;
                maxRequested = 15;
            }
            else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
            {
                minRequested = 15;
                maxRequested = 20;
            }
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
            
            writer.write("  Name = \"Assaulting Infantry\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Assaulting Infantry\";");
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

