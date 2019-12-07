package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.AttackAreaFactory;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuAttackArea.AttackAreaType;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;

public class AttackMcuSequence
{    
    public static final int ATTACK_MCU_TRIGGER_DISTANCE = 12000;

    private SelfDeactivatingCheckZone selfDeactivatingCheckZone;
    private McuTimer activateTimer = new McuTimer();
    private McuTimer deactivateTimer = new McuTimer();
    protected McuDeactivate deactivateEntity = new McuDeactivate();
    private  McuAttackArea attackArea = new McuAttackArea(AttackAreaType.GROUND_TARGETS);

    public AttackMcuSequence()
    {
    }

    public void createAttackArea(String name, FlightTypes flightType, Coordinate targetCoords, int altitude, int attackTime) throws PWCGException 
    {
        selfDeactivatingCheckZone = new SelfDeactivatingCheckZone(targetCoords, ATTACK_MCU_TRIGGER_DISTANCE);
        attackArea = AttackAreaFactory.createAttackArea(flightType, name, targetCoords, altitude, attackTime);
        createSequence(attackArea, name, targetCoords, attackTime) ;
    }
    
    public void createTriggerForPlane(PlaneMCU plane, Coordinate targetCoords)
    {
        attackArea.setObject(plane.getLinkTrId());

        selfDeactivatingCheckZone.setCheckZoneObject(plane.getLinkTrId());
    }
    
    public void createTriggerForFlight(Flight flight, Coordinate targetCoords)
    {
        for (PlaneMCU plane: flight.getPlanes())
        {
            attackArea.setObject(plane.getLinkTrId());
            selfDeactivatingCheckZone.setCheckZoneObject(plane.getLinkTrId());
        }
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
        selfDeactivatingCheckZone.write(writer);
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
