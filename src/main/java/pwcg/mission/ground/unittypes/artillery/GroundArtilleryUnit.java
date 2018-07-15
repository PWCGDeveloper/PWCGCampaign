package pwcg.mission.ground.unittypes.artillery;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.VehicleFactory;
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
import pwcg.mission.ground.unittypes.GroundAreaFireUnit;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;

public class GroundArtilleryUnit extends GroundAreaFireUnit
{
    private Campaign campaign;
    
	public GroundArtilleryUnit(Campaign campaign, GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super (pwcgGroundUnitInformation);
	    this.campaign = campaign;
	}

	protected void createUnits() throws PWCGException  
	{
	    IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        spawningVehicle = vehicleFactory.createArtillery(pwcgGroundUnitInformation.getCountry());
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());  
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
	}

    @Override
    protected void createSpawners() throws PWCGException 
    {        
        int numArtillery = calcNumUnits();

        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        double angleToEnemy = frontLinesForMap.findClosestEnemyPositionAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getCountry().getSide().getOppositeSide());
        
        Orientation gunOrient = new Orientation();
        gunOrient.setyOri(angleToEnemy);

        // Locate the guns such that startCoords is the middle of the line
        double startLocationOrientation = MathUtils.adjustAngle (angleToEnemy, 270);             
        double gunSpacing = 30.0;
        Coordinate gunCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), startLocationOrientation, ((numArtillery * gunSpacing) / 2));       
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (angleToEnemy, 90.0);        

        for (int i = 0; i < numArtillery; ++i)
        {   
            // Calculate the  next gun pwcgGroundUnitInformation.getPosition()
            gunCoords = MathUtils.calcNextCoord(gunCoords, placementOrientation, gunSpacing);

            McuSpawn spawn = new McuSpawn();
            spawn.setName("Artillery Spawn " + (i + 1));      
            spawn.setDesc("Artillery Spawn " + (i + 1));
            spawn.setPosition(gunCoords);

            spawners.add(spawn);
        }       
    }

    protected void createAttackArea() 
    {
        attackAreaTimer.setName("Artillery Battery Area Timer");      
        attackAreaTimer.setDesc("Artillery Battery Area Timer");       
        attackAreaTimer.setPosition(pwcgGroundUnitInformation.getPosition());
        attackAreaTimer.setTarget(attackArea.getIndex());

        attackArea.setAttackGround(1);
        attackArea.setAttackGTargets(0);
        attackArea.setAttackAir(0);
        attackArea.setName("Artillery Battery Area");
        attackArea.setDesc("Artillery Battery Area");
        attackArea.setAttackArea(6000);     
        attackArea.setOrientation(new Orientation());       
        attackArea.setPosition(pwcgGroundUnitInformation.getDestination()); 
        attackArea.setObject(spawningVehicle.getEntity().getIndex());
    }   

    protected void calcNumUnitsByConfig() throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            minRequested = 2;
            maxRequested = 4;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            minRequested = 4;
            maxRequested = 8;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            minRequested = 6;
            maxRequested = 10;
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
            
            writer.write("  Name = \"Artillery\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Artillery\";");
            writer.newLine();

            pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);

            spawnTimer.write(writer);
            spawningVehicle.write(writer);
            for (McuSpawn spawn : spawners)
            {
                spawn.write(writer);
            }

            if (attackAreaTimer != null)
            {
                attackAreaTimer.write(writer);
                attackArea.write(writer);
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

