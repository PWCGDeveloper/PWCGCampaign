package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.virtual.IVirtualWaypointPackage;
import pwcg.mission.mcu.group.virtual.IVirtualWaypoint;
import pwcg.mission.utils.AiAdjuster;

public class MissionUnitFinalizer
{
    private Campaign campaign;
    private Mission mission;
    
    public MissionUnitFinalizer (Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public void finalizeMissionUnits() throws PWCGException 
    {
        convertForCoop();
        finalizeFlights();        
        adjustAI();        
        setCzTriggers();
    }

    private void convertForCoop() throws PWCGException, PWCGException
    {
        if (campaign.isCoop())
        {
            MissionCoopConverter coopConverter = new MissionCoopConverter();
            coopConverter.convertToCoop(mission);
        }
    }

    private void finalizeFlights() throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            flight.finalizeFlight();
        }
    }

    private void adjustAI() throws PWCGException
    {
        AiAdjuster aiAdjuster = new AiAdjuster();
        aiAdjuster.adjustAI(mission);
    }

    private void setCzTriggers() throws PWCGException  
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            triggerOtherFlightCZFromPlayerFlight(flight);
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
                    for (int planeIndex : flight.getMission().getUnits().determinePlayerVehicleIds())
                    {
                        vwp.setVwpTriggerObject(planeIndex);
                    }
                }
            }
        }
    }
}
