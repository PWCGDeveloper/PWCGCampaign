package pwcg.mission.ground.unittypes;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

public abstract class GroundAreaFireUnit extends GroundUnitSpawning
{
    protected McuTimer attackAreaTimer = new McuTimer();
    protected McuAttackArea attackArea = new McuAttackArea();

	public GroundAreaFireUnit(TacticalTarget targetType) 
	{
	    super (targetType);
	}

    public void initialize (MissionBeginUnit missionBeginUnit, String name, Coordinate startCoords, Coordinate destinationCoords, ICountry country) 
    {
        super.initialize(missionBeginUnit, name, startCoords,  destinationCoords, country);
    }

    protected void createAttackArea() 
    {
        attackAreaTimer.setName(name + " AttackArea Timer");      
        attackAreaTimer.setDesc(name + " AttackArea Timer");       
        attackAreaTimer.setPosition(position);

        attackArea.setName(name + " AttackArea");
        attackArea.setDesc(name + " AttackArea");
        attackArea.setOrientation(new Orientation());       
        attackArea.setPosition(position); 
        attackArea.setTime(600);
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

