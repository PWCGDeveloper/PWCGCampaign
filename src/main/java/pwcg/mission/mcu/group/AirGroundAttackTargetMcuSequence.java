package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuAttack;
import pwcg.mission.mcu.McuCounter;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuProximity;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class AirGroundAttackTargetMcuSequence
{    
    private static final int PROXIMITY_DISTANCE = 3000;
    private IFlight flight;
    private List<IVehicle> vehicles;
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    private MissionBeginUnit missionBeginUnit;
    private McuTimer proximityStartTimer = new McuTimer();
    private McuProximity mcuProximity = new McuProximity(PROXIMITY_DISTANCE);
    private McuTimer attackActivateTimer = new McuTimer();
    private McuTimer attackTimeoutTimer = new McuTimer();
    private McuDeactivate attackDeactivateEntity = new McuDeactivate();
    private McuAttack attackTarget = new McuAttack();
    private McuCounter bingoBombsCounter;
    private McuTimer exitAttackTimer = new McuTimer();
    private McuDeactivate killTimeoutDeactivateEntity = new McuDeactivate();
    private McuForceComplete forceCompleteDropOrdnance;
    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

    public AirGroundAttackTargetMcuSequence(IFlight flight, List<IVehicle> vehicles)
    {
        this.flight = flight;
        this.vehicles = vehicles;
        this.flightInformation = flight.getFlightInformation();
        this.targetDefinition = flight.getTargetDefinition();
        missionBeginUnit = new MissionBeginUnit(targetDefinition.getPosition());
    }
        
    
    public void createAttackSequence(int maxAttackTimeSeconds, int bingoLoiterTimeSeconds) throws PWCGException 
    {
        buildEntities(maxAttackTimeSeconds, bingoLoiterTimeSeconds);
        linkEntities();        
        makeSubtitles();
    }

    private void buildEntities(int maxAttackTimeSeconds, int bingoLoiterTimeSeconds)
    {
        buildBingoCounter();
        buildAttackAreaElements(maxAttackTimeSeconds, bingoLoiterTimeSeconds);
    }

    private void linkEntities() throws PWCGException
    {
        createTargetAssociations();
        setProximityTrigger();
        setVehiclesToAttack();
        setForceDropOrdnance();
        setLinkToNextTarget();
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        proximityStartTimer.write(writer);
        mcuProximity.write(writer);
        attackActivateTimer.write(writer);
        attackTarget.write(writer);
        attackTimeoutTimer.write(writer);
        attackDeactivateEntity.write(writer);
        bingoBombsCounter.write(writer);
        exitAttackTimer.write(writer);
        killTimeoutDeactivateEntity.write(writer);
        forceCompleteDropOrdnance.write(writer);
        
        McuSubtitle.writeSubtitles(subTitleList, writer);
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

    private void setProximityTrigger() throws PWCGException 
    {
        List<PlaneMcu> planes = flight.getFlightPlanes().getPlanes();
        mcuProximity.setObject(planes.get(0).getLinkTrId());
        mcuProximity.setObject(vehicles.get(0).getLinkTrId());
    }

    private void setForceDropOrdnance() throws PWCGException 
    {
        List<PlaneMcu> planes = flight.getFlightPlanes().getPlanes();        
        for (PlaneMcu plane : planes)
        {
            forceCompleteDropOrdnance.setObject(plane.getLinkTrId());
            setBingoBombsEventsForPlane(plane);
        }
    }

    private void setVehiclesToAttack() throws PWCGException
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            attackTarget.setObject(plane.getLinkTrId());
        }

        for (IVehicle vehicle : vehicles)
        {
            attackTarget.setAttackTarget(vehicle.getLinkTrId());
        }
        
    }

    private void setLinkToNextTarget() throws PWCGException
    {
        McuWaypoint egressWaypoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        exitAttackTimer.setTimerTarget(egressWaypoint.getIndex());
    }

    private void createTargetAssociations() 
    {
        missionBeginUnit.linkToMissionBegin(proximityStartTimer.getIndex());
        proximityStartTimer.setTimerTarget(mcuProximity.getIndex());
        mcuProximity.setProximityTarget(attackActivateTimer.getIndex());
        
        attackActivateTimer.setTimerTarget(attackTarget.getIndex());
        attackActivateTimer.setTimerTarget(attackTimeoutTimer.getIndex());
        attackActivateTimer.setTimerTarget(bingoBombsCounter.getIndex());
        
        bingoBombsCounter.setCounterTarget(exitAttackTimer.getIndex());
        attackTimeoutTimer.setTimerTarget(exitAttackTimer.getIndex());

        exitAttackTimer.setTimerTarget(killTimeoutDeactivateEntity.getIndex());
        exitAttackTimer.setTimerTarget(attackDeactivateEntity.getIndex());
        exitAttackTimer.setTimerTarget(forceCompleteDropOrdnance.getIndex());
        
        attackDeactivateEntity.setDeactivateTarget(attackTarget.getIndex());
        killTimeoutDeactivateEntity.setDeactivateTarget(attackTimeoutTimer.getIndex());
    }

    private void makeSubtitles() throws PWCGException
    {
        Coordinate subtitlePosition = targetDefinition.getPosition();
        
        McuSubtitle proximitySubtitle = McuSubtitle.makeActivatedSubtitle("Proximity triggered ", subtitlePosition);
        mcuProximity.setProximityTarget(proximitySubtitle.getIndex());
        subTitleList.add(proximitySubtitle);
        
        McuSubtitle attackTriggeredSubtitle = McuSubtitle.makeActivatedSubtitle("Attack triggered ", subtitlePosition);
        attackActivateTimer.setTimerTarget(attackTriggeredSubtitle.getIndex());
        subTitleList.add(attackTriggeredSubtitle);

        McuSubtitle bingoSubtitle = McuSubtitle.makeActivatedSubtitle("Stop attack due to bingo count ", subtitlePosition);
        bingoBombsCounter.setCounterTarget(bingoSubtitle.getIndex());
        subTitleList.add(bingoSubtitle);
        
        McuSubtitle timeoutSubtitle = McuSubtitle.makeActivatedSubtitle("Stop attack due to attack timeout: ", subtitlePosition);
        attackTimeoutTimer.setTimerTarget(timeoutSubtitle.getIndex());
        subTitleList.add(timeoutSubtitle);
    }

    private void buildAttackAreaElements(int attackTime, int bingoLoiterTimeSeconds) 
    {        
        proximityStartTimer.setName("Attack Target Proximity Timer");      
        proximityStartTimer.setDesc("Attack Target Proximity Timer");
        proximityStartTimer.setPosition(targetDefinition.getPosition());
        proximityStartTimer.setTime(1);              
        
        mcuProximity.setName("Attack Target Proximity");      
        mcuProximity.setDesc("Attack Target Proximity");
        mcuProximity.setPosition(targetDefinition.getPosition());

        attackActivateTimer.setName("Attack Target Activate Timer");      
        attackActivateTimer.setDesc("Attack Target Activate Timer");
        attackActivateTimer.setPosition(targetDefinition.getPosition());        
        attackActivateTimer.setTime(1);              

        attackTimeoutTimer.setName("Attack Target Timeout Timer");
        attackTimeoutTimer.setDesc("Attack Target Timeout Timer");
        attackTimeoutTimer.setOrientation(new Orientation());
        attackTimeoutTimer.setPosition(targetDefinition.getPosition());              
        attackTimeoutTimer.setTime(attackTime);              

        attackTarget.setName("Attack Target");
        attackTarget.setDesc("Attack Target");
        attackTarget.setOrientation(new Orientation());
        attackTarget.setPosition(targetDefinition.getPosition());              

        attackDeactivateEntity.setName("Attack Target Deactivate");
        attackDeactivateEntity.setDesc("Attack Target Deactivate");
        attackDeactivateEntity.setOrientation(new Orientation());
        attackDeactivateEntity.setPosition(targetDefinition.getPosition());             
        
        exitAttackTimer.setName("Attack Target Exit Timer");
        exitAttackTimer.setDesc("Attack Target Exit Timer");
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
        forceCompleteDropOrdnance = new McuForceComplete(WaypointPriority.PRIORITY_HIGH, emergencyDropOrdnance);
        forceCompleteDropOrdnance.setName("Escort Cover Force Complete");
        forceCompleteDropOrdnance.setDesc("Escort Cover Force Complete");
        forceCompleteDropOrdnance.setOrientation(new Orientation());
        forceCompleteDropOrdnance.setPosition(targetDefinition.getPosition());
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
}
