package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.AttackAreaFactory;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuAttackArea.AttackAreaType;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.group.MissionBeginSelfDeactivatingCheckZone;

public class AirGroundAttackMcuSequence
{    
    private MissionBeginSelfDeactivatingCheckZone missionBeginUnit;
    private McuTimer activateTimer = new McuTimer();
    private McuTimer deactivateTimer = new McuTimer();
    protected McuDeactivate deactivateEntity = new McuDeactivate();
    private  McuAttackArea attackArea = new McuAttackArea(AttackAreaType.GROUND_TARGETS);

    public AirGroundAttackMcuSequence()
    {
    }

    public void createAttackArea(String name, FlightTypes flightType, Coordinate targetCoords, int altitude, int attackTime) throws PWCGException 
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int attackMcuTriggerDistance = productSpecificConfiguration.getBombFinalApproachDistance() + 1000;

        missionBeginUnit = new MissionBeginSelfDeactivatingCheckZone(targetCoords, attackMcuTriggerDistance);
        attackArea = AttackAreaFactory.createAttackArea(flightType, name, targetCoords, altitude, attackTime);
        createSequence(name, targetCoords, attackTime) ;
        linkTargets() ;
    }
    
    public void createTriggerForPlane(PlaneMCU plane, Coordinate targetCoords)
    {
        attackArea.setObject(plane.getLinkTrId());
        missionBeginUnit.setCheckZoneTriggerObject(plane.getLinkTrId());
    }
    
    public void createTriggerForFlight(Flight flight, Coordinate targetCoords)
    {
        for (PlaneMCU plane: flight.getPlanes())
        {
            attackArea.setObject(plane.getLinkTrId());
            missionBeginUnit.setCheckZoneTriggerObject(plane.getLinkTrId());
        }
    }

    private void createSequence(String name, Coordinate targetCoords, int attackTime) 
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

    }

    private void linkTargets() 
    {
        missionBeginUnit.linkCheckZoneTarget(activateTimer.getIndex());
        activateTimer.setTarget(attackArea.getIndex());
        activateTimer.setTarget(deactivateTimer.getIndex());
        deactivateTimer.setTarget(deactivateEntity.getIndex());
        deactivateEntity.setTarget(attackArea.getIndex());
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
