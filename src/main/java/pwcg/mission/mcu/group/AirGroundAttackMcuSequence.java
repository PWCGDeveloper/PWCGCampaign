package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.AttackAreaFactory;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuCounter;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.target.TargetDefinition;

public class AirGroundAttackMcuSequence
{    
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    private MissionBeginSelfDeactivatingCheckZone missionBeginUnitCheckZone;
    private McuTimer activateTimer = new McuTimer();
    private McuTimer attackAreaTimeoutTimer = new McuTimer();
    private McuDeactivate attackAreaDeactivateEntity = new McuDeactivate();
    private McuAttackArea attackArea;
    private McuCounter bingoBombsCounter;
    private McuTimer exitAttackTimer = new McuTimer();
    private McuDeactivate killTimeoutDeactivateEntity = new McuDeactivate();
    private McuForceComplete forceCompleteDropOrnance;

    public AirGroundAttackMcuSequence(IFlight flight)
    {
        this.flightInformation = flight.getFlightInformation();
        this.targetDefinition = flight.getTargetDefinition();
    }
            
    public void createAttackSequence(int maxAttackTimeSeconds, int bingoLoiterTimeSeconds, AttackAreaType attackAreaType) throws PWCGException 
    {
        buildBingoCounter();
        buildAttackAreaTrigger();
        createAttackArea(maxAttackTimeSeconds, attackAreaType);
        buildAttackAreaElements(maxAttackTimeSeconds, bingoLoiterTimeSeconds);
        createTargetAssociations();
    }

    private void buildBingoCounter()
    {
        if (flightInformation.getMission().isAAATruckMission())
        {
            bingoBombsCounter = new McuCounter(50, 0);        
        }
        else
        {
            int bingoCount = (flightInformation.getPlanes().size() / 2) + 1;
            if (bingoCount < 2)
            {
                bingoCount = 2;
            }
            
            bingoBombsCounter = new McuCounter(bingoCount, 0);
        }
    }


    private void buildAttackAreaTrigger()
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int attackMcuTriggerDistance = productSpecificConfiguration.getBombFinalApproachDistance();
        missionBeginUnitCheckZone = new MissionBeginSelfDeactivatingCheckZone("Air Ground Check Zone", targetDefinition.getPosition(), attackMcuTriggerDistance);
    }

    private void createAttackArea(int maxAttackTimeSeconds, AttackAreaType attackAreaType)
    {
        attackArea = AttackAreaFactory.createAttackArea(flightInformation, targetDefinition.getPosition(), maxAttackTimeSeconds);
    }

    public void setAttackToTriggerOnPlane(List<PlaneMcu> planes) throws PWCGException
    {
        for (PlaneMcu plane : planes)
        {
            missionBeginUnitCheckZone.setCheckZoneTriggerObject(plane.getLinkTrId());
            attackArea.setObject(plane.getLinkTrId());
            forceCompleteDropOrnance.setObject(plane.getLinkTrId());
            setBingoBombsEventsForPlane(plane);
        }
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnitCheckZone.write(writer);
        activateTimer.write(writer);
        attackArea.write(writer);
        attackAreaTimeoutTimer.write(writer);
        attackAreaDeactivateEntity.write(writer);
        bingoBombsCounter.write(writer);
        exitAttackTimer.write(writer);
        killTimeoutDeactivateEntity.write(writer);
        forceCompleteDropOrnance.write(writer);
    }

    public void setLinkToNextTarget(int targetIndex)
    {
        exitAttackTimer.setTimerTarget(targetIndex);
    }

    public BaseFlightMcu getAttackAreaMcu()
    {
        return attackArea;
    }

    public Coordinate getPosition()
    {
        return attackArea.getPosition();
    }

    private void createTargetAssociations() 
    {
        missionBeginUnitCheckZone.linkCheckZoneTarget(activateTimer.getIndex());
        activateTimer.setTimerTarget(attackArea.getIndex());
        activateTimer.setTimerTarget(attackAreaTimeoutTimer.getIndex());
        activateTimer.setTimerTarget(bingoBombsCounter.getIndex());
        
        bingoBombsCounter.setCounterTarget(exitAttackTimer.getIndex());
        attackAreaTimeoutTimer.setTimerTarget(exitAttackTimer.getIndex());

        exitAttackTimer.setTimerTarget(killTimeoutDeactivateEntity.getIndex());
        exitAttackTimer.setTimerTarget(attackAreaDeactivateEntity.getIndex());
        exitAttackTimer.setTimerTarget(forceCompleteDropOrnance.getIndex());
        
        attackAreaDeactivateEntity.setDeactivateTarget(attackArea.getIndex());
        killTimeoutDeactivateEntity.setDeactivateTarget(attackAreaTimeoutTimer.getIndex());
    }

    private void buildAttackAreaElements(int attackTime, int bingoLoiterTimeSeconds) 
    {
        activateTimer.setName("Attack Area Start Timer");      
        activateTimer.setDesc("Attack Area Start Timer");
        activateTimer.setPosition(targetDefinition.getPosition());
        activateTimer.setTime(1);              
        
        attackAreaTimeoutTimer.setName("Attack Area Timeout Timer");
        attackAreaTimeoutTimer.setDesc("Attack Area Timeout Timer");
        attackAreaTimeoutTimer.setOrientation(new Orientation());
        attackAreaTimeoutTimer.setPosition(targetDefinition.getPosition());              
        attackAreaTimeoutTimer.setTime(attackTime);              

        attackAreaDeactivateEntity.setName("Attack Area Deactivate");
        attackAreaDeactivateEntity.setDesc("Attack Area Deactivate");
        attackAreaDeactivateEntity.setOrientation(new Orientation());
        attackAreaDeactivateEntity.setPosition(targetDefinition.getPosition());             
        
        exitAttackTimer.setName("Attack Area Exit Timer");
        exitAttackTimer.setDesc("Attack Area Exit Timer");
        exitAttackTimer.setOrientation(new Orientation());
        exitAttackTimer.setPosition(targetDefinition.getPosition());              
        exitAttackTimer.setTime(bingoLoiterTimeSeconds);              

        bingoBombsCounter.setName("Bingo bombs counter");      
        bingoBombsCounter.setDesc("Bingo bombs counter");
        bingoBombsCounter.setPosition(targetDefinition.getPosition());

        killTimeoutDeactivateEntity.setName("Timeout Timer Deactivate");
        killTimeoutDeactivateEntity.setDesc("Timeout Time Deactivate");
        killTimeoutDeactivateEntity.setOrientation(new Orientation());
        killTimeoutDeactivateEntity.setPosition(targetDefinition.getPosition());

        int emergencyDropOrdnance = 1;
        forceCompleteDropOrnance = new McuForceComplete(WaypointPriority.PRIORITY_HIGH, emergencyDropOrdnance);
        forceCompleteDropOrnance.setName("Escort Cover Force Complete");
        forceCompleteDropOrnance.setDesc("Escort Cover Force Complete");
        forceCompleteDropOrnance.setOrientation(new Orientation());
        forceCompleteDropOrnance.setPosition(targetDefinition.getPosition());
    }
    
    private void setBingoBombsEventsForPlane(PlaneMcu plane) throws PWCGException
    {
        int bingoElement = McuEvent.ONPLANEBINGOBOMBS;
        if (!plane.getPlanePayload().isOrdnance())
        {
            bingoElement = McuEvent.ONPLANEBINGOAMMO;
        }
        
        McuEvent planeEvent = new McuEvent(bingoElement, bingoBombsCounter.getIndex());
        plane.addEvent(planeEvent);
    }

    public void setVehiclesToAttack(List<IVehicle> vehicles) throws PWCGException
    {
        for (IVehicle vehicle : vehicles)
        {
            attackArea.setAttackAreaTarget(vehicle.getLinkTrId());
        }
        
    }    
}
