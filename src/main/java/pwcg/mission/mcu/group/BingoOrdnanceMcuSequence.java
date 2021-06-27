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
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuCounter;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.target.TargetDefinition;

public class BingoOrdnanceMcuSequence
{    
    private IFlight flight;
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    private MissionBeginUnit missionBeginUnit;
    private McuTimer bingoBombsActivateTimer = new McuTimer();
    private McuCounter bingoBombsCounter;
    private McuTimer exitAttackTimer = new McuTimer();
    private McuForceComplete forceCompleteDropOrdnance;
    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

    public BingoOrdnanceMcuSequence(IFlight flight)
    {
        this.flight = flight;
        this.flightInformation = flight.getFlightInformation();
        this.targetDefinition = flight.getTargetDefinition();
        missionBeginUnit = new MissionBeginUnit(targetDefinition.getPosition());
    }
        
    
    public void createBingoBombsSequence(int bingoLoiterTimeSeconds, int egressWaypointIndex) throws PWCGException 
    {
        buildBingoCounter();
        buildAttackAreaElements(bingoLoiterTimeSeconds);
        createTargetAssociations();
        linkToForceOrdnanceDrop();
        setLinkToNextTarget(egressWaypointIndex);
        makeSubtitles();
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        bingoBombsActivateTimer.write(writer);
        bingoBombsCounter.write(writer);
        exitAttackTimer.write(writer);
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

    private void linkToForceOrdnanceDrop() throws PWCGException 
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            forceCompleteDropOrdnance.setObject(plane.getLinkTrId());
            setBingoBombsEventsForPlane(plane);
        }
    }

    private void setLinkToNextTarget(int targetIndex)
    {
        exitAttackTimer.setTarget(targetIndex);
    }

    private void createTargetAssociations() 
    {
        missionBeginUnit.linkToMissionBegin(bingoBombsActivateTimer.getIndex());
        bingoBombsActivateTimer.setTarget(bingoBombsCounter.getIndex());
        bingoBombsCounter.setTarget(exitAttackTimer.getIndex());
        exitAttackTimer.setTarget(forceCompleteDropOrdnance.getIndex());        
    }

    private void makeSubtitles() throws PWCGException
    {
        Coordinate subtitlePosition = targetDefinition.getPosition();
        
        McuSubtitle proximitySubtitle = McuSubtitle.makeActivatedSubtitle("Bingo bombs triggered ", subtitlePosition);
        bingoBombsActivateTimer.setTarget(proximitySubtitle.getIndex());
        subTitleList.add(proximitySubtitle);

        McuSubtitle bingoSubtitle = McuSubtitle.makeActivatedSubtitle("Stop attack due to bingo count ", subtitlePosition);
        bingoBombsCounter.setTarget(bingoSubtitle.getIndex());
        subTitleList.add(bingoSubtitle);
    }

    private void buildAttackAreaElements(int bingoLoiterTimeSeconds) 
    {        
        bingoBombsActivateTimer.setName("Attack Target Activate Timer");      
        bingoBombsActivateTimer.setDesc("Attack Target Activate Timer");
        bingoBombsActivateTimer.setPosition(targetDefinition.getPosition());        

        exitAttackTimer.setName("Attack Target Exit Timer");
        exitAttackTimer.setDesc("Attack Target Exit Timer");
        exitAttackTimer.setOrientation(new Orientation());
        exitAttackTimer.setPosition(targetDefinition.getPosition());              
        exitAttackTimer.setTime(bingoLoiterTimeSeconds);              

        bingoBombsCounter.setName("Bingo bombs counter");      
        bingoBombsCounter.setDesc("Bingo bombs counter");
        bingoBombsCounter.setPosition(targetDefinition.getPosition());

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
