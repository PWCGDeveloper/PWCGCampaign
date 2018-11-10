package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;

public class AttackMcuSequence
{    
    public static final int CHECK_ZONE_INSTANCE = 11000;
            
    private MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
    private McuTimer activateTimer = new McuTimer();
    private McuTimer deactivateTimer = new McuTimer();
    private McuAttackArea attackArea = new McuAttackArea();
    protected McuDeactivate deactivateEntity = new McuDeactivate();

    public AttackMcuSequence()
    {
    }

    public void createAttackArea(PlaneMCU plane, String name, Coordinate targetCoords, int altitude, int attackTIme) throws PWCGException 
    {
        attackArea.setAttackGround(0);
        attackArea.setAttackGTargets(1);
        attackArea.setAttackAir(0);
        attackArea.setName("Attack Area for " + name);
        attackArea.setDesc("Attack Area for " + name);
        attackArea.setAttackArea(4000);
        attackArea.setTime(attackTIme);
        
        attackArea.setOrientation(new Orientation());
        
        Coordinate attackAreaCoords = targetCoords.copy();
        attackAreaCoords.setYPos(altitude);
        
        attackArea.setPosition(attackAreaCoords);   
        attackArea.setObject(plane.getLinkTrId());
        
        createSequence(attackArea, name, targetCoords, attackTIme) ;
        
        Coalition coalition  = Coalition.getFriendlyCoalition(plane.getCountry());
        missionBeginUnit.initialize(targetCoords, CHECK_ZONE_INSTANCE, coalition);
        missionBeginUnit.setStartTime(2);
        missionBeginUnit.linkToMissionBegin(activateTimer.getIndex());
    }

    private void createSequence(BaseFlightMcu attackMcu, String name, Coordinate targetCoords, int attackTime) 
    {
        activateTimer.setName(name + ": Attack Area Timer");      
        activateTimer.setDesc("Attack Area Timer for " + name);
        activateTimer.setPosition(targetCoords.copy());
        activateTimer.setTimer(1);              

        deactivateEntity.setName("Attack Area Deactivate");
        deactivateEntity.setDesc("Attack Area Deactivate");
        deactivateEntity.setOrientation(new Orientation());
        deactivateEntity.setPosition(targetCoords.copy());             
        
        deactivateTimer.setName("Attack Area Deactivate Timer");
        deactivateTimer.setDesc("Attack Area Deactivate Timer");
        deactivateTimer.setOrientation(new Orientation());
        deactivateTimer.setPosition(targetCoords.copy());              
        deactivateTimer.setTimer(attackTime);              

        linkTargets(attackMcu) ;
    }

    private void linkTargets(BaseFlightMcu attackMcu) 
    {
        activateTimer.setTarget(attackMcu.getIndex());
        activateTimer.setTarget(deactivateTimer.getIndex());
        deactivateTimer.setTarget(deactivateEntity.getIndex());
        deactivateEntity.setTarget(attackMcu.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        activateTimer.write(writer);
        attackArea.write(writer);
        deactivateTimer.write(writer);
        deactivateEntity.write(writer);
    }

    public void linkToAttackDeactivate(int targetIndex)
    {
        deactivateTimer.setTarget(targetIndex);
    }

    public McuTimer getDeactivateTimer()
    {
        return deactivateTimer;
    }

    public McuAttackArea getAttackAreaMcu()
    {
        return attackArea;
    }
}
