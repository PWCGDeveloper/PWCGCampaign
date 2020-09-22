package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.virtual.IVirtualWaypointPackage;
import pwcg.mission.mcu.group.virtual.IVirtualWaypoint;
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
    
    public void finalizeMissionFlights() throws PWCGException 
    {
        convertForCoop();
        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            flight.finalizeFlight();
        }
        
        AiAdjuster aiAdjuster = new AiAdjuster(campaign);
        aiAdjuster.adjustAI(mission);
        
        setCzTriggers();
    }

    private void convertForCoop() throws PWCGException, PWCGException
    {
        if (campaign.isCoop())
        {
            MissionCoopConverter coopConverter = new MissionCoopConverter();
            coopConverter.convertToCoop(mission.getMissionFlightBuilder());
        }
    }

    private void setCzTriggers() throws PWCGException  
    {
        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            triggerOtherFlightCZFromPlayerFlight(flight);
            for (IFlight linkedFlight : flight.getLinkedFlights().getLinkedFlights())
            {
                triggerOtherFlightCZFromPlayerFlight(linkedFlight);
            }
        }
    }

    private void triggerOtherFlightCZFromPlayerFlight(IFlight flight) throws PWCGException 
    {
        if (flight.getFlightInformation().isVirtual())
        {
            IVirtualWaypointPackage virtualWaypointPackage = flight.getVirtualWaypointPackage();
            for (IVirtualWaypoint vwp : virtualWaypointPackage.getVirtualWaypoints())
            {
                if (vwp != null)
                {
                    for (int planeIndex : flight.getMission().getMissionFlightBuilder().determinePlayerPlaneIds())
                    {
                        vwp.setVwpTriggerObject(planeIndex);
                    }
                }
            }
        }
    }
}
