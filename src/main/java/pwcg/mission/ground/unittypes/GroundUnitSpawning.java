package pwcg.mission.ground.unittypes;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public abstract class GroundUnitSpawning extends GroundUnit
{    
    public static final int NUM_UNITS_BY_CONFIG = -1;
    
    protected McuTimer spawnTimer = new McuTimer();
    protected List <McuSpawn> spawners = new ArrayList<McuSpawn>();
    
    protected IVehicle spawningVehicle = null;
    
    protected int minRequested = NUM_UNITS_BY_CONFIG;
    protected int maxRequested = NUM_UNITS_BY_CONFIG;

	public GroundUnitSpawning(TacticalTarget targetType) 
	{
        super(targetType);
	}

    protected int calcNumUnits() throws PWCGException 
    {
        if (minRequested == NUM_UNITS_BY_CONFIG || maxRequested == NUM_UNITS_BY_CONFIG)
        {
            calcNumUnitsByConfig();
        }
        
        int randomUnits = 0;
        if (maxRequested > minRequested)
        {
            randomUnits = RandomNumberGenerator.getRandom(maxRequested - minRequested + 1);
        }

        int numUnits = minRequested + randomUnits;
        
        return numUnits;
    }

    public void initialize (MissionBeginUnit missionBeginUnit, String name, Coordinate startCoords, Coordinate destinationCoords, ICountry country) 
    {
        super.initialize(missionBeginUnit, name, startCoords,  destinationCoords, country);
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
        spawnTimer.setPosition(position.copy());
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

    public void setMinRequested(int minRequested)
    {
        this.minRequested = minRequested;
    }

    public void setMaxRequested(int maxRequested)
    {
        this.maxRequested = maxRequested;
    }
    
    public List<McuSpawn> getSpawners()
    {
        return spawners;
    }

    abstract protected void createUnits() throws PWCGException ;
    abstract protected void createSpawners() throws PWCGException ;
    abstract protected void calcNumUnitsByConfig() throws PWCGException ;
}	

