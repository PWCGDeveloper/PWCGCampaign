package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.mcu.group.VirtualWayPoint;
import pwcg.mission.utils.AiAdjuster;

public class MissionFlightFinalizer
{
    private Campaign campaign;
    private Mission mission;
    
    public MissionFlightFinalizer (Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public List<Flight> finalizeMissionFlights() throws PWCGException 
    {
        convertForCoop();
        for (Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            flight.finalizeFlight();
        }
        
        AiAdjuster aiAdjuster = new AiAdjuster(campaign);
        aiAdjuster.adjustAI(mission);

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(campaign, mission);
        List<Flight> finalizedMissionFlights = missionFlightKeeper.keepLimitedFlights();

        setFlightAttackMcuForPlanes();
        setFlightAttackMcuForBalloons();
        
        setCzTriggers();
        
        return finalizedMissionFlights;
    }

    private void convertForCoop() throws PWCGException, PWCGException
    {
        if (campaign.isCoop())
        {
            MissionCoopConverter coopConverter = new MissionCoopConverter();
            coopConverter.convertToCoop(mission.getMissionFlightBuilder());
        }
    }

    private void setFlightAttackMcuForPlanes() throws PWCGException  
    {
        List<Flight> axisFlights = mission.getMissionFlightBuilder().getAllFlightsForSide(Side.AXIS);
        List<Flight> alliedFlights = mission.getMissionFlightBuilder().getAllFlightsForSide(Side.ALLIED);
        
        for (Flight axisFlight : axisFlights)
        {
            for (Flight alliedFlight : alliedFlights)
            {
                axisFlight.addFlightTarget(alliedFlight);
            }
        }
        
        for (Flight alliedFlight : alliedFlights)
        {
            for (Flight axisFlight : axisFlights)
            {
                alliedFlight.addFlightTarget(axisFlight);
            }
        }
    }

    private void setFlightAttackMcuForBalloons() throws PWCGException  
    {
        List<Flight> axisFlights = mission.getMissionFlightBuilder().getAllFlightsForSide(Side.AXIS);
        List<Flight> alliedFlights = mission.getMissionFlightBuilder().getAllFlightsForSide(Side.ALLIED);

        List<IGroundUnit> axisBalloons = mission.getMissionGroundUnitManager().getBalloonsForSide(Side.AXIS);
        List<IGroundUnit> alliedBalloons = mission.getMissionGroundUnitManager().getBalloonsForSide(Side.ALLIED);
        
        for (IGroundUnit axisBalloon : axisBalloons)
        {
            for (Flight alliedFlight : alliedFlights)
            {
                alliedFlight.addGroundUnitTarget(axisBalloon);
            }
        }
        
        for (IGroundUnit alliedBalloon : alliedBalloons)
        {
            for (Flight axisFlight : axisFlights)
            {
                axisFlight.addGroundUnitTarget(alliedBalloon);
            }
        }
    }

    private void setCzTriggers() throws PWCGException  
    {
        for (Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            // Trigger the flight on proximity to player
            triggerOtherFlightCZFromPlayerFlight(flight);
            for (IUnit linkedUnit  : flight.getLinkedUnits())
            {
                if (linkedUnit instanceof Flight)
                {
                    Flight linkedFlight = (Flight)linkedUnit;
                    triggerOtherFlightCZFromPlayerFlight(linkedFlight);
                }
            }
        }
    }

    private void triggerOtherFlightCZFromPlayerFlight(Flight flight) throws PWCGException 
    {
        if (flight.isVirtual())
        {
            VirtualWaypointPackage virtualWaypointPackage = flight.getVirtualWaypointPackage();
            for (VirtualWayPoint vwp : virtualWaypointPackage.getVirtualWaypoints())
            {
                if (vwp != null && vwp instanceof VirtualWayPoint)
                {
                    VirtualWayPoint vwpCZ = (VirtualWayPoint)vwp;
                    for (int planeIndex : flight.getMission().getMissionFlightBuilder().determinePlayerPlaneIds())
                    {
                        vwpCZ.setVirtualWaypointTriggerObject(planeIndex);
                    }
                }
            }
        }
    }
}
