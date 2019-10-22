package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundDirectFireUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuFlare;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.group.FlareSequence;

public class GroundMachineGunFlareUnit extends GroundDirectFireUnit
{
    private FlareSequence flares = new FlareSequence();

	public GroundMachineGunFlareUnit(GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super (pwcgGroundUnitInformation);
	}	

	protected void createUnits() throws PWCGException  
	{
	    IVehicle flareMachineGun = pwcg.mission.ground.vehicle.VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.MachineGun);

        flareMachineGun.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        flareMachineGun.setPosition(pwcgGroundUnitInformation.getPosition().copy());           
        flareMachineGun.populateEntity();
        flareMachineGun.getEntity().setEnabled(1);

       this.spawningVehicle = flareMachineGun;

       createFlares();
	}

    protected int calcNumUnits()
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            setMinMaxRequested(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            setMinMaxRequested(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(1, 2);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(2, 2);
        }
        
        return calculateForMinMaxRequested();
    }

    protected void createSpawners() throws PWCGException  
    {
        // Face towards enemy
        double pillBoxFacingAngle = MathUtils.calcAngle(pwcgGroundUnitInformation.getPosition(), pwcgGroundUnitInformation.getDestination());
        Orientation pillBoxOrient = new Orientation();
        pillBoxOrient.setyOri(pillBoxFacingAngle);
        
        McuSpawn spawn = new McuSpawn();
        spawn.setName("Pillbox Spawn");      
        spawn.setDesc("Pillbox Spawn");
        spawn.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        spawners.add(spawn);
    }   

    public void createFlares() throws PWCGException 
    {
        int flareColor = McuFlare.FLARE_COLOR_RED;        
        if (pwcgGroundUnitInformation.getCountry().getSide() == Side.ALLIED)
        {
            flareColor = McuFlare.FLARE_COLOR_GREEN;
        }
        
        Coalition friendlyCoalition  = Coalition.getFriendlyCoalition(pwcgGroundUnitInformation.getCountry());

        flares = new FlareSequence();
        flares.setFlare(friendlyCoalition, pwcgGroundUnitInformation.getPosition().copy(), flareColor, spawningVehicle.getEntity().getIndex());
    }

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
