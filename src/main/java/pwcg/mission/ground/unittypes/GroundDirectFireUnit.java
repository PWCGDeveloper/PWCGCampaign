package pwcg.mission.ground.unittypes;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.mcu.McuAttack;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public abstract class GroundDirectFireUnit extends GroundUnitSpawning implements IGroundDirectFireUnit
{
    protected McuTimer attackTimer = new McuTimer();
    protected McuAttack attackEntity = new McuAttack();

    public GroundDirectFireUnit(GroundUnitInformation pwcgGroundUnitInformation) 
    {
        super(pwcgGroundUnitInformation);
    }

    @Override
    public void createUnitMission() throws PWCGException 
    {
        createAttack();
        super.createUnitMission();
    }

    protected void createAttack() 
    {
        attackTimer.setName(pwcgGroundUnitInformation.getName() + " Attack Timer");      
        attackTimer.setDesc(pwcgGroundUnitInformation.getName() + " Attack Timer");       
        attackTimer.setPosition(pwcgGroundUnitInformation.getPosition());

        attackEntity.setName(pwcgGroundUnitInformation.getName() + " Attack");
        attackEntity.setDesc(pwcgGroundUnitInformation.getName() + " Attack");
        attackEntity.setOrientation(new Orientation());       
        attackEntity.setPosition(pwcgGroundUnitInformation.getPosition()); 
    }   

    public void addTarget(int targetIndex)
    {
        attackEntity.setTarget(targetIndex);
    }

    @Override
    protected void createGroundTargetAssociations() 
    {
        // MBU -> Spawn Timer
        pwcgGroundUnitInformation.getMissionBeginUnit().linkToMissionBegin(this.spawnTimer.getIndex());

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

