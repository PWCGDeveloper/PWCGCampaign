package pwcg.mission.ground.unittypes;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.mcu.McuAttack;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public abstract class GroundDirectFireUnit extends GroundUnitSpawning implements IGroundDirectFireUnit
{
    protected McuTimer attackTimer = new McuTimer();
    protected McuAttack attackEntity = new McuAttack();

    public GroundDirectFireUnit(TacticalTarget targetType) 
    {
        super (targetType);
        setFiring(true);
    }

    public void initialize (MissionBeginUnit missionBeginUnit, String name, Coordinate startCoords, Coordinate destinationCoords, ICountry country) 
    {
        super.initialize(missionBeginUnit, name, startCoords,  destinationCoords, country);
    }

    @Override
    public void createUnitMission() throws PWCGException 
    {
        createAttack();
        super.createUnitMission();
    }

    protected void createAttack() 
    {
        attackTimer.setName(name + " Attack Timer");      
        attackTimer.setDesc(name + " Attack Timer");       
        attackTimer.setPosition(position);

        attackEntity.setName(name + " Attack");
        attackEntity.setDesc(name + " Attack");
        attackEntity.setOrientation(new Orientation());       
        attackEntity.setPosition(position); 
    }   

    public void addTarget(int targetIndex)
    {
        attackEntity.setTarget(targetIndex);
    }

    @Override
    protected void createGroundTargetAssociations() 
    {
        // MBU -> Spawn Timer
        this.missionBeginUnit.linkToMissionBegin(this.spawnTimer.getIndex());

        // Spawn Timer -> Spawns
        for (McuSpawn spawn : spawners)
        {
            spawnTimer.setTarget(spawn.getIndex());
        }

        // Spawn Timer -> Attack Timer
        spawnTimer.setTarget(this.attackTimer.getIndex());

        // Attack Timer -> Attack
        attackTimer.setTarget(this.attackEntity.getIndex());

    }
    
    @Override
    protected void createObjectAssociations() 
    {
        // Unit may be spawning or always on the map
        if (spawningVehicle != null)
        {
            attackEntity.setObject(spawningVehicle.getEntity().getIndex());
            super.createObjectAssociations();
        }
    }

}	

