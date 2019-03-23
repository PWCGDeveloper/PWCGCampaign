package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.balloondefense.AiBalloonDefenseFlight;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.group.VirtualWayPoint;

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

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(campaign, mission);
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
            coopConverter.convertToCoop(mission.getMissionFlightBuilder());
        }
    }


    private void setFlightAttackMcu() throws PWCGException  
    {
        List<Flight> axisFlights = mission.getMissionFlightBuilder().getAllAxisFlights();
        List<Flight> alliedFlights = mission.getMissionFlightBuilder().getAllAlliedFlights();
        
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

    // TODO COOP TEST THIS - verify setting virtual flight triggers to player plane ids
    private void setCzTriggers() throws PWCGException  
    {
        for (Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            // Trigger the flight on proximity to player
            triggerOtherFlightCZFromPlayerFlight(flight);
            for (Unit linkedUnit  : flight.getLinkedUnits())
            {
                if (linkedUnit instanceof Flight)
                {
                    Flight linkedFlight = (Flight)linkedUnit;
                    triggerOtherFlightCZFromPlayerFlight(linkedFlight);
                }
            }
        }
    }

    // TODO COOP TEST THIS - verify setting virtual flight triggers to player plane ids
    private void triggerOtherFlightCZFromPlayerFlight(Flight virtualFlight) throws PWCGException 
    {
    	if (!virtualFlight.isVirtual())
    	{
    		return;
    	}
    	
        // Makes linked activate on the players plane rather than any coalition
        WaypointPackage waypointpackage = virtualFlight.getWaypointPackage();
        if (waypointpackage != null && waypointpackage instanceof VirtualWaypointPackage)
        {
            VirtualWaypointPackage virtualWaypointPackage = (VirtualWaypointPackage)waypointpackage;
            for (VirtualWayPoint vwp : virtualWaypointPackage.getVirtualWaypoints())
            {
                if (vwp != null && vwp instanceof VirtualWayPoint)
                {
                    VirtualWayPoint vwpCZ = (VirtualWayPoint)vwp;
                    McuCheckZone checkZone = vwpCZ.getTriggerCheckZone().getCheckZone();
                    checkZone.triggerCheckZoneByMultipleObjects(virtualFlight.getMission().getMissionFlightBuilder().determinePlayerPlaneIds());
                }
            }
        }
        
        if (virtualFlight instanceof AiBalloonDefenseFlight)
        {
            AiBalloonDefenseFlight balloonDefenseFlight = (AiBalloonDefenseFlight)virtualFlight;
            balloonDefenseFlight.setBalloonCheckZoneForPlayer(virtualFlight.getMission().getMissionFlightBuilder().determinePlayerPlaneIds());
        }
    }

}
