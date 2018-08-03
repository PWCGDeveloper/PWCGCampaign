package pwcg.mission.ground.unittypes;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public abstract class GroundUnitSpawning extends GroundUnit
{    
    
    protected McuTimer spawnTimer = new McuTimer();
    protected List <McuSpawn> spawners = new ArrayList<McuSpawn>();
    
    protected IVehicle spawningVehicle = null;

	public GroundUnitSpawning(GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super(pwcgGroundUnitInformation);
	}

    @Override
    public void createUnitMission() throws PWCGException 
    {
        createSpawnTimer();
        createUnits();
        createSpawners();
        createObjectAssociations();
        createGroundTargetAssociations();
    }

    protected void createSpawnTimer() 
    {
        spawnTimer.setName("Spawn Timer");
        spawnTimer.setDesc("Spawn Timer");
        spawnTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
    }

    public List<IVehicle> getVehicles() 
    {
        List<IVehicle> vehicles = new ArrayList<IVehicle>();
        if (spawningVehicle != null)
        {
            vehicles.add(spawningVehicle);
        }
        return vehicles;
    }

    protected void createObjectAssociations() 
    {
        for (McuSpawn spawn : spawners)
        {
            spawn.setObject(spawningVehicle.getEntity().getIndex());
        }
    }
    
    public List<McuSpawn> getSpawners()
    {
        return spawners;
    }

    abstract protected void createUnits() throws PWCGException ;
    abstract protected void createSpawners() throws PWCGException ;
    abstract protected int calcNumUnits() throws PWCGException ;
}	

