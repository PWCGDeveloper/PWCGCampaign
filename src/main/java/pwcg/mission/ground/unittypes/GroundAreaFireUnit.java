package pwcg.mission.ground.unittypes;

import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public abstract class GroundAreaFireUnit extends GroundUnitSpawning
{
    protected McuTimer attackAreaTimer = new McuTimer();
    protected McuAttackArea attackArea = new McuAttackArea();

	public GroundAreaFireUnit(GroundUnitInformation pwcgGroundUnitInformation) 
	{
	    super (pwcgGroundUnitInformation);
	}

    protected void createAttackArea() 
    {
        attackAreaTimer.setName(pwcgGroundUnitInformation.getName() + " AttackArea Timer");      
        attackAreaTimer.setDesc(pwcgGroundUnitInformation.getName() + " AttackArea Timer");       
        attackAreaTimer.setPosition(pwcgGroundUnitInformation.getPosition());

        attackArea.setName(pwcgGroundUnitInformation.getName() + " AttackArea");
        attackArea.setDesc(pwcgGroundUnitInformation.getName() + " AttackArea");
        attackArea.setOrientation(new Orientation());       
        attackArea.setPosition(pwcgGroundUnitInformation.getPosition()); 
        attackArea.setTime(600);
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
        spawnTimer.setTarget(this.attackAreaTimer.getIndex());

        // Attack Timer -> Attack
        attackAreaTimer.setTarget(this.attackArea.getIndex());

    }
    
    @Override
    protected void createObjectAssociations() 
    {
        attackArea.setObject(spawningVehicle.getEntity().getIndex());
        super.createObjectAssociations();
    }


}	

