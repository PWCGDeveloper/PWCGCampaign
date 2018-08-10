package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.balloondefense.AiBalloonDefenseFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class MissionFlightFinalizer
{
    private Campaign campaign;
    private MissionFlightBuilder missionFlightBuilder;
    
    public MissionFlightFinalizer (Campaign campaign, MissionFlightBuilder missionFlightBuilder)
    {
        this.campaign = campaign;
        this.missionFlightBuilder = missionFlightBuilder;
    }
    
    public List<Flight> finalizeMissionFlights() throws PWCGException 
    {
        convertForCoop();
        for (Flight flight : missionFlightBuilder.getAllAerialFlights())
        {
            flight.finalizeFlight();
        }

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(campaign, missionFlightBuilder);
        List<Flight> finalizedMissionFlights = missionFlightKeeper.keepLimitedFlights();

        setFlightAttackMcu();
        
        setCzTriggers();
        
        return finalizedMissionFlights;
    }

    private void convertForCoop() throws PWCGException, PWCGException
    {
        if (campaign.getCampaignData().isCoop())
        {
            MissionCoopConverter coopConverter = new MissionCoopConverter();
            coopConverter.convertToCoop(missionFlightBuilder);
        }
    }


    private void setFlightAttackMcu() throws PWCGException  
    {
        List<Flight> axisFlights = missionFlightBuilder.getAllAxisFlights();
        List<Flight> alliedFlights = missionFlightBuilder.getAllAlliedFlights();
        
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

    private void setCzTriggers() throws PWCGException  
    {
        for (Flight flight : missionFlightBuilder.getAllAerialFlights())
        {
            // Trigger the flight on proximity to player
            triggerOtherFlightCZFromMyFlight(flight);
            for (Unit linkedUnit  : flight.getLinkedUnits())
            {
                if (linkedUnit instanceof Flight)
                {
                    Flight linkedFlight = (Flight)linkedUnit;
                    triggerOtherFlightCZFromMyFlight(linkedFlight);
                }
            }
        }
    }

    private void triggerOtherFlightCZFromMyFlight(Flight flight) throws PWCGException 
    {
        List<PlaneMCU> playerPlanes = missionFlightBuilder.getPlayerFlight().getPlayerPlanes();
        if (!playerPlanes.isEmpty())
        {
            // Makes linked activate on the players plane rather than any coalition
            WaypointPackage waypointpackage = flight.getWaypointPackage();
            if (waypointpackage != null && waypointpackage instanceof VirtualWaypointPackage)
            {
                VirtualWaypointPackage virtualWaypointPackage = (VirtualWaypointPackage)waypointpackage;
                for (VirtualWayPoint vwp : virtualWaypointPackage.getVirtualWaypoints())
                {
                    if (vwp != null && vwp instanceof VirtualWayPoint)
                    {
                        VirtualWayPoint vwpCZ = (VirtualWayPoint)vwp;
                        SelfDeactivatingCheckZone selfDeactivatingCheckZone = vwpCZ.getTriggerCheckZone();
                        McuCheckZone checkZone = selfDeactivatingCheckZone.getCheckZone();
                        checkZone.setCheckZoneForPlayer(flight.getMission());
                    }
                }
            }
            
            if (flight instanceof AiBalloonDefenseFlight)
            {
                AiBalloonDefenseFlight balloonDefenseFlight = (AiBalloonDefenseFlight)flight;
                balloonDefenseFlight.setBalloonCheckZoneForPlayer(playerPlanes.get(0).getEntity().getIndex());
            }
        }
    }

}
