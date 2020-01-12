package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightFinalizer;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
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
    
    public List<IFlight> finalizeMissionFlights() throws PWCGException 
    {
        convertForCoop();
        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            FlightFinalizer finalizer = new FlightFinalizer(flight);
            finalizer.finalizeFlight();
        }
        
        AiAdjuster aiAdjuster = new AiAdjuster(campaign);
        aiAdjuster.adjustAI(mission);

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(campaign, mission);
        List<IFlight> finalizedMissionFlights = missionFlightKeeper.keepLimitedFlights();

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
        List<IFlight> axisFlights = mission.getMissionFlightBuilder().getAllFlightsForSide(Side.AXIS);
        List<IFlight> alliedFlights = mission.getMissionFlightBuilder().getAllFlightsForSide(Side.ALLIED);
        
        for (IFlight axisFlight : axisFlights)
        {
            for (IFlight alliedFlight : alliedFlights)
            {
                axisFlight.getFlightData().getFlightPlanes().addFlightTarget(alliedFlight);
            }
        }
        
        for (IFlight alliedFlight : alliedFlights)
        {
            for (IFlight axisFlight : axisFlights)
            {
                alliedFlight.getFlightData().getFlightPlanes().addFlightTarget(axisFlight);
            }
        }
    }

    private void setFlightAttackMcuForBalloons() throws PWCGException  
    {
        List<IFlight> axisFlights = mission.getMissionFlightBuilder().getAllFlightsForSide(Side.AXIS);
        List<IFlight> alliedFlights = mission.getMissionFlightBuilder().getAllFlightsForSide(Side.ALLIED);

        List<IGroundUnit> axisBalloons = mission.getMissionGroundUnitManager().getBalloonsForSide(Side.AXIS);
        List<IGroundUnit> alliedBalloons = mission.getMissionGroundUnitManager().getBalloonsForSide(Side.ALLIED);
        
        for (IGroundUnit axisBalloon : axisBalloons)
        {
            for (IFlight alliedFlight : alliedFlights)
            {
                alliedFlight.getFlightData().getFlightPlanes().addGroundUnitTarget(axisBalloon);
            }
        }
        
        for (IGroundUnit alliedBalloon : alliedBalloons)
        {
            for (IFlight axisFlight : axisFlights)
            {
                axisFlight.getFlightData().getFlightPlanes().addGroundUnitTarget(alliedBalloon);
            }
        }
    }

    private void setCzTriggers() throws PWCGException  
    {
        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            // Trigger the flight on proximity to player
            triggerOtherFlightCZFromPlayerFlight(flight);
            for (IFlight linkedFlight : flight.getFlightData().getLinkedFlights().getLinkedFlights())
            {
                triggerOtherFlightCZFromPlayerFlight(linkedFlight);
            }
        }
    }

    private void triggerOtherFlightCZFromPlayerFlight(IFlight flight) throws PWCGException 
    {
        if (flight.getFlightData().getFlightInformation().isVirtual())
        {
            IVirtualWaypointPackage virtualWaypointPackage = flight.getFlightData().getVirtualWaypointPackage();
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
