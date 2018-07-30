package pwcg.mission.ground.unittypes.artillery;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.factory.VehicleFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundRepeatSpawningUnit;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.mission.mcu.McuSpawn;

public class GroundAAABattery extends GroundRepeatSpawningUnit
{
    static private int MG_CHECK_ZONE = 1000;
    static private int MG_ATTACK_AREA = 500;
    static private int ARTY_CHECK_ZONE = 6000;
    static private int ARTY_ATTACK_AREA = 5000;

    protected boolean isMg = true;
    
    protected boolean hasBeenWritten = false;

    public GroundAAABattery(GroundUnitInformation pwcgGroundUnitInformation, boolean isMg) throws PWCGException
    {
        super(pwcgGroundUnitInformation);
        this.isMg = isMg;
    }   

    public void createUnits() throws PWCGException 
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        
        if (isMg)
        {
            spawningVehicle = vehicleFactory.createAAAMachineGun(pwcgGroundUnitInformation.getCountry());
            checkZoneMeters = MG_CHECK_ZONE;
            checkAttackAreaMeters = MG_ATTACK_AREA;
        }
        else
        {
            spawningVehicle = vehicleFactory.createAAAArtillery(pwcgGroundUnitInformation.getCountry());
            checkZoneMeters = ARTY_CHECK_ZONE;
            checkAttackAreaMeters = ARTY_ATTACK_AREA;
        }

        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.setCountry(pwcgGroundUnitInformation.getCountry());
        
        spawningVehicle.populateEntity();       
        
        super.createUnits();
    }

    protected void createSpawners() throws PWCGException 
    {
        int numAAA = calcNumUnits();
        
        for (int i = 0; i < numAAA; ++i)
        {
            McuSpawn spawn = new McuSpawn();
            
            Coordinate spawnPosition = pwcgGroundUnitInformation.getPosition().copy();
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

    protected int calcNumUnits()
    {
        if (!isMinMaxRequested())
        {
            if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
            {
                setMinMaxRequested(1, 1);
            }
            else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
            {
                setMinMaxRequested(1, 2);
            }
            else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
            {
                setMinMaxRequested(1, 3);
            }
            else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
            {
                setMinMaxRequested(2, 4);
            }
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
            
            writer.write("  Name = \"AAA Battery\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"AAA Battery\";");
            writer.newLine();

            pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);

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
