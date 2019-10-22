package pwcg.mission.ground.unittypes.artillery;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundAreaFireUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.McuSpawn;

public class GroundArtilleryUnit extends GroundAreaFireUnit
{
	public GroundArtilleryUnit(GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super (pwcgGroundUnitInformation);
	}

	protected void createUnits() throws PWCGException  
	{
        spawningVehicle = pwcg.mission.ground.vehicle.VehicleFactory.createVehicle(pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getDate(), VehicleClass.ArtilleryHowitzer);
        spawningVehicle.setPosition(pwcgGroundUnitInformation.getPosition().copy());  
        spawningVehicle.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
        spawningVehicle.populateEntity();
        spawningVehicle.getEntity().setEnabled(1);
	}

    @Override
    protected void createSpawners() throws PWCGException 
    {        
        int numArtillery = calcNumUnits();

        double startLocationOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 270);             
        double gunSpacing = 30.0;
        Coordinate gunCoords = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), startLocationOrientation, ((numArtillery * gunSpacing) / 2));       
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (pwcgGroundUnitInformation.getOrientation().getyOri(), 90.0);        

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

    protected int calcNumUnits()
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            setMinMaxRequested(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            setMinMaxRequested(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(4, 8);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(6, 10);
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

