package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.FlightAttackAreaFactory;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.target.TargetDefinition;

public class AirGroundAttackMcuSequence
{    
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    private MissionBeginSelfDeactivatingCheckZone missionBeginUnit;
    private McuTimer activateTimer = new McuTimer();
    private McuTimer deactivateTimer = new McuTimer();
    protected McuDeactivate deactivateEntity = new McuDeactivate();
    private  McuAttackArea attackArea;

    public AirGroundAttackMcuSequence(IFlight flight)
    {
        this.flightInformation = flight.getFlightInformation();
        this.targetDefinition = flight.getTargetDefinition();
    }
    
    public void createAttackArea(int attackTime, AttackAreaType attackAreaType) throws PWCGException 
    {
        attackArea = new McuAttackArea(attackAreaType);
        
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int attackMcuTriggerDistance = productSpecificConfiguration.getBombFinalApproachDistance();

        missionBeginUnit = new MissionBeginSelfDeactivatingCheckZone("Air Ground Check Zone", targetDefinition.getPosition(), attackMcuTriggerDistance);
        attackArea = FlightAttackAreaFactory.createAttackArea(flightInformation.getFlightType(), targetDefinition.getPosition(), flightInformation.getAltitude(), attackTime);
        createSequence(attackTime) ;
        linkTargets() ;
    }
    
    public void finalize(PlaneMcu plane)
    {
        createObjectAssociations(plane);
    }
    
    private void createObjectAssociations(PlaneMcu plane)
    {
        missionBeginUnit.setCheckZoneTriggerObject(plane.getLinkTrId());
        attackArea.setObject(plane.getLinkTrId());
    }

    private void createSequence(int attackTime) 
    {
        activateTimer.setName("Attack Area Timer");      
        activateTimer.setDesc("Attack Area Timer");
        activateTimer.setPosition(targetDefinition.getPosition());
        activateTimer.setTimer(1);              

        deactivateEntity.setName("Attack Area Deactivate");
        deactivateEntity.setDesc("Attack Area Deactivate");
        deactivateEntity.setOrientation(new Orientation());
        deactivateEntity.setPosition(targetDefinition.getPosition());             
        
        deactivateTimer.setName("Attack Area Deactivate Timer");
        deactivateTimer.setDesc("Attack Area Deactivate Timer");
        deactivateTimer.setOrientation(new Orientation());
        deactivateTimer.setPosition(targetDefinition.getPosition());              
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

    public McuTimer getActivateTimer()
    {
        return activateTimer;
    }

    public void setLinkToNextTarget(int targetIndex)
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

    public Coordinate getPosition()
    {
        return attackArea.getPosition();
    }

    public void changeAttackAreaPosition(Coordinate newPosition)
    {
        attackArea.setPosition(newPosition);
    }
}
