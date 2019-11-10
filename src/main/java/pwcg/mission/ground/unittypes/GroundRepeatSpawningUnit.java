

package pwcg.mission.ground.unittypes;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.CoalitionFactory;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuDelete;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;

/**
 * Unit that spawns and deletes itself repeatedly
 * Used only for AAA Batteries
 * 
 * @author Patrick Wilson
 *
 */
public abstract class GroundRepeatSpawningUnit extends GroundUnitSpawning
{
    protected McuTimer checkZoneTimer = new McuTimer();
    protected McuCheckZone checkZone = null;
    protected McuTimer processTimer = new McuTimer();
    
    protected McuTimer addVehicleTimer = new McuTimer();
    protected McuTimer attackAreaTimer = new McuTimer();
    protected McuAttackArea attackArea = new McuAttackArea();

    protected McuTimer hideTimer = new McuTimer();
    protected McuTimer deleteTimer = new McuTimer();
    protected McuDelete deleteEntity = new McuDelete();
    
    protected McuDeactivate deactivateAAAEntity = new McuDeactivate();
    protected McuTimer reactivateTimer = new McuTimer();
    protected McuActivate reactivateEntity = new McuActivate();
        
    protected int checkZoneMeters = 15000;
    protected int checkAttackAreaMeters = 15000;

    public GroundRepeatSpawningUnit (GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(pwcgGroundUnitInformation);
    }

    public void createUnits() throws PWCGException 
    {
        makeCheckZone();
        makeActivation();
        makeDeactivation();
        makeProcessTimers();

    }

    private void makeCheckZone() 
    {        
        checkZone = new McuCheckZone ();
        checkZone.setZone(checkZoneMeters);
        checkZone.setName("Check Zone");
        checkZone.setDesc("Check Zone");
        checkZone.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        Coalition enemyCoalition = CoalitionFactory.getEnemyCoalition(pwcgGroundUnitInformation.getCountry());
        checkZone.triggerCheckZoneByPlaneCoalition(enemyCoalition);
    }

    private void makeActivation() 
    {
        addVehicleTimer.setName("Add Timer");
        addVehicleTimer.setDesc("Add Timer");
        addVehicleTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        spawnTimer.setName("Spawn Timer");
        spawnTimer.setDesc("Spawn Timer");
        spawnTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        attackAreaTimer.setName("Attack Area Timer");
        attackAreaTimer.setDesc("Attack Area Timer");
        attackAreaTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        attackArea.setTime(600);

        createAttackArea();        
    }

    private void createAttackArea() 
    {
        attackArea.setAttackGround(0);
        attackArea.setAttackGTargets(0);
        attackArea.setAttackAir(1);
        attackArea.setName("Attack Area");
        attackArea.setDesc("Attack Area");
        attackArea.setAttackArea(checkAttackAreaMeters);
        attackArea.setPosition(pwcgGroundUnitInformation.getPosition().copy());
    }

    private void makeDeactivation() 
    {
        hideTimer.setName("Hide Timer");
        hideTimer.setDesc("Hide Timer");
        hideTimer.setTimer(600);
        hideTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        deleteTimer.setName("Delete Timer");
        deleteTimer.setDesc("Delete Timer");
        deleteTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        deactivateAAAEntity.setName("Deactivate");
        deactivateAAAEntity.setDesc("Deactivate");
        deactivateAAAEntity.setPosition(pwcgGroundUnitInformation.getPosition().copy());              

        spawnTimer.setName("Spawn Timer");
        spawnTimer.setDesc("Spawn Timer");
        spawnTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        reactivateTimer.setName("Reactivate Timer");
        reactivateTimer.setDesc("Reactivate Timer");
        reactivateTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        deleteEntity.setName("Delete");
        deleteEntity.setDesc("Delete");
        deleteEntity.setPosition(pwcgGroundUnitInformation.getPosition().copy());

        reactivateEntity.setName("Reactivate");
        reactivateEntity.setDesc("Reactivate");
        reactivateEntity.setPosition(pwcgGroundUnitInformation.getPosition().copy());
    }

    protected void makeProcessTimers() 
    {
        checkZoneTimer.setName("AAA Check Zone Timer");
        checkZoneTimer.setDesc("AAA Check Zone Timer");
        checkZoneTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
            
        processTimer.setName("AAA Process Timer");
        processTimer.setDesc("AAA Process Timer");
        processTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
    }

    @Override
    protected void createGroundTargetAssociations() 
    {
        pwcgGroundUnitInformation.getMissionBeginUnit().linkToMissionBegin(checkZoneTimer.getIndex());
        checkZoneTimer.setTarget(checkZone.getIndex());
        checkZone.setTarget(checkZone.getIndex());
        checkZone.setTarget(processTimer.getIndex());

        processTimer.setTarget(addVehicleTimer.getIndex());
        addVehicleTimer.setTarget(attackAreaTimer.getIndex());
        attackAreaTimer.setTarget(attackArea.getIndex());
        
        addVehicleTimer.setTarget(spawnTimer.getIndex());
        for (McuSpawn spawn : spawners)
        {
            spawnTimer.setTarget(spawn.getIndex());
        }

        processTimer.setTarget(hideTimer.getIndex());
        hideTimer.setTarget(deleteTimer.getIndex());
        deleteTimer.setTarget(deleteEntity.getIndex());

        hideTimer.setTarget(reactivateTimer.getIndex());
        reactivateTimer.setTarget(reactivateEntity.getIndex());
        reactivateEntity.setTarget(checkZone.getIndex());

        processTimer.setTarget(deactivateAAAEntity.getIndex());
        deactivateAAAEntity.setTarget(checkZone.getIndex());
    }

    @Override
    protected void createObjectAssociations() 
    {
        
        attackArea.setObject(spawningVehicle.getEntity().getIndex());
        
        for (McuSpawn spawn : spawners)
        {
            spawn.setObject(spawningVehicle.getEntity().getIndex());
        }
        
        deleteEntity.setObject(spawningVehicle.getEntity().getIndex());
    }
}
