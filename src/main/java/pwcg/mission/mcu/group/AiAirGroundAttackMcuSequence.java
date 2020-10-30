package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.FlightAttackAreaFactory;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class AiAirGroundAttackMcuSequence implements IAirGroundAttackMcuSequence
{    
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    private McuTimer activateTimer = new McuTimer();
    private McuTimer deactivateTimer = new McuTimer();
    protected McuDeactivate deactivateEntity = new McuDeactivate();
    private  McuAttackArea attackArea;

    public AiAirGroundAttackMcuSequence(IFlight flight)
    {
        this.flightInformation = flight.getFlightInformation();
        this.targetDefinition = flight.getTargetDefinition();
    }
    
    @Override
    public void createAttackArea(int attackTime, AttackAreaType attackAreaType) throws PWCGException 
    {
        attackArea = FlightAttackAreaFactory.createAttackArea(flightInformation.getFlightType(), targetDefinition.getPosition(), flightInformation.getAltitude(), attackTime);
        createSequence(attackTime) ;
        createTargetAssociations() ;
    }
    
    @Override
    public void setAttackToTriggerOnPlane(int planeIndex)
    {
        attackArea.setObject(planeIndex);
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        activateTimer.write(writer);
        attackArea.write(writer);
        deactivateTimer.write(writer);
        deactivateEntity.write(writer);
    }

    @Override
    public void setLinkToNextTarget(int targetIndex)
    {
        deactivateTimer.setTarget(targetIndex);
    }

    @Override
    public McuAttackArea getAttackAreaMcu()
    {
        return attackArea;
    }

    @Override
    public Coordinate getPosition()
    {
        return attackArea.getPosition();
    }

    private void createTargetAssociations() 
    {
        activateTimer.setTarget(attackArea.getIndex());
        activateTimer.setTarget(deactivateTimer.getIndex());
        deactivateTimer.setTarget(deactivateEntity.getIndex());
        deactivateEntity.setTarget(attackArea.getIndex());
    }

    private void createSequence(int attackTime) 
    {
        activateTimer.setName("Attack Area Timer");      
        activateTimer.setDesc("Attack Area Timer");
        activateTimer.setPosition(targetDefinition.getPosition());
        activateTimer.setTime(1);              

        deactivateEntity.setName("Attack Area Deactivate");
        deactivateEntity.setDesc("Attack Area Deactivate");
        deactivateEntity.setOrientation(new Orientation());
        deactivateEntity.setPosition(targetDefinition.getPosition());             
        
        deactivateTimer.setName("Attack Area Deactivate Timer");
        deactivateTimer.setDesc("Attack Area Deactivate Timer");
        deactivateTimer.setOrientation(new Orientation());
        deactivateTimer.setPosition(targetDefinition.getPosition());              
        deactivateTimer.setTime(attackTime);              
    }

    @Override
    public void setLinkToAttack(McuWaypoint linkToAttack)
    {
        linkToAttack.setTarget(activateTimer.getIndex());        
    }
}
