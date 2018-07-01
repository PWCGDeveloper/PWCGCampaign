package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;

public class AttackMcuSequence
{    
    private McuTimer activateTimer = new McuTimer();
    private McuTimer deactivateTimer = new McuTimer();
    private McuAttackArea attackArea = new McuAttackArea();
    protected McuDeactivate deactivateEntity = new McuDeactivate();

    public AttackMcuSequence()
    {
    }

    public AttackMcuSequence copy()
    {
        AttackMcuSequence clone = new AttackMcuSequence();
        clone.activateTimer = activateTimer.copy();
        clone.deactivateTimer = deactivateTimer.copy();
        clone.deactivateEntity = deactivateEntity.copy();
         
        if (attackArea != null)
        {
            clone.attackArea = attackArea.copy();
        }
        
        return clone;
    }

    public void createAttackArea(PlaneMCU plane, String name, Coordinate targetCoords, int altitude, int attackTIme) 
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
    }

    private void createSequence(BaseFlightMcu attackMcu, String name, Coordinate targetCoords, int attackTIme) 
    {
        activateTimer.setName(name + ": Attack Area Timer");      
        activateTimer.setDesc("Attack Area Timer for " + name);
        activateTimer.setPosition(targetCoords.copy());


        // Deactivate the attack entity after a period of time
        deactivateEntity.setName("Dive Bombing Deactivate");
        deactivateEntity.setDesc("Dive Bombing Deactivate");
        deactivateEntity.setOrientation(new Orientation());
        deactivateEntity.setPosition(targetCoords.copy());             
        
        // Deactivate timer
        deactivateTimer.setName("Dive Bombing Deactivate Timer");
        deactivateTimer.setDesc("Dive Bombing Deactivate Timer");
        deactivateTimer.setOrientation(new Orientation());
        deactivateTimer.setPosition(targetCoords.copy());              
        deactivateTimer.setTimer(attackTIme);              
        
        // Link everything
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
        activateTimer.write(writer);
        attackArea.write(writer);
        deactivateTimer.write(writer);
        deactivateEntity.write(writer);
    }

    public McuTimer getActivateTimer()
    {
        return activateTimer;
    }

    public void setActivateTimer(McuTimer activateTimer)
    {
        this.activateTimer = activateTimer;
    }

    public McuTimer getDeactivateTimer()
    {
        return deactivateTimer;
    }

    public void setDeactivateTimer(McuTimer deactivateTimer)
    {
        this.deactivateTimer = deactivateTimer;
    }

    public McuAttackArea getAttackAreaMcu()
    {
        return attackArea;
    }

    public void setAttackAreaMcu(McuAttackArea attackAreaMcu)
    {
        this.attackArea = attackAreaMcu;
    }

    public McuDeactivate getDeactivateEntity()
    {
        return deactivateEntity;
    }

    public void setDeactivateEntity(McuDeactivate deactivateEntity)
    {
        this.deactivateEntity = deactivateEntity;
    }
}
